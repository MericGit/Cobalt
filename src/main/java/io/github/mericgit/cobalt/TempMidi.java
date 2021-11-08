package io.github.mericgit.cobalt;

import io.github.mericgit.cobalt.Engine;
import io.github.mericgit.cobalt.MidiUtils;
import io.github.mericgit.cobalt.Note;
import io.github.mericgit.cobalt.TestEngine;

import java.io.File;

public class TempMidi {

    public static void main(String[] args) throws Exception {
        System.out.println("AMOOGSUS");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        Note.setTargetSample("splendor_a");
        //MidiUtils.midiToNoteSequence(new File("C:\\Users\\dongd\\Documents\\MC Server\\plugins\\Cobalt\\songs\\" +"wedding.mid"));
        MidiUtils.midiToNoteSequence(new File("/Users/lawrence.zhang/Documents/MC Server/plugins/Cobalt/songs/" +"clair.mid"));

        TestEngine.playSoundProcess(MidiUtils.getFinalProcess());

    }
}

