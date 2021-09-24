package io.github.mericgit.cobalt;

import java.io.File;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class TempMidi {

    public static void main(String[] args) throws Exception {
        MidiUtils.midiToNoteSequence("test3.mid");

    }
}


