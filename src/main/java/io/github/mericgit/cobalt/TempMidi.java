package io.github.mericgit.cobalt;


import java.io.File;

public class TempMidi {

    public static void main(String[] args) throws Exception {
        //ResourcePackBuilder.generateRPack("C:\Users\dongd\Documents\MC Server\plugins\Cobalt\Resources\songTest.json");
        //ResourcePackBuilder.generateSongJson("C:\\Users\\dongd\\Downloads\\titan");
        ResourceHandler.registerFiles("C:\\Users\\dongd\\Documents\\MC Server\\plugins\\Cobalt\\Resources\\songTest.json");
        ResourceHandler.registerAllPool();
        //Mapper.RRPoolToString();
        System.out.println("AMOOGSUS");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        Note.setTargetSample("titan_s");
        MidiUtils.midiToNoteSequence(new File("C:\\Users\\dongd\\Documents\\MC Server\\plugins\\Cobalt\\songs\\" + "asweetsmile.mid"));
        Mapper.initInstrMap(MidiUtils.getFinalProcess());
        TestEngine.playSoundProcess(MidiUtils.getFinalProcess());
        System.out.println(MidiUtils.getFinalProcess());
    }
}

