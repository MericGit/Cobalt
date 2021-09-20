package io.github.mericgit.cobalt;

import org.bukkit.Bukkit;

import javax.sound.midi.*;
import javax.sound.midi.spi.MidiFileReader;
import java.io.File;
import java.util.ArrayList;

public class MidiUtils {
    private static final int NOTE_ON = 0x90;
    private static final int NOTE_OFF = 0x80;
    private static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private static final int DEFAULT_TEMPO_MPQ = 500000; // 120bpm
    private static final int META_END_OF_TRACK_TYPE = 0x2F;
    private static final int META_TEMPO_TYPE = 0x51;
    public static float timeConverter = 0;



    public static ArrayList<Note> midiToNoteSequence(String file) throws Exception {
        Sequence sequence = MidiSystem.getSequence(new File(file));
        MidiFileFormat midiFile = MidiSystem.getMidiFileFormat(new File(file));

        // Use the sequencer interface to extract the incumbent tempo of the MIDI file
        Sequencer sequencer = MidiSystem.getSequencer(false);
        sequencer.setSequence(sequence);
        int oldTempo = (int) sequencer.getTempoInBPM();
        System.out.println("Old Tempo: " + oldTempo);
        int PPQ = midiFile.getResolution();

        timeConverter = (float) 60000 / (oldTempo * PPQ);
        System.out.println("Milliseconds per tick = " + timeConverter);

        System.out.println(PPQ);
        ArrayList<Note> noteSequence = new ArrayList<>();
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
                    //System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON && sm.getData2() != 0) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        long tick = event.getTick();
                        long mcTick =  Math.round((tick * getTimeConverter()) / 50);
                        int bank = 1;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        //System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity + " at ΔTick " + tick);
                        noteSequence.add(new Note(tick,key,velocity,bank,mcTick));
                    } else if (sm.getCommand() == NOTE_OFF || sm.getData2() == 0) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        long tick = event.getTick();
                        int bank = 1;
                        long mcTick =  Math.round((tick * getTimeConverter()) / 50);

                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        //System.out.println("Note off, " + noteName + octave + " key=" + key + " at ΔTick " + tick);
                        noteSequence.add(new Note(tick,key,velocity,bank,mcTick));
                    } else {
                        //System.out.println("Command:" + sm.getCommand());
                    }
                } else {
                    //System.out.println("Other message: " + message.getClass());
                }
            }

            System.out.println();
        }
        Bukkit.getServer().broadcastMessage(noteSequence.toString());
        return noteSequence;
    }

    public static float getTimeConverter() {
        return timeConverter;
    }


    public static ArrayList<Note> convertNonDelta(ArrayList<Note> soundProcess) {
        ArrayList<Note> copy = new ArrayList<>();
        copy.add(soundProcess.get(0));
        for (int i = 1; i < soundProcess.size(); i++) {
            copy.add(new Note(soundProcess.get(i).getTick(),soundProcess.get(i).getKey(),soundProcess.get(i).getVelocity(),soundProcess.get(i).getBank(),(soundProcess.get(i).getMcTick() - soundProcess.get(i-1).getMcTick())));
        }
        return copy;
    }
}
