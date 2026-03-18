package me.kso4more.fromnull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;


import static me.kso4more.fromnull.ReadWriteFile.entityToUuid;
import static me.kso4more.fromnull.ReadWriteFile.printEntityMap;
import static org.bukkit.Bukkit.getServer;

public class StatusCountdown {
    static HashMap<String, String> countdownMapTemp = ReadWriteFile.fileToMap("countdownMap.txt");
    static HashMap<Entity, String> countdownMap = ReadWriteFile.uuidToEntity(countdownMapTemp, ReadWriteFile.allEntity);

    static HashMap<Entity, String> vaccineLag = new HashMap<>();
    static ArrayList<Entity> deadEntity = new ArrayList<>();
    public static void update(Entity entity, String string){
        countdownMap.put(entity, string);
        ReadWriteFile.writeFile(entityToUuid(countdownMap), "countdownMap.txt");
    }

    public static void minusOne() throws IOException {
        HashMap<Entity, String> newCountdownMap = new HashMap<>();
        removeDeadEntity();

        if (vaccineLag.size() > 0) {
            setVaccine();
        }

        for (Entity i : countdownMap.keySet()) {
            if (i == null) continue;

            String value = countdownMap.get(i);
            String status = value.split(":")[0];
            int time = Integer.parseInt(value.split(":")[1]) - 1;
            if (time <= 0)
            {
                String nextStage = getNextStage(status);
                System.out.println("Countdown to ZERO, nextStage: " + nextStage);
                if (!nextStage.equals("S")){
                    int newTime = getCountdownTime(nextStage);
                    newCountdownMap.put(i, (nextStage + ":" + newTime));
                    if (i.getType() == EntityType.PLAYER) {
                        SeirStatus.allPlayerIDMap.put(String.valueOf(i.getUniqueId()), (nextStage + ":" + newTime));
                    }

                    if (Objects.equals(nextStage, "I"))
                    {
                        FromNull.confirmed++;
                        System.out.println("CONFIRMED COUNT: " + FromNull.confirmed);
                        ReadWriteFile.appendToFile("confirmedList.txt", String.valueOf(i.getUniqueId()));
                    }
                }
                else {
                    //SeirStatus.seirMap.put(i, "S");
                    SeirStatus.update(i, "S");
                    if (i.getType() == EntityType.PLAYER) {
                        SeirStatus.allPlayerIDMap.put(String.valueOf(i.getUniqueId()), "S");
                    }
                }
            }
            else{
                String newValue = status + ":" + time;
                newCountdownMap.put(i, newValue);
                System.out.println(i.getUniqueId() + " countdown: " + newValue);
                if (i.getType() == EntityType.PLAYER) {
                    SeirStatus.allPlayerIDMap.put(String.valueOf(i.getUniqueId()), newValue);
                }

            }

        }
        StatusCountdown.countdownMap = newCountdownMap;
        ReadWriteFile.printEntityMap(countdownMap, "countdownMap");

        checkInfectious();
    }

    public static void setVaccine() throws IOException {
        HashMap<Entity, String> newVaccineLag = new HashMap<>();
        for (Entity entity : vaccineLag.keySet()) {
            Integer waitTime = Integer.parseInt(vaccineLag.get(entity));
            if (waitTime > 0) {
                newVaccineLag.put(entity, String.valueOf(waitTime - 1));
            }
            else {

                if (FromNull.deathRateV > Math.random())
                {
                    SeirStatus.update(entity, "D");
                }

                else {
                    if (SeirStatus.seirMap.get(entity) == "S" || SeirStatus.seirMap.get(entity) == "R"){
                        SeirStatus.update(entity, "V");
                    }
                }


            }
        }
        vaccineLag = newVaccineLag;
        printEntityMap(vaccineLag, "vaccineLag");
    }

