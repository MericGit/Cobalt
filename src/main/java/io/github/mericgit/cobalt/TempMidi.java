package io.github.mericgit.cobalt;


import java.io.File;

public class TempMidi {

    public static void main(String[] args) throws Exception {
        //ResourcePackBuilder.generateRPack("C:\\Users\\dongd\\Downloads\\titan");
        //ResourcePackBuilder.generateSongJson("/Users/lawrence.zhang/Documents/MC Server/plugins/Cobalt/samplepool");
        ResourcePackBuilder.registerFiles("/Users/lawrence.zhang/Documents/MC Server/plugins/Cobalt/samplepool/songTest.json");
        Note.RRPoolToString();
        System.out.println("AMOOGSUS");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        Note.setTargetSample("genshin_piano");
        MidiUtils.midiToNoteSequence(new File("/Users/lawrence.zhang/Documents/MC Server/plugins/Cobalt/songs/" + "clair.mid"));
        TestEngine.playSoundProcess(MidiUtils.getFinalProcess());
    }
}

