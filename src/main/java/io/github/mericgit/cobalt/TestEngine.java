package io.github.mericgit.cobalt;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class TestEngine {

    private static final MidiChannel[] channels;
    private static Synthesizer synth;
    private static final MidiChannel midiChannel;
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


        //File file = new File("/Users/lawrence.zhang/Downloads/Sonatina_Symphonic_Orchestra.sf2");

        currentInstrument = synth.getAvailableInstruments()[0];
        long latency = synth.getLatency();
        System.out.println("latency is: " + latency);
        //synth.loadAllInstruments(synth.getDefaultSoundbank());
        midiChannel.programChange(currentInstrument.getPatch().getBank(), currentInstrument.getPatch().getProgram());
    }

    public static void playSoundProcess(ArrayList<Note> soundProcess) throws MidiUnavailableException {
        System.out.println("REAL FINAL ");
        //System.out.println(soundProcess);
        Mapper.updateRRTimeConv(MidiUtils.getTempo(),MidiUtils.getPPQ());
        System.out.println("TEMPO: " + MidiUtils.getTempo());
        System.out.println("PPQ" + MidiUtils.getPPQ());
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (MidiUtils.getFinalProcess().size() > 1  )
                    playSound(MidiUtils.getFinalProcess());
                else {
                    executor.shutdown();
                    System.out.print("TERMINATED THREAD");
                }
            }
        }, 1000,1, TimeUnit.MILLISECONDS);
    }
    private static void playSound(ArrayList<Note> soundProcess) {
        try {
            if (soundProcess.get(0).getDataF1() == 2) {
                Mapper.updateInstrMap(soundProcess.get(0));
                //System.out.println("Updated instruments for Bank: " + soundProcess.get(0).getBank() + " ID: " + Mapper.getMidiInstrMap().get(soundProcess.get(0).getBank()) + " " + Mapper.gmMapper(Mapper.getMidiInstrMap().get(soundProcess.get(0).getBank())));
            }
            if (soundProcess.get(0).getMcTick() <= 0) {
                if(soundProcess.get(0).getVelocity() != 0 && soundProcess.get(0).getDataF1() == 0) {
                    if (Mapper.getMidiInstrMap().get(soundProcess.get(0).getBank()) != null) {
                        currentInstrument = synth.getAvailableInstruments()[Mapper.getMidiInstrMap().get(soundProcess.get(0).getBank())];

                    }
                    else {
                        //System.out.println("Amoogus moment: Missing Instrument at track ID: " + soundProcess.get(0).getBank());
                        currentInstrument = synth.getAvailableInstruments()[0];
                        //System.out.println("No registered INSTR for TRACK: " + soundProcess.get(0).getBank() + "Instr: " + Mapper.gmMapper(soundProcess.get(0).getKey()));
                    }
                    int bank = currentInstrument.getPatch().getBank(), program = currentInstrument.getPatch().getProgram();
                    synth.loadInstrument(currentInstrument);
                    program |= (bank&1)<<7; bank >>>= 1; // correction:
                    channels[soundProcess.get(0).getChannel()].programChange(bank, program);
                    channels[soundProcess.get(0).getChannel()].noteOn(soundProcess.get(0).getKey(),soundProcess.get(0).getVelocity());
                    //System.out.print(soundProcess.get(0));
                }
                else if (soundProcess.get(0).getVelocity() == 0) {
                    channels[soundProcess.get(0).getChannel()].noteOff(soundProcess.get(0).getKey(),soundProcess.get(0).getVelocity());
                }
                soundProcess.remove(0);
            }
            soundProcess.get(0).setMcTick(soundProcess.get(0).getMcTick()-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

