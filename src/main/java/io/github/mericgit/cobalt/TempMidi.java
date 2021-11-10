package io.github.mericgit.cobalt;


import java.io.File;

public class TempMidi {

    public static void main(String[] args) throws Exception {
        System.out.println("AMOOGSUS");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        Note.setTargetSample("splendor_b");
        Note.initPool();
        ResourcePackBuilder.initResourcePool("block.note_block.splendor_b_","block/note_block/splendor_b_");
        ResourcePackBuilder.generateSoundsJson();
        //MidiUtils.midiToNoteSequence(new File("C:\\Users\\dongd\\Documents\\MC Server\\plugins\\Cobalt\\songs\\" +"clair.mid"));
        MidiUtils.midiToNoteSequence(new File("/Users/lawrence.zhang/Documents/MC Server/plugins/Cobalt/songs/" +"clair.mid"));

        TestEngine.playSoundProcess(MidiUtils.getFinalProcess());

    }
}

