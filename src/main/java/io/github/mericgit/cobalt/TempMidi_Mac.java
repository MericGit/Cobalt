package io.github.mericgit.cobalt;


import java.io.File;

public class TempMidi_Mac {

    public static void main(String[] args) throws Exception {
        //ResourceHandler.generateRPack("/Users/lawrence.zhang/Library/Application Support/minecraft/resourcepacks/Cobalt-Lunar/assets/minecraft/sounds/block/note_block");
        //ResourceHandler.generateSongJson("/Users/lawrence.zhang/Library/Application Support/minecraft/resourcepacks/Cobalt-Lunar/assets/minecraft/sounds/block/note_block");
        ResourceHandler.registerFiles("/Users/lawrence.zhang/Documents/MC Server/plugins/Cobalt/Resources/songTest.json");
        ResourceHandler.registerAllPool();
        //Mapper.RRPoolToString();
        System.out.println("AMOOGSUS");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        Note.setTargetSample("titan_s");
        MidiUtils.midiToNoteSequence(new File("/Users/lawrence.zhang/Documents/MC Server/plugins/Cobalt/Songs/" + "xenoblade.mid"));
        Mapper.initInstrMap(MidiUtils.getFinalProcess());
        TestEngine.playSoundProcess(MidiUtils.getFinalProcess());
        System.out.println(MidiUtils.getFinalProcess());
    }
}

