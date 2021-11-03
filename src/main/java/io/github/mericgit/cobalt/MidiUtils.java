package io.github.mericgit.cobalt;

import javax.sound.midi.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MidiUtils {
    private static final int NOTE_ON = 0x90;
    private static final int NOTE_OFF = 0x80;
    private static final int PROGRAM_CHANGE = 0xC0;
    private static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private static final int DEFAULT_TEMPO_MPQ = 500000; // 120bpm
    private static final int META_END_OF_TRACK_TYPE = 0x2F;
    private static final int META_TEMPO_TYPE = 0x51;
    public static double gTempo;
    private static ArrayList<Note> finalProcess;
    private static double timeConverter;
    private static long currentTick = 0;
    public static ArrayList<Note> midiToNoteSequence(File file) throws Exception {
        Sequence sequence = MidiSystem.getSequence(file);
        MidiFileFormat midiFile = MidiSystem.getMidiFileFormat(file);
        Sequencer sequencer = MidiSystem.getSequencer(false);
        sequencer.setSequence(sequence);
        gTempo = (int) sequencer.getTempoInBPM();
        System.out.println("Old Tempo: " + gTempo);
        int PPQ = midiFile.getResolution();
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
                           // timeConverter = ((double) 60000 / (gTempo * PPQ));
                            System.out.println("gTempo is: " + gTempo);
                            System.out.println("TimeConverter has been updated. New TC is: " + timeConverter);
                        }
                    }
                }
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() ==PROGRAM_CHANGE) {
                        long tick = event.getTick();
                        noteSequence.add(new Note(currentTick+1,sm.getData1(),128,0,0,"1",0,0,timeConverter));
                        System.out.println("Program Change");
                    }
                    if (sm.getCommand() == NOTE_ON && sm.getData2() != 0) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        long tick = event.getTick();
                        currentTick = event.getTick();
                        int bank = trackNumber;
                        int channel = sm.getChannel();
                        long mcTick = 0;
                        //long mcTick = Math.round((tick * timeConverter) / 50);
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        noteSequence.add(new Note(tick, key, velocity, bank, mcTick,calcSample(key),calcFreq(key),channel,timeConverter));
                        System.out.println("note add");
                    } else if (sm.getCommand() == NOTE_OFF || sm.getData2() == 0) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        long tick = event.getTick();
                        int bank = trackNumber;
                        //long mcTick = Math.round((tick * timeConverter) / 50);
                        int channel = sm.getChannel();
                        long mcTick = 0;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        noteSequence.add(new Note(tick, key, velocity, bank, mcTick,calcSample(key),calcFreq(key),channel,timeConverter));
                    }
                }
            }

            System.out.println();
        }
        /*
            System.out.println("Original Sequence");
            System.out.println(noteSequence);
            System.out.println("-------------------------");
            System.out.println("Sort");
            System.out.println(bubbleSort(noteSequence));
            System.out.println("----------");
            System.out.println(convertNonDelta(quickSort(noteSequence)));

         */
        System.out.println("Note Sequence");
        System.out.println(convertNonDelta(updateMcTick(quickSort(noteSequence))));
        finalProcess = convertNonDelta(updateMcTick(quickSort(noteSequence)));

        return noteSequence;
    }

    private static String calcSample(int key) {
        return "TBD";
    }

    private static float calcFreq(int key) {
        return 1F;
    }

    public static ArrayList<Note> getFinalProcess() {
        return finalProcess;
    }

    private static ArrayList<Note> updateMcTick(ArrayList<Note> soundProcess) {
        for (int i =0; i < soundProcess.size(); i++) {
            soundProcess.get(i).setMcTick(Math.round(soundProcess.get(i).getTick() * soundProcess.get(i).getTimeConv() ));

        }
        return soundProcess;
    }


    private static ArrayList<Note> convertNonDelta(ArrayList<Note> soundProcess) {
        ArrayList<Note> copy = new ArrayList<>();
        copy.add(soundProcess.get(0));
        for (int i = 1; i < soundProcess.size(); i++) {
            copy.add(new Note(soundProcess.get(i).getTick(), soundProcess.get(i).getKey(), soundProcess.get(i).getVelocity(), soundProcess.get(i).getBank(), (soundProcess.get(i).getMcTick() - soundProcess.get(i - 1).getMcTick()),soundProcess.get(i).getSample(),soundProcess.get(i).getFreq(),soundProcess.get(i).getChannel(),soundProcess.get(i).getTimeConv()));
        }
        return copy;
    }


//C, G, D, A, E, B (=C♭), F♯ (=G♭), C♯ (=D♭), A♭, E♭, B♭, F


    private static ArrayList<Note> quickSort (ArrayList < Note > soundProcess)
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

        lesser = quickSort(lesser);
        greater = quickSort(greater);

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
                    // swap arr[j+1] and arr[j]
                    Collections.swap(soundProcess,j+1,j);
                }
        return soundProcess;
    }

}



