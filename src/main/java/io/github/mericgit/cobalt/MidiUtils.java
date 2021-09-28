package io.github.mericgit.cobalt;

import javax.sound.midi.*;
import javax.sound.midi.spi.MidiFileReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MidiUtils {
    private static final int NOTE_ON = 0x90;
    private static final int NOTE_OFF = 0x80;
    private static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private static final int DEFAULT_TEMPO_MPQ = 500000; // 120bpm
    private static final int META_END_OF_TRACK_TYPE = 0x2F;
    private static final int META_TEMPO_TYPE = 0x51;

    public static ArrayList<Note> midiToNoteSequence(String file) throws Exception {
        Sequence sequence = MidiSystem.getSequence(new File("./src/main/java/io/github/mericgit/cobalt/" + file));
        MidiFileFormat midiFile = MidiSystem.getMidiFileFormat(new File("./src/main/java/io/github/mericgit/cobalt/" + file));

        // Use the sequencer interface to extract the incumbent tempo of the MIDI file
        Sequencer sequencer = MidiSystem.getSequencer(false);
        sequencer.setSequence(sequence);
        int oldTempo = (int) sequencer.getTempoInBPM();
        System.out.println("Old Tempo: " + oldTempo);
        int PPQ = midiFile.getResolution();
        System.out.println("PPQ IS: " + PPQ);

        double timeConverter = ((double) 60000 / (oldTempo * PPQ));
        System.out.println("Milliseconds per tick = " + timeConverter);


        ArrayList<Note> noteSequence = new ArrayList<Note>();
        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            //System.out.println("Track " + trackNumber + ": size = " + track.size());
            //System.out.println();
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof MetaMessage) {
                    System.out.println(message);
                }
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    //System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON && sm.getData2() != 0) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        long tick = event.getTick();
                        int bank = trackNumber;
                        long mcTick = Math.round((tick * timeConverter) / 50);
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        //System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity + " at ΔTick " + tick);
                        noteSequence.add(new Note(tick, key, velocity, bank, mcTick));
                    } else if (sm.getCommand() == NOTE_OFF || sm.getData2() == 0) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        long tick = event.getTick();
                        int bank = trackNumber;
                        long mcTick = Math.round((tick * timeConverter) / 50);
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        //System.out.println("Note off, " + noteName + octave + " key=" + key + " at ΔTick " + tick);
                        noteSequence.add(new Note(tick, key, velocity, bank, mcTick));
                    } else {
                        //System.out.println("Command:" + sm.getCommand());
                    }
                } else {
                    //System.out.println("Other message: " + message.getClass());
                }
            }

            System.out.println();
        }
        System.out.println("Original Sequence");
        System.out.println(noteSequence);
        System.out.println("-------------------------");
        System.out.println("ConvertNonDelta");
        System.out.println(convertNonDelta(noteSequence));
        System.out.println("-------------------------");
        System.out.println("Quicksort + nonDelta");
        System.out.println(convertNonDelta(quickSort(noteSequence)));
        return noteSequence;
    }




    public static ArrayList<Note> convertNonDelta(ArrayList<Note> soundProcess) {
        ArrayList<Note> copy = new ArrayList<>();
        copy.add(soundProcess.get(0));
        for (int i = 1; i < soundProcess.size(); i++) {
            copy.add(new Note(soundProcess.get(i).getTick(),soundProcess.get(i).getKey(),soundProcess.get(i).getVelocity(),soundProcess.get(i).getBank(),(soundProcess.get(i).getMcTick() - soundProcess.get(i-1).getMcTick())));
        }
        return copy;
    }


    public static ArrayList<Note> quickSort(ArrayList<Note> soundProcess)
    {
        if (soundProcess.size() <= 1)
            return soundProcess; // Already sorted

        ArrayList<Note> sorted = new ArrayList<Note>();
        ArrayList<Note> lesser = new ArrayList<Note>();
        ArrayList<Note> greater = new ArrayList<Note>();
        long pivot = soundProcess.get(soundProcess.size()-1).getTick(); // Use last Vehicle as pivot
        for (int i = 0; i < soundProcess.size()-1; i++)
        {
            //int order = list.get(i).compareTo(pivot);
            if (soundProcess.get(i).getTick() < pivot)
                lesser.add(soundProcess.get(i));
            else
                greater.add(soundProcess.get(i));
        }

        lesser = quickSort(lesser);
        greater = quickSort(greater);

        lesser.add(soundProcess.get(soundProcess.size()-1));
        lesser.addAll(greater);
        sorted = lesser;

        return sorted;
    }

}

