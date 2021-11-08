package io.github.mericgit.cobalt;


import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class ResourcePackBuilder {

    public static void main(String[] args) throws Exception {
        System.out.println("AMOOGSUS");
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());

        ArrayList<String> eventName = new ArrayList<>();
        ArrayList<String> fileName = new ArrayList<>();
        String sample = "block.note_block.splendor_a_";
        String file = "block/note_block/splendor_a_";

        for (int i = 1; i <= 7; i++) {
            eventName.add(sample + i);
            fileName.add(file + i);
        }

        System.out.println(eventName);
        System.out.println(fileName);


        JSONObject soundEventsMaster = new JSONObject();
        for (int i = 0; i < 7; i++) {
            soundEventsMaster.put(eventName.get(0),soundEvent);
        }
        JSONObject soundEvent = new JSONObject();

    }
}

