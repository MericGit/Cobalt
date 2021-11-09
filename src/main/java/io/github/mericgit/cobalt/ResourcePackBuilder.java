package io.github.mericgit.cobalt;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class ResourcePackBuilder {

    private static String sample;
    private static String file;
    private static ArrayList<String> eventNames;
    private static ArrayList<String> fileNames;

    public static void initResourcePool(String sampleName, String fileName) {

        eventNames = new ArrayList<>();
        fileNames = new ArrayList<>();
        sample = sampleName;
        file = fileName;

        for (int i = 1; i <= 7; i++) {
            eventNames.add(sample + i);
            fileNames.add(file + i);
        }

        System.out.println(eventNames);
        System.out.println(fileName);

    }

    public static void generateSoundsJson() {
        System.out.println("AMOOGSUS");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());

        ArrayList<String> eventName = new ArrayList<>();
        ArrayList<String> fileName = new ArrayList<>();
         sample = "block.note_block.splendor_a_";
         file = "block/note_block/splendor_a_";

        for (int i = 1; i <= 7; i++) {
            eventName.add(sample + i);
            fileName.add(file + i);
        }

        System.out.println(eventName);
        System.out.println(fileName);


        JSONObject soundEventsMaster = new JSONObject();
        for (int i = 0; i < 7; i++) {
            for (int j = 1; j<16; j++) {
                JSONObject soundEvent = new JSONObject();
                JSONArray sounds = new JSONArray();
                sounds.add(fileName.get(i));
                soundEvent.put("sounds",sounds);
                soundEvent.put("subtitle","subtitles.block.note_block.note");
                soundEventsMaster.put(eventName.get(i)+"_"+j,soundEvent);
            }
        }
        System.out.println(soundEventsMaster);
    }
}

