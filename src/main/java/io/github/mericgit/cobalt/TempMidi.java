package io.github.mericgit.cobalt;


import java.io.File;

public class TempMidi {

    public static void main(String[] args) throws Exception {
        //ResourceHandler.generateRPack("C:\\Users\\dongd\\AppData\\Roaming\\.minecraft\\resourcepacks\\Cobalt-Lunar\\assets\\minecraft\\sounds\\block\\note_block");
        //ResourceHandler.generateSongJson("C:\\Users\\dongd\\AppData\\Roaming\\.minecraft\\resourcepacks\\Cobalt-Lunar\\assets\\minecraft\\sounds\\block\\note_block");
        ResourceHandler.registerFiles("C:\\Users\\dongd\\Documents\\MC Server\\plugins\\Cobalt\\Resources\\songTest.json");
        ResourceHandler.registerAllPool();
        //Mapper.RRPoolToString();
        System.out.println("AMOOGSUS");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        Note.setTargetSample("titan_s");
        MidiUtils.midiToNoteSequence(new File("C:\\Users\\dongd\\Documents\\MC Server\\plugins\\Cobalt\\songs\\" + "torment.mid"));
        Mapper.initInstrMap(MidiUtils.getFinalProcess());
        TestEngine.playSoundProcess(MidiUtils.getFinalProcess());
        System.out.println(MidiUtils.getFinalProcess());
    }
}

