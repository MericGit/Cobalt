package io.github.mericgit.cobalt;

public class Note {

    private long tick;
    private int key;
    private int velocity;
    private int bank;

    public Note(long tick, int key, int velocity, int bank) {
        this.tick = tick;
        this.key = key;
        this.velocity = velocity;
        this.bank = bank;
    }
    public String toString() {
        return "Note{" +
                "tick=" + tick +
                ", key=" + key +
                ", velocity=" + velocity +
                ", bank=" + bank +
                '}';
    }
}


