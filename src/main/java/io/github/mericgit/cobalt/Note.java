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

    public static float getFreq(Note note) {
        return switch (note.getKey()) {
            case 0 -> 0.50000F;
            case 1 -> 0.52973F;
            case 2 -> 0.56123F;
            case 3 -> 0.59461F;
            case 4 -> 0.62995F;
            case 5 -> 0.66741F;
            case 6 -> 0.70711F;
            case 7 -> 0.74916F;
            case 8 -> 0.79370F;
            case 9 -> 0.84089F;
            case 10 -> 0.89091F;
            case 11 -> 0.94386F;
            case 12 -> 1.00000F;
            case 13 -> 1.05945F;
            case 14 -> 1.12245F;
            case 15 -> 1.18920F;
            case 16 -> 1.25993F;
            case 17 -> 1.33484F;
            case 18 -> 1.41420F;
            case 19 -> 1.49832F;
            case 20 -> 1.58741F;
            case 21 -> 1.68180F;
            case 22 -> 1.78180F;
            case 23 -> 1.88775F;
            case 24 -> 2.00000F;
            default -> 0F;
        };
    }


    public Note(long tick, int key, int velocity, int bank, long mcTick, String sample, float freq, int channel) {
        this.tick = tick;
        this.mcTick = mcTick;
        this.key = key;
        this.velocity = velocity;
        this.bank = bank;
        this.sample = sample;
        this.freq = freq;
        this.channel = channel;
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

    public void setFreq(float freq) {
        this.freq = freq;
    }
}


