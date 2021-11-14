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
                String eventName = Note.advSample2(soundProcess.get(i)) + "_" + Note.rrPoolInterface(Note.advSample2(soundProcess.get(i)), soundProcess.get(i).getTick());
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
                Note.updateRRTimeConv(soundProcess.get(i).getFreq(),MidiUtils.getPPQ());
            }
        }
        return soundProcess;
    }
}
