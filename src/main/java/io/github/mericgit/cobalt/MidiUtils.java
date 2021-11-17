package io.github.mericgit.cobalt;

import javax.sound.midi.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MidiUtils {
    private static final int NOTE_ON = 0x90;
    private static final int NOTE_OFF = 0x80;
    private static final int PROGRAM_CHANGE = 0xC0;
    private static final int META_END_OF_TRACK_TYPE = 0x2F;
    private static int PPQ;
    private static double gTempo;
    private static ArrayList<Note> finalProcess;
    private static double timeConverter;
    private static long currentTick = 0;

    public static ArrayList<Note> midiToNoteSequence(File file) throws Exception {
        Sequence sequence = MidiSystem.getSequence(file);
        MidiFileFormat midiFile = MidiSystem.getMidiFileFormat(file);
        Sequencer sequencer = MidiSystem.getSequencer(false);

        sequencer.setSequence(sequence);
        gTempo = sequencer.getTempoInBPM();
        System.out.println("Old Tempo: " + gTempo);
        PPQ = midiFile.getResolution();
        System.out.println("PPQ IS: " + PPQ);

        timeConverter = ((double) 60000 / (gTempo * PPQ));
        System.out.println("Ms per tick = " + timeConverter);

        ArrayList<Note> noteSequence = new ArrayList<Note>();
        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof MetaMessage) {
                    MetaMessage mm = (MetaMessage) message;
                    byte[] data = mm.getData();
                    if ((mm.getType() & 0xff) == 0x51 && data != null && data.length > 2)
                    {
                        int nTempo = ((data[0] & 0xFF) << 16)
                                | ((data[1] & 0xFF) << 8)
                                | (data[2] & 0xFF);
                        if (nTempo <= 0) {
                            gTempo = 60000000.0 / nTempo;
                        }
                        else {
                            gTempo = 60000000.0 / nTempo;
                            noteSequence.add(new Note(currentTick + 1,0,0,0,0,"TEMPO_CHANGE", (float) gTempo,0, 0,1));
                        }
                    }
                    else if ((mm.getType() & 0xff) == META_END_OF_TRACK_TYPE && data !=null) {
                        noteSequence.add(new Note(currentTick + 1,0,0,trackNumber,0,"TRACK_END", (float) gTempo,0,0, 3));
                    }
                }
                if (message instanceof ShortMessage) {
                    currentTick = event.getTick();
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() ==PROGRAM_CHANGE) {
                        noteSequence.add(new Note(currentTick+1,sm.getData1(),0,trackNumber,0,"PROGRAM_CHANGE",0,0,0, 2));
                    }
                    if (sm.getCommand() == NOTE_ON && sm.getData2() != 0) {
                        int key = sm.getData1();
                        long tick = event.getTick();
                        int bank = trackNumber;
                        int channel = sm.getChannel();
                        long mcTick = 0;
                        int velocity = sm.getData2();
                        noteSequence.add(new Note(tick, key, velocity, bank, mcTick,"TBD",1F,channel,0,0));
                        //System.out.print((new Note(tick, key, velocity, bank, mcTick,calcSample(key),calcFreq(key),channel,0)));
                    } else if (sm.getCommand() == NOTE_OFF || sm.getData2() == 0) {
                        int key = sm.getData1();
                        long tick = event.getTick();
                        int bank = trackNumber;
                        int channel = sm.getChannel();
                        long mcTick = 0;
                        noteSequence.add(new Note(tick, key, 0, bank, mcTick,"TBD",1F,channel,0,0));
                    }
                }
            }
        }
        System.out.println("Note Sequence");
        finalProcess = Mapper.calcRR(calculateTimeConverter(convertNonDelta(bubbleSort(noteSequence))));
        //System.out.println(finalProcess);
        return finalProcess;
    }

    private static ArrayList<Note> calculateTimeConverter(ArrayList<Note> soundProcess) {
        float tempo = (float) timeConverter;
        for (int i = 0; i < soundProcess.size(); i++) {
            if (soundProcess.get(i).getDataF1() == 1.0){
                tempo = soundProcess.get(i).getFreq();
                //System.out.println("VAL" + ((float) 60000 / (tempo*PPQ)));
            }
            soundProcess.get(i).setMcTick(Math.round(  soundProcess.get(i).getMcTick() * ((float) 60000 / (tempo*PPQ)) ));
        }
        return soundProcess;
    }

    private static ArrayList<Note> convertNonDelta(ArrayList<Note> soundProcess) {
        ArrayList<Note> copy = new ArrayList<>();
        copy.add(soundProcess.get(0));
        for (int i = 1; i < soundProcess.size(); i++) {
            copy.add(new Note(soundProcess.get(i).getTick(), soundProcess.get(i).getKey(), soundProcess.get(i).getVelocity(), soundProcess.get(i).getBank(), (soundProcess.get(i).getTick() - soundProcess.get(i - 1).getTick()),soundProcess.get(i).getSample(),soundProcess.get(i).getFreq(),soundProcess.get(i).getChannel(),soundProcess.get(i).getDuration(), soundProcess.get(i).getDataF1()));
        }
        return copy;
    }

    public void quickSort(ArrayList<Note> soundProcess, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(soundProcess, begin, end);
            quickSort(soundProcess, begin, partitionIndex-1);
            quickSort(soundProcess, partitionIndex+1, end);
        }
    }

    private int partition(ArrayList<Note> soundProcess, int begin, int end) {
        long pivot = soundProcess.get(soundProcess.size() - 1).getTick();
        int i = (begin-1);
        for (int j = begin; j < end; j++) {
            if (soundProcess.get(j).getTick() <= pivot) {
                i++;
                Collections.swap(soundProcess,i,j);
            }
        }
        Collections.swap(soundProcess,(i+1),soundProcess.size() - 1);
        return i+1;
    }


    private static ArrayList<Note> quickSort2 (ArrayList < Note > soundProcess)
    {
        if (soundProcess.size() <= 1)
            return soundProcess; // Already sorted
        ArrayList<Note> sorted = new ArrayList<Note>();
        ArrayList<Note> lesser = new ArrayList<Note>();
        ArrayList<Note> greater = new ArrayList<Note>();
        long pivot = soundProcess.get(soundProcess.size() - 1).getTick();
        for (int i = 0; i < soundProcess.size() - 1; i++) {
            if (soundProcess.get(i).getTick() < pivot)
                lesser.add(soundProcess.get(i));
            else
                greater.add(soundProcess.get(i));
        }
        lesser = quickSort2(lesser);
        greater = quickSort2(greater);
        lesser.add(soundProcess.get(soundProcess.size() - 1));
        lesser.addAll(greater);
        sorted = lesser;
        return sorted;
    }

    private static ArrayList<Note> bubbleSort(ArrayList<Note> soundProcess) {
        int n = soundProcess.size();
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (soundProcess.get(j).getTick() > soundProcess.get(j+1).getTick())
                {
                    Collections.swap(soundProcess,j+1,j);
                }
        return soundProcess;
    }


    public static int getPPQ() {
        return PPQ;
    }
    public static float getTempo() {
        return (float) gTempo;
    }
    public static ArrayList<Note> getFinalProcess() {
        return finalProcess;
    }

}



