
import io.github.mericgit.cobalt.Engine;
import io.github.mericgit.cobalt.MidiUtils;

import java.io.File;

public class TempMidi {

    public static void main(String[] args) throws Exception {
        System.out.println("AMOOGSUS");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        MidiUtils.midiToNoteSequence("run_lads.mid");
        TestEngine.playSoundProcess(MidiUtils.getFinalProcess());

    }
}


