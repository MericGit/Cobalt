package io.github.mericgit.cobalt;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class TestEngine {
    private static MidiChannel[] channels;
    private static Synthesizer synth;
    private static MidiChannel midiChannel;
    private static Instrument currentInstrument;


    static {
        try {
            synth = MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
        try {
            synth.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
        channels = synth.getChannels();
        midiChannel = synth.getChannels()[0];
        currentInstrument = synth.getAvailableInstruments()[0];
        System.out.println("Switching instrument to #" + 4 + ": " + currentInstrument.getName());
        synth.loadInstrument(currentInstrument);
        midiChannel.programChange(currentInstrument.getPatch().getBank(), currentInstrument.getPatch().getProgram());
    }

    private static int channel = 0; // 0 is a piano, 9 is percussion, other channels are for other instruments
    private static int duration = 200; // in milliseconds


    public static void playSoundProcess(ArrayList<Note> soundProcess) throws MidiUnavailableException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (MidiUtils.getFinalProcess().size() > 1  )
                    playSound(MidiUtils.getFinalProcess());
            }
        }, 1000,1, TimeUnit.MILLISECONDS);
    }

    private static void playSound(ArrayList<Note> soundProcess) {
        try {

            if (soundProcess.get(0).getDataF1() == 1) {
                currentInstrument = synth.getAvailableInstruments()[soundProcess.get(0).getKey()];
                System.out.println("Switching instrument to #" + soundProcess.get(0).getKey() + ": " + currentInstrument.getName());
                synth.loadInstrument(currentInstrument);
                midiChannel.programChange(currentInstrument.getPatch().getBank(), currentInstrument.getPatch().getProgram());
            }
            if (soundProcess.get(0).getMcTick() <= 0) {
                //System.out.println("CURRENT: " + soundProcess.get(0));
                if(soundProcess.get(0).getVelocity() != 0 || soundProcess.get(0).getVelocity() != 128) {
                    channels[soundProcess.get(0).getChannel()].noteOn(soundProcess.get(0).getKey(),soundProcess.get(0).getVelocity());
                    //System.out.println("Current INSTR: " + currentInstrument.getName());
                }
                else if (soundProcess.get(0).getVelocity() == 0) {
                    channels[soundProcess.get(0).getChannel()].noteOff(soundProcess.get(0).getKey(),soundProcess.get(0).getVelocity());
                }
                soundProcess.remove(0);
            }
            soundProcess.get(0).setMcTick(soundProcess.get(0).getMcTick()-1);
            //System.out.println("MIDI Ticks: " + soundProcess.get(0).getTick());
            //System.out.println("Mc Ticks: " +  soundProcess.get(0).getMcTick());

            //System.out.println("Thread stop!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

