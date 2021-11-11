package io.github.mericgit.cobalt;


import java.io.File;

public class TempMidi {

    public static void main(String[] args) throws Exception {
        ResourcePackBuilder.generateRPack("C:\\Users\\dongd\\Downloads\\titan");
        //ResourcePackBuilder.generateSongJson("C:\\Users\\dongd\\Downloads\\titan");
        ResourcePackBuilder.registerFiles("C:\\Users\\dongd\\Downloads\\titan\\songTest.json");
        Note.RRPoolToString();
        System.out.println("AMOOGSUS");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        Note.setTargetSample("titan_s");
        MidiUtils.midiToNoteSequence(new File("C:\\Users\\dongd\\Documents\\MC Server\\plugins\\Cobalt\\songs\\" +"clair.mid"));
        //MidiUtils.midiToNoteSequence(new File("/Users/lawrence.zhang/Documents/MC Server/plugins/Cobalt/songs/" +"clair.mid"));

        TestEngine.playSoundProcess(MidiUtils.getFinalProcess());

    }
}

