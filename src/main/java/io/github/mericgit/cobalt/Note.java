package io.github.mericgit.cobalt;

import org.bukkit.Bukkit;

public class Note {

    private long tick;
    private int key;
    private int velocity;
    private int bank;
    private long mcTick;
    private String sample;
    private float freq;
    private int channel;
    private double timeConv;
    private static String targetSample;
    private float dataF1;

    private static String S1 = "Sample";
    private static String S2 = "Sample";
    private static String S3 = "Sample";
    private static String S4 = "Sample";  // Lower 3 ( 1st Octave)
    private static String S5 = "Sample";
    private static String S6 = "Sample";
    private static String S7 = "Sample";  // Genshin Piano
    private static String S8 = "Sample";
    private static String S9 = "Sample";
    private static String S10 = "Sample"; // Boost 3 (7th Octave)
    private static String S11 = "Sample";
    private static String S12 = "Sample";
    private static String S13 = "Sample";

    public static String advSample(Note note) {
        return switch (note.getKey()) {
            case 0 -> "Undf"; // Extra Range Capabilities?
            case 1,2,3,4,5,6,7 -> S1; //4 Is CORE
            case 8 -> S2;
            case 9 -> S2;
            case 10 -> S2;
            case 11 -> S2; // CORE sample 2 G1
            case 12 -> S2;
            case 13 -> S2;
            case 14 -> S2;
            case 15 -> S3;
            case 16 -> S3;
            case 17 -> S3;
            case 18 -> S3; // CORE sample 3 D2
            case 19 -> S3;
            case 20 -> S3;
            case 21 -> S3;
            case 22 -> S4;
            case 23 -> S4;
            case 24 -> S4;
            case 25 -> S4; // CORE sample 4 A2
            case 26 -> S4;
            case 27 -> S4;
            case 28 -> S4;
            case 29 -> S5;
            case 30 -> S5;
            case 31 -> S5;
            case 32 -> S5; // CORE sample 5 E3
            case 33 -> S5;
            case 34 -> S5;
            case 35 -> S5;
            case 36 -> S6;
            case 37 -> S6;
            case 38 -> S6;
            case 39 -> S6; // CORE sample 6 B3
            case 40 -> S6;
            case 41 -> S6;
            case 42 -> S6;
            case 43 -> S7;
            case 44 -> S7;
            case 45 -> S7;
            case 46 -> S7; // CORE sample 7 F#4
            case 47 -> S7;
            case 48 -> S7;
            case 49 -> S7;
            case 50 -> S8;
            case 51 -> S8;
            case 52 -> S8;
            case 53 -> S8; // CORE sample 8 C#5
            case 54 -> S8;
            case 55 -> S8;
            case 56 -> S8;
            case 57 -> S9;
            case 58 -> S9;
            case 59 -> S9;
            case 60 -> S9; // CORE sample 9 G#5
            case 61 -> S9;
            case 62 -> S9;
            case 63 -> S9;
            case 64 -> S10;
            case 65 -> S10;
            case 66 -> S10;
            case 67 -> S10; // CORE sample 10 D#6
            case 68 -> S10;
            case 69 -> S10;
            case 70 -> S10;
            case 71 -> S11;
            case 72 -> S11;
            case 73 -> S11;
            case 74 -> S11;  // CORE sample 11 A#6
            case 75 -> S11;
            case 76 -> S11;
            case 77 -> S11;
            case 78 -> S12;
            case 79 -> S12;
            case 80 -> S12;
            case 81 -> S12; // CORE sample 12 F7
            case 82 -> S12;
            case 83 -> S12;
            case 84 -> S12;
            case 85 -> S13;
            case 86 -> S13;
            case 87 -> S13;
            case 88 -> S13; // CORE sample 13 C8
            default -> throw new IllegalStateException("Unexpected value: " + note.getKey());
        };
    }

    public static String getTargetSample() {
        return targetSample;
    }

    public static void setTargetSample(String t) {
        targetSample = t;
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

//pitch = 2^((((note_key - 33) + (note_pitch / 100)) - 12) / 12)
    public Note(long tick, int key, int velocity, int bank, long mcTick, String sample, float freq, int channel,double timeConv,float dataF1) {
        this.tick = tick;
        this.mcTick = mcTick;
        this.key = key;
        this.velocity = velocity;
        this.bank = bank;
        this.sample = sample;
        this.freq = freq;
        this.channel = channel;
        this.timeConv = timeConv;
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
                ", timeConv=" + timeConv +
                ", dataF1=" + dataF1 +
                '}' + "\n";
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

    public void setTick(long tick) {
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

    public void setTimeConv(double timeConv) {
        this.timeConv = timeConv;
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
    public void setTimeConv(float timeConv) {
        this.timeConv = timeConv;
    }
    public double getTimeConv() {
        return timeConv;
    }
}


