package io.github.mericgit.cobalt;

import java.io.File;

public class TempMidi {

    public static void main(String[] args) throws Exception {
        System.out.println("AMOOGSUS");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        MidiUtils.midiToNoteSequence("run_lads.mid");
        Engine.playSoundProcess(MidiUtils.getFinalProcess());

    }
}

