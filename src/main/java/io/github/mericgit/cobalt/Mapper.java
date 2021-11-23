package io.github.mericgit.cobalt;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Mapper {
    private long tick;
    private int key;
    private int velocity;
    private int bank;
    private long mcTick;
    private String sample;
    private float freq;
    private int channel;
    private static float timeConv;
    private static String targetSample;
    private float dataF1;
    private float duration;
    private static final HashMap<String, int[]> rrPool =new HashMap<String, int[]>();
    private static final HashMap<Integer, String[]> instrumentPool = new HashMap<Integer, String[]>();
    private static HashMap<Integer, Integer> midiInstrMap = new HashMap<>();


    public static void updateRRTimeConv(float tempo, int PPQ) {
        //System.out.print("RR TIME CONV WAS UPDATED! OLD: " + timeConv);
        timeConv = ((float) 60000 / (tempo * PPQ));
        //System.out.println("| NEW + " + timeConv);
    }

    public static void updatePool(String sample, int[] data) {
        rrPool.put(sample, data);
    }
    public static HashMap<String, int[]> getRRPool() {
        return rrPool;
    }
    public static void RRPoolToString() {
        rrPool.entrySet().forEach(entry -> {
            System.out.println("SAMPLE: " + entry.getKey() + " | " + Arrays.toString(entry.getValue()) + " | ");
        });
    }
    public static int rrPoolInterface(String sample, long tick) {
        int[] temp = rrPool.get(sample);
        //System.out.println("SAMPLE PASSED IN IS: ");
        //System.out.println("DIFF IS: " + String.valueOf((tick * timeConv) - (temp[1] * timeConv)));
        //System.out.println("timeConv is: " + timeConv);
        if (temp[0] > 14 || (tick * timeConv) - (temp[1] * timeConv) > 1000) {
            temp[0] = 1;
        }
        else {
            temp[0] = temp[0] + 1;
        }
        temp[1] = (int) tick;
        rrPool.put(sample,temp);
        /*
        System.out.print("TIMECONV " + timeConv + " | ");

        rrPool.entrySet().forEach(entry -> {
            System.out.print("SAMPLE: " + entry.getKey().substring(28) + " | " +Arrays.toString(entry.getValue()) + " | ");
        });
        System.out.println();
         */
        return rrPool.get(sample)[0];
    }

    public static ArrayList<Note> calcRR(ArrayList<Note> soundProcess) {
        for (int i =0; i < soundProcess.size(); i++) {
            if (soundProcess.get(i).getDataF1() == 0 && soundProcess.get(i).getVelocity() != 0) {
                String eventName = Note.advSample2(soundProcess.get(i)) + "_" + Mapper.rrPoolInterface(Note.advSample2(soundProcess.get(i)), soundProcess.get(i).getTick());
                soundProcess.get(i).setSample(eventName);
                for (int j = i; j < soundProcess.size(); j++) {
                    if (soundProcess.get(j).getKey() == soundProcess.get(i).getKey() && soundProcess.get(j).getVelocity() == 0) {
                        soundProcess.get(j).setSample(eventName);
                        soundProcess.get(i).setDuration((soundProcess.get(j).getTick() * timeConv) - (soundProcess.get(i).getTick() * timeConv));
                        break;
                    }
                }
            }
            else if (soundProcess.get(i).getDataF1() == 1) {
                Mapper.updateRRTimeConv(soundProcess.get(i).getFreq(),MidiUtils.getPPQ());
            }
        }
        return soundProcess;
    }

    public static void initInstrMap(ArrayList<Note> soundProcess) {
        for (int i = soundProcess.size() - 1; i >= 0; i--) {
            if (soundProcess.get(i).getDataF1() == 2) {
                midiInstrMap.put(soundProcess.get(i).getBank(),soundProcess.get(i).getKey());
            }
        }
        midiInstrMap.entrySet().forEach(entry -> {
            System.out.println("Instr Bank: " + entry.getKey() + " ID: " + entry.getValue() + " " + gmMapper(entry.getValue()));
        });
    }

    public static void updateInstrMap(Note note) {
        midiInstrMap.put(note.getBank(),note.getKey());
    }

    public static HashMap<Integer, Integer> getMidiInstrMap() {
        return midiInstrMap;
    }


    public static String sampleBuilder(Note note) {


        return "SHEESH";
    }


    public static String gmMapper(int bank) {
        return switch (bank) {
            case 0, 1, 2, 3, 4, 5 -> "piano";
            case 6 -> "harpsichord";
            case 7 -> "clavinet";
            case 8 -> "celesta";
            case 9 -> "glockenspiel";
            case 10 -> "music_box";
            case 11 -> "vibraphone";
            case 12 -> "marimba";
            case 13 -> "xylophone";
            case 14 -> "tubular_bells";
            case 15 -> "dulcimer";
            case 16, 17, 18, 19, 20 -> "organ";
            case 21 -> "accordion";
            case 22 -> "harmonica";
            case 23 -> "tango_accordion";
            case 24 -> "acoustic_guitar_nylon";
            case 25 -> "acoustic_guitar_steel";
            case 26 -> "electric_guitar_jazz";
            case 27 -> "electric_guitar_clean";
            case 28 -> "electric_guitar_muted";
            case 29 -> "overdriven_guitar";
            case 30 -> "distortion_guitar";
            case 31 -> "guitar_harmonics";
            case 32 -> "acoustic_bass";
            case 33 -> "electric_bass_finger";
            case 34 -> "electric_bass_pick";
            case 35 -> "fretless_bass";
            case 36 -> "slap_bass_1";
            case 37 -> "slap_bass_2";
            case 38 -> "synth_bass_1";
            case 39 -> "synth_bass_2";
            case 40 -> "violin";
            case 41 -> "viola";
            case 42 -> "cello";
            case 43 -> "contrabass";
            case 44 -> "tremolo_strings";
            case 45 -> "pizzicato_strings";
            case 46 -> "orchestral_harp";
            case 47 -> "timpani";
            case 48 -> "string_ensemble_1";
            case 49 -> "string_ensemble_2";
            case 50 -> "synth_strings_1";
            case 51 -> "synth_strings_2";
            case 52 -> "choir_aahs";
            case 53 -> "voice_oohs";
            case 54 -> "synth_choir";
            case 55 -> "orchestra_hit";
            case 56 -> "trumpet";
            case 57 -> "trombone";
            case 58 -> "tuba";
            case 59 -> "muted_trumpet";
            case 60 -> "french_horn";
            case 61 -> "brass_section";
            case 62 -> "synth_brass_1";
            case 63 -> "synth_brass_2";
            case 64 -> "soprano_sax";
            case 65 -> "alto_sax";
            case 66 -> "tenor_sax";
            case 67 -> "baritone_sax";
            case 68 -> "oboe";
            case 69 -> "english_horn";
            case 70 -> "bassoon";
            case 71 -> "clarinet";
            case 72 -> "piccolo";
            case 73 -> "flute";
            case 74 -> "recorder";
            case 75 -> "pan_flute";
            case 76 -> "blown_bottle";
            case 77 -> "shakuhachi";
            case 78 -> "whistle";
            case 79 -> "ocarina";
            case 80 -> "lead_1_square";
            case 81 -> "lead_2_sawtooth";
            case 82 -> "lead_3_calliope";
            case 83 -> "lead_4_chiff";
            case 84 -> "lead_5_charang";
            case 85 -> "lead_6_voice";
            case 86 -> "lead_7_fifths";
            case 87 -> "lead_8_bass__lead";
            case 88 -> "pad_1_new_age";
            case 89 -> "pad_2_warm";
            case 90 -> "pad_3_polysynth";
            case 91 -> "pad_4_choir";
            case 92 -> "pad_5_bowed";
            case 93 -> "pad_6_metallic";
            case 94 -> "pad_7_halo";
            case 95 -> "pad_8_sweep";
            case 96 -> "fx_1_rain";
            case 97 -> "fx_2_soundtrack";
            case 98 -> "fx_3_crystal";
            case 99 -> "fx_4_atmosphere";
            case 100 -> "fx_5_brightness";
            case 101 -> "fx_6_goblins";
            case 102 -> "fx_7_echoes";
            case 103 -> "fx_8_scifi";
            case 104 -> "sitar";
            case 105 -> "banjo";
            case 106 -> "shamisen";
            case 107 -> "koto";
            case 108 -> "kalimba";
            case 109 -> "bagpipe";
            case 110 -> "fiddle";
            case 111 -> "shanai";
            case 112 -> "tinkle_bell";
            case 113 -> "agogo";
            case 114 -> "steel_drums";
            case 115 -> "woodblock";
            case 116 -> "taiko_drum";
            case 117 -> "melodic_tom";
            case 118 -> "synth_drum";
            case 119 -> "reverse_cymbal";
            case 120 -> "guitar_fret_noise";
            case 121 -> "breath_noise";
            case 122 -> "seashore";
            case 123 -> "bird_tweet";
            case 124 -> "telephone_ring";
            case 125 -> "helicopter";
            case 126 -> "applause";
            case 127 -> "gunshot";
            default -> "";
        };
    }
}
