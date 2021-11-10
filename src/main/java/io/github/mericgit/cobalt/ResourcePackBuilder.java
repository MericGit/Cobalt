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



        for (int i = 1; i <= 7; i++) {
            eventNames.add(sample + i);
            fileNames.add(file + i);
        }

        System.out.println(eventNames);
        System.out.println(fileNames);


        JSONObject soundEventsMaster = new JSONObject();
        for (int i = 0; i < 7; i++) {
            for (int j = 1; j<16; j++) {
                JSONObject soundEvent = new JSONObject();
                JSONArray sounds = new JSONArray();
                sounds.add(fileNames.get(i));
                soundEvent.put("sounds",sounds);
                soundEvent.put("subtitle","subtitles.block.note_block.note");
                soundEventsMaster.put(eventNames.get(i)+"_"+j,soundEvent);
            }
        }
        System.out.println(soundEventsMaster);
    }
}

