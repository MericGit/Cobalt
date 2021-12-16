package io.github.mericgit.cobalt;


import java.io.File;

public class TempMidi {

    public static void main(String[] args) throws Exception {
        //ResourcePackBuilder.generateRPack("C:\\Users\\dongd\\Downloads\\titan");
        //ResourcePackBuilder.generateSongJson("C:\\Users\\dongd\\Downloads\\titan");
        ResourceHandler.registerFiles("/Users/lawrence.zhang/Downloads/songTest.json");
        ResourceHandler.registerAllPool();
        //Mapper.RRPoolToString();
        System.out.println("AMOOGSUS");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        Note.setTargetSample("titan_s");
        MidiUtils.midiToNoteSequence(new File("/Users/lawrence.zhang/Documents/MC Server/plugins/Cobalt/songs/" + "highc.mid"));
        Mapper.initInstrMap(MidiUtils.getFinalProcess());
        TestEngine.playSoundProcess(MidiUtils.getFinalProcess());
        System.out.println(MidiUtils.getFinalProcess());
    }
}

