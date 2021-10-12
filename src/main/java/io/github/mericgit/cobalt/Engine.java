package io.github.mericgit.cobalt;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class Engine {

    private static int channel = 0; // 0 is a piano, 9 is percussion, other channels are for other instruments
    private static int duration = 200; // in milliseconds


    public static void playSoundProcess(ArrayList<Note> soundProcess) {
        final int[] i = {0};
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Current i is: " + i[0]);
                playSound(soundProcess.get(i[0]).getKey(),soundProcess.get(i[0]).getVelocity());
                i[0]++;
                System.out.println("played");
            }
        }, 1, TimeUnit.MILLISECONDS);
    }

    public static void playSound(int note, int velocity) {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel[] channels = synth.getChannels();
            channels[channel].noteOn(note, velocity); // C note
            channels[channel].allNotesOff();
            synth.close();
            System.out.println("Thread stop!");
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}