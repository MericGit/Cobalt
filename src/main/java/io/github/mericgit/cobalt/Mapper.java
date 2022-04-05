package io.github.mericgit.cobalt;


import java.lang.reflect.Array;
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
        System.out.print("RR TIME CONV WAS UPDATED! OLD: " + timeConv);
        timeConv = ((float) 60000 / (tempo * PPQ));
        System.out.println("| NEW + " + timeConv);
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
        //*************************************************


        //System.out.println(sample);    <----- MASTER DEBUG LINE FOR RR POOL  (USE WHEN TEMP ARRAY IS NULL)


        //************************************************
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

    /**
     * Calculates the round-robin value of the sample to be used
     * and sets the sample name to reflect this.
     * @param soundProcess the input arraylist of notes
     * @return the updated sound process
     */
    public static ArrayList<Note> calcRRandOctave(ArrayList<Note> soundProcess) {
        for (int i =0; i < soundProcess.size(); i++) {
            //soundProcess.get(i).getSample().contains("_0s") &&
            if (soundProcess.get(i).getDataF1() == 0 && soundProcess.get(i).getVelocity() != 0 && soundProcess.get(i).getSample().contains("_sus") || soundProcess.get(i).getSample().contains("_perc") || soundProcess.get(i).getSample().contains("pizz")) {
                if (soundProcess.get(i).getSample().contains("missing")){
                    continue;
                }
                //System.out.println("Sample grab: " + Note.advSample2(soundProcess.get(i)));
                //System.out.println("OG" + soundProcess.get(i).getSample() + " | ");
                String eventName = Note.advSample2(soundProcess.get(i));
                if (soundProcess.get(i).getSample().contains("_sus")) {
                  eventName = eventName + "_" + Mapper.rrPoolInterface(Note.advSample2(soundProcess.get(i)), soundProcess.get(i).getTick());
                }
                soundProcess.get(i).setSample(eventName);
                for (int j = i; j < soundProcess.size(); j++) {
                    if (soundProcess.get(j).getKey() == soundProcess.get(i).getKey() && soundProcess.get(j).getVelocity() == 0 && soundProcess.get(j).getBank() == soundProcess.get(i).getBank()) {
                        soundProcess.get(j).setSample(eventName);
                        soundProcess.get(i).setDuration((soundProcess.get(j).getTick() * timeConv) - (soundProcess.get(i).getTick() * timeConv));
                        //soundProcess.get(j).setRelVol(0.6f * (    (float) soundProcess.get(i).getVelocity() / 127) / ((soundProcess.get(i).getDuration() / 1000)  * 2f));
                        soundProcess.get(j).setRelVol(calcVolDecay(((float) soundProcess.get(i).getVelocity() / 127),soundProcess.get(i).getDuration(),soundProcess.get(i).getSample()));
                        if (soundProcess.get(i).getDuration() < 250 && soundProcess.get(i).getSample().contains("_sus")) {
                            soundProcess.get(i).setSample(soundProcess.get(i).getSample().substring(0,soundProcess.get(i).getSample().lastIndexOf("_")).replaceFirst("_sus_","_stac_"));
                            soundProcess.get(j).setSample(soundProcess.get(i).getSample().substring(0,soundProcess.get(i).getSample().lastIndexOf("_")).replaceFirst("_sus_","_stac_"));
                        }

                                                      //Vol Mod    //Initial Volume                                  //Volume Drop Ratio
                        break;
                    }
                }
            }
            else if (!soundProcess.get(i).getSample().contains("_sus") && soundProcess.get(i).getDataF1() == 0 && soundProcess.get(i).getVelocity() != 0 && soundProcess.get(i).getChannel() ==9) {
                String eventName = Note.percMap(soundProcess.get(i));
                soundProcess.get(i).setSample(eventName);
            }
            else if (soundProcess.get(i).getDataF1() == 1) {
                Mapper.updateRRTimeConv(soundProcess.get(i).getFreq(),MidiUtils.getPPQ());
            }
        }
        return soundProcess;
    }
