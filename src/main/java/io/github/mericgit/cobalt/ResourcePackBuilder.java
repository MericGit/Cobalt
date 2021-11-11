package io.github.mericgit.cobalt;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ResourcePackBuilder {

    private static String sample;
    private static String file;
    private static List<String> eventNames;
    private static List<String> fileNames;

    public static void registerFiles(String path) {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(path)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray samplesList = (JSONArray) obj;
            System.out.println(samplesList);

            //Iterate over array
            samplesList.forEach(sample -> parseSampleObject((JSONObject) sample));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void parseSampleObject(JSONObject sample) {
        JSONObject sampleObject = (JSONObject) sample.get("sample");
        if (sampleObject.get("type").equals("sus")) {
            int[] temp = new int[]{1, 0};
            Note.updatePool("block.note_block." + sampleObject.get("name"), temp);
        }
    }

    public static void generateSongJson(String path) {

        fileNames = new ArrayList<String>();
        eventNames = new ArrayList<>();
        File[] files = new File(path).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                fileNames.add(file.getName().substring(0, file.getName().lastIndexOf('.')));
            }
        }
        System.out.println(fileNames);
        System.out.println(eventNames);

        JSONArray songsSampleList = new JSONArray();
        for (int i = 0; i < fileNames.size(); i++) {
            if (fileNames.get(i).contains("_s_")) {
                JSONObject sample = new JSONObject();
                sample.put("name", fileNames.get(i));
                sample.put("type", "sus");
                JSONObject sampleObject = new JSONObject();
                sampleObject.put("sample", sample);
                songsSampleList.add(sampleObject);
            } else if (fileNames.get(i).contains("_r_")) {
                JSONObject sample = new JSONObject();
                sample.put("name", fileNames.get(i));
                sample.put("type", "rel");
                JSONObject sampleObject = new JSONObject();
                sampleObject.put("sample", sample);
                songsSampleList.add(sampleObject);
            } else {
                JSONObject sample = new JSONObject();
                sample.put("name", fileNames.get(i));
                sample.put("type", "other");
                JSONObject sampleObject = new JSONObject();
                sampleObject.put("sample", sample);
                songsSampleList.add(sampleObject);
            }
        }
        System.out.println(songsSampleList);
        try (FileWriter file = new FileWriter("C:\\Users\\dongd\\Downloads\\titan\\songTest.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(songsSampleList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public static void generateRPack (String path){

            fileNames = new ArrayList<String>();
            eventNames = new ArrayList<>();
            File[] files = new File(path).listFiles();

            for (File file : files) {
                if (file.isFile()) {
                    fileNames.add("block/note_block/" + file.getName().substring(0, file.getName().lastIndexOf('.')));
                    eventNames.add("block.note_block." + file.getName().substring(0, file.getName().lastIndexOf('.')));
                }
            }
            System.out.println(fileNames);
            System.out.println(eventNames);

            JSONObject soundEventsMaster = new JSONObject();
            for (int i = 0; i < fileNames.size(); i++) {
                if (fileNames.get(i).contains("_s_")) {
                    for (int j = 1; j < 16; j++) {
                        JSONObject soundEvent = new JSONObject();
                        JSONArray sounds = new JSONArray();
                        sounds.add(fileNames.get(i));
                        soundEvent.put("sounds", sounds);
                        soundEventsMaster.put(eventNames.get(i) + "_" + j, soundEvent);
                    }
                } else {
                    JSONObject soundEvent = new JSONObject();
                    JSONArray sounds = new JSONArray();
                    sounds.add(fileNames.get(i));
                    soundEvent.put("sounds", sounds);
                    soundEventsMaster.put(eventNames.get(i), soundEvent);
                }
            }
            System.out.println(soundEventsMaster);
        }

        public static List<String> getEventNames() {
            return eventNames;
        }

        public static List<String> getFileNames() {
            return fileNames;
        }
    }







