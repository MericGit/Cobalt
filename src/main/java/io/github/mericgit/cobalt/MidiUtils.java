package io.github.mericgit.cobalt;

import javax.sound.midi.*;
import java.io.File;
import java.util.ArrayList;

public class MidiUtils {
    private static final int NOTE_ON = 0x90;
    private static final int NOTE_OFF = 0x80;
    private static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};


    public static ArrayList<Note> midiToNoteSequence(String file) throws Exception {
        Sequence sequence = MidiSystem.getSequence(new File("C:\\Users\\dongd\\OneDrive\\Documents\\GitHub\\MidiTesting\\src\\" + file));
        ArrayList<Note> noteSequence = new ArrayList<Note>();
        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON && sm.getData2() != 0) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        long tick = event.getTick();
                        int bank = 1;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity + " at ΔTick " + tick);
                        noteSequence.add(new Note(tick,key,velocity,bank));
                    } else if (sm.getCommand() == NOTE_OFF || sm.getData2() == 0) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        long tick = event.getTick();
                        int bank = 1;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note off, " + noteName + octave + " key=" + key + " at ΔTick " + tick);
                        noteSequence.add(new Note(tick,key,velocity,bank));
                    } else {
                        System.out.println("Command:" + sm.getCommand());
                    }
                } else {
                    System.out.println("Other message: " + message.getClass());
                }
            }

            System.out.println();
        }
        System.out.println(noteSequence);
        return noteSequence;
    }


    public static void midiLiveParse(String file) throws Exception {
        Sequence sequence = MidiSystem.getSequence(new File("C:\\Users\\dongd\\OneDrive\\Documents\\GitHub\\MidiTesting\\src\\" + file));
        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON && sm.getData2() != 0) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        long tick = event.getTick();
                        int bank = 1;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity + " at ΔTick " + tick);
                    } else if (sm.getCommand() == NOTE_OFF || sm.getData2() == 0) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        long tick = event.getTick();
                        int bank = 1;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note off, " + noteName + octave + " key=" + key + " at ΔTick " + tick);
                    } else {
                        System.out.println("Command:" + sm.getCommand());
                    }
                } else {
                    System.out.println("Other message: " + message.getClass());
                }
            }

            System.out.println();
        }
    }
}