    public static void removeDeadEntity() throws IOException {
        for (Entity entity: SeirStatus.seirMap.keySet()) {
            if (SeirStatus.seirMap.get(entity).equals("D")) {
                if (!deadEntity.contains(entity)) {
                    deadEntity.add(entity);
                }
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.setHealth(0.0);
                if (livingEntity.hasPotionEffect(PotionEffectType.WEAKNESS)) {
                    getServer().broadcastMessage( ChatColor.WHITE +entity.getName() + " 死於疾病感染");
                }

                else{
                    getServer().broadcastMessage( ChatColor.WHITE + entity.getName() + " 死於施打疫苗");
                }
                if (entity.getType().equals(EntityType.PLAYER)) {
                    Player p = (Player) entity;
                    if (p.hasPotionEffect(PotionEffectType.WEAKNESS)) {
                        p.sendMessage(ChatColor.RED + "You died of disease");
                        //getServer().broadcastMessage( ChatColor.WHITE +entity.getName() + "死於確診");
                    }

                    else{
                        p.sendMessage(ChatColor.RED + "You died of vaccine");
                        //getServer().broadcastMessage( ChatColor.WHITE + entity.getName() + "died of vaccination");
                    }

                }
            }
        }
        for (Entity entity : deadEntity) {
            if (countdownMap.containsKey(entity)) {
                countdownMap.remove(entity);
            }
            if (entity.getType() == EntityType.VILLAGER){
                SeirStatus.seirMap.remove(entity);
            } else if (entity.getType() == EntityType.PLAYER) {
                SeirStatus.update(entity, "S");
            }
            countdownMap.remove(entity);
            System.out.println("MABS INFO: " + entity.getUniqueId() + " has dead, removed from map");
        }
        deadEntity.clear();

    }


    public static String getNextStage(String status) throws IOException {
        switch (status){
            case "E":
                return "I";
            case "I":
                if (Math.random() > FromNull.deathRateI) {
                    return "R";
                } else {
                    return "D";
                }
            case "R":
            case "V":
                return "S";
            default:
                System.out.println("Unexpected value of getNextStage: " + status + ";" + status.strip());
                String output = System.currentTimeMillis() + "," + "getNextStage" + "," + status;
                ReadWriteFile.appendToFile("exception.txt", output);
                return "NA";
        }
    }

    public static int normalDistribution(float mean, float std) {
        Random random = new Random();
        int val = (int) (random.nextGaussian() * std + mean);
        float timeIntervalPerDay = 96f;
        val *= timeIntervalPerDay;
        return val;
    }

    public static int getCountdownTime(String status) throws IOException {
        if (status.equals("D")) {
            return 99;
        }


        else if (status.equals("E") || status.equals("I") || status.equals("R") || status.equals("V")) {
            switch (status){
                case "E":
                    return normalDistribution(FromNull.exposedMean, FromNull.exposedStd);
                case "I":
                    return normalDistribution(FromNull.infectiousMean, FromNull.infectiousStd);
                case "R":
                    return normalDistribution(FromNull.removedMean, FromNull.removedStd);
                case "V":
                    return normalDistribution(FromNull.removedMean, FromNull.removedStd);
                    //return normalDistribution(FromNull.vaccineMean, FromNull.vaccineStd);
            }
        }

        System.out.println("Unexpected value of getCountdownTime: " + status + ";" + status.strip());
        String output = System.currentTimeMillis() + "," + "getCountdownTime" + "," + status;
        ReadWriteFile.appendToFile("exception.txt", output);
        return -99;
    }

    public static void checkInfectious() throws IOException {
        ArrayList<Entity> newExposed = new ArrayList<>();
        for (Entity i : countdownMap.keySet()) {
            String paste = countdownMap.get(i);
            String status = paste.split(":")[0];
            if (Objects.equals(status, "I")){
                HashMap<Entity, Entity> returnExposed = Infectious.entityNearInfectious(i);
                for (Entity newExposedEntity : returnExposed.keySet()) {
                    if (!newExposed.contains(newExposedEntity)) {
                        newExposed.add(newExposedEntity);
                        String output = System.currentTimeMillis() + "," + returnExposed.get(newExposedEntity).getUniqueId() + "," + newExposedEntity.getUniqueId();
                        ReadWriteFile.appendToFile("infection.txt", output);
                    }
                }
            }
        }
        for (Entity i : newExposed) {
            SeirStatus.update(i, "E");
        }
    }
}