//Piano decay rate 0.033
    private static float calcVolDecay(float iVol,float duration, String instr) {
        float decayRate = 0;
        if (instr.contains("noir")){
            decayRate = 0.033f;
        }
        float decayAmt = duration * decayRate;
        if(decayAmt > 100) {
            decayAmt = 100;
        }
            return (iVol) * ((100f-decayAmt)/100);
    }

    public static void initInstrMap(ArrayList<Note> soundProcess) {
        for (int i = soundProcess.size() - 1; i >= 0; i--) {
            if (soundProcess.get(i).getDataF1() == 2) {
                //midiInstrMap.put(soundProcess.get(i).getBank(),soundProcess.get(i).getKey());
            }
        }   
        midiInstrMap.entrySet().forEach(entry -> {
            //System.out.println("Instr Bank: " + entry.getKey() + " ID: " + entry.getValue() + " " + gmMapper(entry.getValue()));
        });
    }

    public static void updateInstrMap(Note note) {
        midiInstrMap.put(note.getBank(),note.getKey());
    }

    public static HashMap<Integer, Integer> getMidiInstrMap() {
        return midiInstrMap;
    }

    public static ArrayList specialChannelInstrumentMapper(ArrayList<Note> soundProcess) {
        ArrayList pizzChannels = new ArrayList();
        for (int i = 0; i < soundProcess.size(); i++) {
            if (soundProcess.get(i).getDataF1() == 2 && soundProcess.get(i).getKey() == 45) {
                pizzChannels.add(soundProcess.get(i).getChannel());
            }
            else if (soundProcess.get(i).getDataF1() == 0) {
                if (pizzChannels.contains(soundProcess.get(i).getChannel()) && soundProcess.get(i).getSample().contains("bstr")) {
                    if (soundProcess.get(i).getSample().contains("_sus")) {
                        soundProcess.get(i).setSample(soundProcess.get(i).getSample().replaceFirst("bstr_sus","bstr_pizz").substring(0,soundProcess.get(i).getSample().lastIndexOf("_") + 1));
                    }
                    else if (soundProcess.get(i).getSample().contains("_stac")) {
                        soundProcess.get(i).setSample(soundProcess.get(i).getSample().replaceFirst("bstr_stac","bstr_pizz"));

                    }
                }
            }
        }
        return soundProcess;
    }


    public static ArrayList sampleBuilder(ArrayList<Note> soundProcess) {
        Mapper.initInstrMap(soundProcess);
        //System.out.print(soundProcess);
        String currentINSTR = "piano";
        int[] channelBasedINSTR = new int[16];
        for (int i = 0; i < soundProcess.size(); i++) {
            if (soundProcess.get(i).getDataF1() == 2) {
                Mapper.updateInstrMap(soundProcess.get(i));
                channelBasedINSTR[soundProcess.get(i).getChannel()] = soundProcess.get(i).getKey();
                currentINSTR = gmMapper(soundProcess.get(i).getKey());
                System.out.println("Updated Channel: " + soundProcess.get(i).getChannel() + " " + currentINSTR);
                //System.out.println("Raw: " + soundProcess.get(i).getKey());
            }

            else if (soundProcess.get(i).getDataF1() == 0) {
                if (Mapper.getMidiInstrMap().get(soundProcess.get(i).getBank()) != null) {
                    //System.out.println("Update: " + Mapper.getMidiInstrMap().get(soundProcess.get(i).getBank()));
                    if (soundProcess.get(i).getChannel() != 9) {
                        soundProcess.get(i).setSample(gmMapper(Mapper.getMidiInstrMap().get(soundProcess.get(i).getBank())));
                    }
                    else if (soundProcess.get(i).getChannel() == 9) {
                        soundProcess.get(i).setSample(Note.percMap(soundProcess.get(i)));
                        //System.out.println("Perc: " + soundProcess.get(i).getSample());
                    }
                    //System.out.println("Updated INSTRE (2) to: " + currentINSTR);
                }
                else {
                    //System.out.println("Missing  instr bank");
                    soundProcess.get(i).setSample(gmMapper(channelBasedINSTR[soundProcess.get(i).getChannel()]));
                }
            }
        }
        return soundProcess;
    }

    public static String caravanMapper(int bank) {
        return switch (bank) {
            case 0, 1, 2, 3, 4, 5 -> "piano_0s";
            case 9 -> "glockenspiel_0s";
            case 11 -> "vibraphone_0s";
            case 14 -> "tubular_bells_0s";
            case 32,33,34,35,44 -> "acoustic_bass_0s";
            case 56 -> "trumpet_0s";
            case 57 -> "trombone_0s";
            case 59 -> "muted_trumpet_0s";
            case 64 -> "soprano_sax_0s";
            case 65 -> "alto_sax_0s";
            case 66 -> "tenor_sax_0s";
            case 67 -> "baritone_sax_0s";
            case 71 -> "clarinet_0s";
            default -> "";
        };
    }
    public static String gmMapper(int bank) {
        return switch (bank) {
            case 0, 1, 2, 3, 4, 5 -> "noir_sus";    //titan_sus
            case 6 -> "harpsichord_0s";
            case 7 -> "clavinet_0s";
            case 8 -> "noir_sus";//"celesta_0s";
            case 9 -> "glock_perc"; //"glockenspiel_0s";
            case 10 -> "music_box_0s";
            case 11 -> "vib_perc"; //"vibraphone_0s";
            case 12 -> "marim_perc"; //"marimba_0s";
            case 13 -> "xylo_perc"; //"xylophone_0s";
            case 14 -> "tubular_perc"; //"tubular_bells_0s";
            case 15 -> "dulcimer_0s";
            case 16, 17, 18, 19, 20 -> "organ_0s";
            case 21 -> "accordion_0s";
            case 22 -> "harmonica_0s";
            case 23 -> "tango_accordion_0s";
            case 24 -> "acoustic_guitar_nylon_0s";
            case 25 -> "noir_sus"; //"acoustic_guitar_steel_0s";
            case 26 -> "noir_sus"; //"electric_guitar_jazz_0s";
            case 27 -> "noir_sus"; //"electric_guitar_clean_0s";
            case 28 -> "noir_sus"; //"electric_guitar_muted_0s";
            case 29 -> "noir_sus"; //"overdriven_guitar_0s";
            case 30 -> "noir_sus"; //"distortion_guitar_0s";
            case 31 ->  "noir_sus"; //"guitar_harmonics_0s";
            case 32 -> "noir_sus"; //"acoustic_bass_0s";
            case 33 -> "noir_sus"; //"electric_bass_finger_0s";
            case 34 -> "noir_sus"; //"electric_bass_pick_0s";
            case 35 -> "noir_sus"; //"fretless_bass_0s";
            case 36 -> "noir_sus"; //"slap_bass_1_0s";
            case 37 -> "noir_sus"; //"slap_bass_2_0s";
            case 38 -> "noir_sus"; //"synth_bass_1_0s";
            case 39 -> "synth_bass_2_0s";
            case 40 -> "bstr_sus";
            case 41 -> "bstr_sus";
            case 42 -> "bstr_sus";
            case 43 -> "bstr_sus";
            case 44 -> "bstr_sus";
            case 45 -> "bstr_pizz"; //"pizzicato_strings_0s";
            case 46 -> "harp_perc"; //"orchestral_harp_0s";
            case 47 -> "timpani_perc"; //"timpani_0s";
            case 48 -> "bstr_sus";
            case 49 -> "bstr_sus";
            case 50 -> "bstr_sus";
            case 51 -> "bstr_sus";
            case 52 -> "wood_sus"; //"choir_aahs_0s";
            case 53 -> "wood_sus"; //"voice_oohs_0s";
            case 54 -> "wood_sus"; //"synth_choir_0s";
            case 55 -> "orchestra_hit_0s";
            case 56 -> "brass_sus"; //"trumpet_0s";
            case 57 -> "brass_sus"; //"trombone_0s";
            case 58 -> "brass_sus"; //"tuba_0s";
            case 59 -> "brass_sus"; //"muted_trumpet_0s";
            case 60 -> "brass_sus"; //"french_horn_0s";
            case 61 -> "brass_sus"; //"brass_section_0s";
            case 62 -> "brass_sus"; //"synth_brass_1_0s";
            case 63 -> "brass_sus"; //"synth_brass_2_0s";
            case 64 -> "sax_sus";//"soprano_sax_0s";
            case 65 -> "soprano-missing";//"alto_sax_0s";
            case 66 -> "sax_sus";//"tenor_sax_0s";
            case 67 -> "sax_sus";//"baritone_sax_0s";
            case 68 -> "wood_sus"; //"oboe_0s";
            case 69 -> "wood_sus"; //"english_horn_0s";
            case 70 -> "wood_sus"; //"bassoon_0s";
            case 71 -> "wood_sus"; //"clarinet_0s";
            case 72 -> "wood_sus"; //"piccolo_0s";
            case 73 -> "wood_sus"; //"flute_sus";
            case 74 -> "wood_sus"; //"recorder_0s";
            case 75 -> "wood_sus"; //"pan_flute_0s";
            case 76 -> "blown_bottle_0s";
            case 77 -> "shakuhachi_0s";
            case 78 -> "wood_sus"; //"whistle_0s";
            case 79 -> "ocarina_0s";
            case 80 -> "lead_1_square_0s";
            case 81 -> "lead_2_sawtooth_0s";
            case 82 -> "lead_3_calliope_0s";
            case 83 -> "lead_4_chiff_0s";
            case 84 -> "lead_5_charang_0s";
            case 85 -> "lead_6_voice_0s";
            case 86 -> "lead_7_fifths_0s";
            case 87 -> "lead_8_bass__lead_0s";
            case 88 -> "pad_1_new_age_0s";
            case 89 -> "pad_2_warm_0s";
            case 90 -> "pad_3_polysynth_0s";
            case 91 -> "pad_4_choir_0s";
            case 92 -> "pad_5_bowed_0s";
            case 93 -> "pad_6_metallic_0s";
            case 94 -> "pad_7_halo_0s";
            case 95 -> "pad_8_sweep_0s";
            case 96 -> "fx_1_rain_0s";
            case 97 -> "fx_2_soundtrack_0s";
            case 98 -> "fx_3_crystal_0s";
            case 99 -> "fx_4_atmosphere_0s";
            case 100 -> "fx_5_brightness_0s";
            case 101 -> "fx_6_goblins_0s";
            case 102 -> "fx_7_echoes_0s";
            case 103 -> "fx_8_scifi_0s";
            case 104 -> "sitar_0s";
            case 105 -> "banjo_0s";
            case 106 -> "shamisen_0s";
            case 107 -> "koto_0s";
            case 108 -> "kalimba_0s";
            case 109 -> "pipes_sus"; //"wood_sus"; //"bagpipe_0s";
            case 110 -> "fiddle_0s";
            case 111 -> "shanai_0s";
            case 112 -> "tinkle_bell_0s";
            case 113 -> "agogo_0s";
            case 114 -> "steel_drums_0s";
            case 115 -> "woodblock_0s";
            case 116 -> "taiko_drum_0s";
            case 117 -> "melodic_tom_0s";
            case 118 -> "synth_drum_0s";
            case 119 -> "reverse_cymbal_0s";
            case 120 -> "guitar_fret_noise_0s";
            case 121 -> "breath_noise_0s";
            case 122 -> "seashore_0s";
            case 123 -> "bird_tweet_0s";
            case 124 -> "telephone_ring_0s";
            case 125 -> "helicopter_0s";
            case 126 -> "applause_0s";
            case 127 -> "gunshot_0s";
            default -> "";
        };
    }
}
