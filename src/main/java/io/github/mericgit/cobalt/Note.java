package io.github.mericgit.cobalt;

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
    private float duration;
    private static final HashMap<String, int[]> rrPool =new HashMap<String, int[]>();

    public Note(long tick, int key, int velocity, int bank, long mcTick, String sample, float freq, int channel,float duration, float dataF1) {
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
    public static void updateRRTimeConv(float tempo, int PPQ) {
        //System.out.print("RR TIME CONV WAS UPDATED! OLD: " + timeConv);
        timeConv = ((float) 60000 / (tempo * PPQ));
        //System.out.println("| NEW + " + timeConv);
    }

    public static void updatePool(String sample, int[] data) {
        rrPool.put(sample, data);
    }
    public static HashMap<String, int[]> getRRPool() {
        return rrPool;
    }
    public static void RRPoolToString() {
        rrPool.entrySet().forEach(entry -> {
            System.out.println("SAMPLE: " + entry.getKey() + " | " +Arrays.toString(entry.getValue()) + " | ");
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
                        soundProcess.get(i).setDuration((soundProcess.get(i).getTick() * timeConv) - (soundProcess.get(j).getTick() * timeConv));
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
        if (note.getKey() <= 24) {
            return temp + "01";
        }
        else if (note.getKey() <= 31) {
            return  temp + "02";
        }
        else if (note.getKey() <= 38) {
            return  temp + "03";
        }
        else if (note.getKey() <= 45) {
            return  temp + "04";
        }
        else if (note.getKey() <= 52) {
            return  temp + "05";
        }
        else if (note.getKey() <= 59) {
            return  temp + "06";
        }
        else if (note.getKey() <= 66) {
            return  temp + "07";
        }
        else if (note.getKey() <= 73) {
            return  temp + "08";
        }
        else if (note.getKey() <= 80) {
            return  temp + "09";
        }
        else if (note.getKey() <= 87) {
            return  temp + "10";
        }
        else if (note.getKey() <= 94) {
            return  temp + "11";
        }
        else if (note.getKey() <= 101) {
            return  temp + "12";
        }
        return temp + "01";
    }

    public static float advFreq(Note note) {
        int interval = 0;
        for (int i = 0; i < 12; i++) {
            if (note.getKey() - (24 + 7*i) <= 0)
                interval +=i;
        }
        int root = 24 + interval * 7;
        return (float) Math.pow(2,((double) (-1 * (root - note.getKey())) / 12));
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

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setDataF1(int dataF1) {
        this.dataF1 = dataF1;
    }

    public void setFreq(float freq) {
        this.freq = freq;
    }
}


