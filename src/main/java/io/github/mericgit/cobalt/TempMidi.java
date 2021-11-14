package io.github.mericgit.cobalt;


import java.io.File;

public class TempMidi {

    public static void main(String[] args) throws Exception {
        //ResourcePackBuilder.generateRPack("C:\\Users\\dongd\\Downloads\\titan");
        //ResourcePackBuilder.generateSongJson("C:\\Users\\dongd\\Downloads\\titan");
        ResourceHandler.registerFiles("C:\\Users\\dongd\\Downloads\\Temp\\songTest.json");
        Note.RRPoolToString();
        System.out.println("AMOOGSUS");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        Note.setTargetSample("titan_s");
        MidiUtils.midiToNoteSequence(new File("C:\\Users\\dongd\\Documents\\MC Server\\plugins\\Cobalt\\songs\\" +"aquarium.mid"));
        TestEngine.playSoundProcess(MidiUtils.getFinalProcess());
    }
}

