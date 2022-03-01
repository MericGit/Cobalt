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
    public static String percMap(Note note) {
        String temp = "block.note_block.";
        return temp + switch (note.getKey()) {
            case 35 -> "acoustic_bass_drum";
            case 36 -> "bass_drum_1";
            case 37 -> "side_stick";
            case 38 -> "acoustic_snare";
            case 39 -> "hand_clap";
            case 40 -> "electric_snare";
            case 41 -> "low_floor_tom";
            case 42 -> "closed_hi_hat";
            case 43 -> "high_floor_tom";
            case 44 -> "pedal_hi_hat";
            case 45 -> "low_tom";
            case 46 -> "open_hi_hat";
            case 47 -> "low_mid_tom";
            case 48 -> "hi_mid_tom";
            case 49 -> "crash_cymbal_1";
            case 50 -> "high_tom";
            case 51 -> "ride_cymbal_1";
            case 52 -> "chinese_cymbal";
            case 53 -> "ride_bell";
            case 54 -> "tambourine";
            case 55 -> "splash_cymbal";
            case 56 -> "cowbell";
            case 57 -> "crash_cymbal_2";
            case 58 -> "vibraslap";
            case 59 -> "ride_cymbal_2";
            case 60 -> "high_bongo";
            case 61 -> "low_bongo";
            case 62 -> "mute_high_conga";
            case 63 -> "open_high_conga";
            case 64 -> "low_conga";
            case 65 -> "high_timbale";
            case 66 -> "low_timbale";
            case 67 -> "high_agogo";
            case 68 -> "low_agogo";
            case 69 -> "cabasa";
            case 70 -> "maracas";
            case 71 -> "short_whistle";
            case 72 -> "long_whistle";
            case 73 -> "short_guiro";
            case 74 -> "long_guiro";
            case 75 -> "claves";
            case 76 -> "high_wood_block";
            case 77 -> "low_wood_block";
            case 78 -> "mute_cuica";
            case 79 -> "open_cuica";
            case 80 -> "mute_triangle";
            case 81 -> "open_triangle";
            default -> "unexpected";
        };
    }
    public static String advSample2(Note note) {
        String temp = "block.note_block." + note.getSample() + "_";
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
        else if (note.getKey() <=108) {
            return temp + "13";
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

    public static ArrayList<Note> calcArtic(ArrayList<Note> soundProcess) {
        for (int i = 0; i < soundProcess.size(); i++) {
            float duration = soundProcess.get(i).getDuration();
            if (duration < 220 && soundProcess.get(i).getDataF1() == 0 && soundProcess.get(i).getVelocity() != 0) {
                soundProcess.get(i).setSample(soundProcess.get(i).getSample().substring(0,soundProcess.get(i).getSample().lastIndexOf("_")).replaceFirst("_sus_","_stac_"));
            }
        }
        return soundProcess;
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


