package io.github.mericgit.cobalt;

public class Note {

    private long tick;
    private int key;
    private int velocity;
    private int bank;
    private long mcTick;
    private int track;

    public Note(long tick, int key, int velocity, int bank, long mcTick) {
        this.tick = tick;
        this.mcTick = mcTick;
        this.key = key;
        this.velocity = velocity;
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "Note{" +
                "tick=" + tick +
                ", key=" + key +
                ", velocity=" + velocity +
                ", bank=" + bank +
                ", mcTick=" + mcTick +
                '}' + "\n";
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
}


