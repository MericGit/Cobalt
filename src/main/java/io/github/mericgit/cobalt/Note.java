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
        else if (note.getKey() <= 127) {
            return  temp + "12";
        }
        return temp + "01";
    }

    public static float advFreq(Note note) {
        int interval = 0;
        for (int i = 0; i < 12; i++) {
            if (note.getKey() - (24 + 7*i) <= 0) {
                interval = i;
                break;
            }
        }
        int root = 24 + interval * 7;
        return (float) Math.pow(2,((double) (-1 * (root - note.getKey())) / 12));
    }

    public static String calcArtic(Note note) {
        float duration = note.getDuration();
        if (duration <= 250 && note.getBank() > 5) {
            return "_s";
        } else {
            return "_a";
        }
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
                ", duration=" + duration +
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


