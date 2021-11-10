package io.github.mericgit.cobalt;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Note {

    private long tick;
    private int key;
    private int velocity;
    private int bank;
    private long mcTick;
    private String sample;
    private float freq;
    private int channel;
    private static float timeConv;
    private static String targetSample;
    private float dataF1;
    private static HashMap<String, int[]> rrPool =new HashMap<String, int[]>();


    public static void updateRRTimeConv(float tempo, int PPQ) {
        //System.out.print("RR TIME CONV WAS UPDATED! OLD: " + timeConv);
        timeConv = ((float) 60000 / (tempo * PPQ));
        //System.out.println("| NEW + " + timeConv);

    }

    public static void initPool() {
        for (int i = 1; i < 8; i++) {
            int[] temp = new int[]{1,0};
            rrPool.put("block.note_block." + targetSample + "_" + i, temp);
        }
        System.out.println("LOADING POOL");
        rrPool.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " +Arrays.toString(entry.getValue()));
        });
    }


    public static int rrPoolInterface(String sample, long tick) {
        int[] temp = rrPool.get(sample);
        //System.out.println("SAMPLE PASSED IN IS: ");
        //System.out.println("DIFF IS: " + String.valueOf((tick * timeConv) - (temp[1] * timeConv)));
        //System.out.println("timeConv is: " + timeConv);
        if (temp[0] > 14 || (tick * timeConv) - (temp[1] * timeConv) > 1000) {
            temp[0] = 1;
        }
        else {
            temp[0] = temp[0] + 1;
        }
        temp[1] = (int) tick;
        rrPool.put(sample,temp);
        /*
        System.out.print("TIMECONV " + timeConv + " | ");

        rrPool.entrySet().forEach(entry -> {
            System.out.print("SAMPLE: " + entry.getKey().substring(28) + " | " +Arrays.toString(entry.getValue()) + " | ");
        });
        System.out.println();
         */
        return rrPool.get(sample)[0];

    }

    public static ArrayList<Note> calcRR(ArrayList<Note> soundProcess) {
        for (int i =0; i < soundProcess.size(); i++) {
            if (soundProcess.get(i).getDataF1() == 0 && soundProcess.get(i).getVelocity() != 0) {
                String eventName = Note.advSample2(soundProcess.get(i)) + "_" + Note.rrPoolInterface(Note.advSample2(soundProcess.get(i)), soundProcess.get(i).getTick());
                soundProcess.get(i).setSample(eventName);
                for (int j = i; j < soundProcess.size(); j++) {
                    if (soundProcess.get(j).getKey() == soundProcess.get(i).getKey() && soundProcess.get(j).getVelocity() == 0) {
                        soundProcess.get(j).setSample(eventName);
                        break;
                    }
                }
            }
            else if (soundProcess.get(i).getDataF1() == 1) {
                Note.updateRRTimeConv(soundProcess.get(i).getFreq(),MidiUtils.getPPQ());
            }

        }
        return soundProcess;
    }


    public static String advSample2(Note note) {

        String temp = "block.note_block." +getTargetSample() + "_";
        if (note.getKey() <= 36) {
            return temp + "1";
        }
        else if (note.getKey() <= 48) {
            return  temp + "2";
        }
        else if (note.getKey() <= 60) {
            return  temp + "3";
        }
        else if (note.getKey() <= 72) {
            return  temp + "4";
        }
        else if (note.getKey() <= 84) {
            return  temp + "5";
        }
        else if (note.getKey() <= 96) {
            return  temp + "6";
        }
        else if (note.getKey() <= 108) {
            return  temp + "7";
        }
        return "null";
    }


    public static float advFreq(Note note) {
        int pitch = (note.getKey() % 12 - 6);
        //System.out.println("Final pitch: " + pitch);
        //System.out.println("Resulting pitch math: " + Float.toString((float) Math.pow(2,((double) pitch / 12))));
            return (float) Math.pow(2,((double) pitch / 12));
        }

        public Note(long tick, int key, int velocity, int bank, long mcTick, String sample, float freq, int channel,float dataF1) {
        this.tick = tick;
        this.mcTick = mcTick;
        this.key = key;
        this.velocity = velocity;
        this.bank = bank;
        this.sample = sample;
        this.freq = freq;
        this.channel = channel;
        this.dataF1 = dataF1;
    }

    @Override
    public String toString() {
        return "Note{" +
                "tick=" + tick +
                ", key=" + key +
                ", velocity=" + velocity +
                ", bank=" + bank +
                ", mcTick=" + mcTick +
                ", sample='" + sample + '\'' +
                ", freq=" + freq +
                ", channel=" + channel +
                ", dataF1=" + dataF1 +
                '}' + "\n";
    }


    public static String getTargetSample() {
        return targetSample;
    }

    public static void setTargetSample(String t) {
        targetSample = t;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public long getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public long getMcTick() {
        return mcTick;
    }

    public void setMcTick(long mcTick) {
        this.mcTick = mcTick;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public float getFreq() {
        return freq;
    }
    public float getDataF1() {
        return dataF1;
    }

    public void setDataF1(int dataF1) {
        this.dataF1 = dataF1;
    }

    public void setFreq(float freq) {
        this.freq = freq;
    }

}


