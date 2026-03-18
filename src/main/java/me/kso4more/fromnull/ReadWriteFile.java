package me.kso4more.fromnull;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;

public class ReadWriteFile {
    public static HashMap<String, Entity> allEntity = getAllEntity();

    static HashMap<String, Entity> getAllEntity() {

        HashMap<String, String> temp = fileToMap("seirMap.txt");
        ArrayList<String> allUuid = new ArrayList<>();
        for (String i : temp.keySet()) {
            allUuid.add(i);
        }
        System.out.println("output allUuid");
        for (String i : allUuid) {
            System.out.println("UUID: " + i);
        }
        HashMap<String, Entity> allEntity = new HashMap<>();


        for (Entity entity : Bukkit.getServer().getWorld("World").getEntities()) {
            String uuidString = String.valueOf(entity.getUniqueId());
            System.out.println("ENTITY::::::" + uuidString);

            if (allUuid.contains(uuidString)){
                allEntity.put(uuidString, entity);
            }
        }

        printAllEntity(allEntity);
        return allEntity;
    }
    static void printAllEntity(HashMap<String, Entity> allEntity) {
        System.out.println("PRINTING allEntity Results");
        for (String uuidString : allEntity.keySet()) {
            if (allEntity.get(uuidString) != null)
                System.out.println(uuidString + " paired " + allEntity.get(uuidString).getCustomName());
            else
                System.out.println(uuidString + " not paired yet");
        }
    }

    static void printEntityMap(HashMap<Entity, String> targetMap, String mapName){
        System.out.println("=========START PRINTING " + mapName);
        for (Entity i : targetMap.keySet()) {
            System.out.println("key: " + i.getUniqueId() + " value: " + targetMap.get(i));
        }
    }
    static void printStringMap(HashMap<String, String> targetMap, String mapName){
        System.out.println("=========START PRINTING " + mapName);
        for (String i : targetMap.keySet()) {
            System.out.println("key: " + i + " value: " + targetMap.get(i));
        }
    }


    static void appendToFile(String filename, String newline) throws IOException {
        //System.out.println("AppendToFile: " + filename + ", String: " + newline);
        filename = "outputData\\" + filename;
        FileWriter fw = new FileWriter(filename, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(newline);
        bw.newLine();
        bw.close();
    }
    static HashMap<String, String> fileToMap(String filename){
        System.out.println("Executing fileToMap, filename: " + filename);
        filename = "outputData\\" + filename;
        HashMap<String, String> outputMap = new HashMap<>();
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] splitResult = data.split(":");
                String uniqueId = splitResult[0];
                String value = null;
                if (splitResult.length == 2){
                    value = splitResult[1];
                } else if (splitResult.length == 3) {
                    value = splitResult[1] + ":" + splitResult[2];
                }
                outputMap.put(uniqueId, value);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        printStringMap(outputMap, filename);
        return outputMap;
    }

    /*
    static ArrayList<String> fileToList(String filename){
        System.out.println("Executing fileToList, filename: " + filename);
        ArrayList<String> output = new ArrayList<>();
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                output.add(data);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return output;
    }
    */
    static HashMap<Entity, String> uuidToEntity(HashMap<String, String> sourceMap, HashMap<String, Entity> allEntity){
        HashMap<Entity, String> output = new HashMap<>();
        for (String uuid : sourceMap.keySet()) {
           output.put(allEntity.get(uuid), sourceMap.get(uuid));
        }
        return output;
    }

    static HashMap<String, String> entityToUuid(HashMap<Entity, String> sourceMap){
        HashMap<String, String> output = new HashMap<>();
        for (Entity entity : sourceMap.keySet()) {
            if (entity == null) {continue;}
            output.put(String.valueOf(entity.getUniqueId()), sourceMap.get(entity));
        }
        return output;
    }

    static void writeFile(HashMap<String, String> targetMap, String filename){
        filename = "outputData\\" + filename;
        System.out.println("START writeFile, filename:" + filename);
        File file = new File(filename);
        BufferedWriter bf = null;
        try {
            bf = new BufferedWriter(new FileWriter(file));
            for (String i : targetMap.keySet()) {
                bf.write(i + ":" + targetMap.get(i));
                bf.newLine();
                System.out.println( i + ":" + targetMap.get(i));
            }
            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                bf.close();
            }
            catch (Exception e) {
            }
        }
    }

    public static void printPlayerMap(HashMap<Player, Integer> playerMap, String mapName) {
        System.out.println("=========START PRINTING " + mapName);
        for (Entity i : playerMap.keySet()) {
            System.out.println("key: " + i.getUniqueId() + " value: " + playerMap.get(i));
        }
    }
}
