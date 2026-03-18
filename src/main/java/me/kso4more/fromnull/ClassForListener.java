package me.kso4more.fromnull;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class ClassForListener
{

    public static void playerQuit(Player player) throws IOException {
        String output = System.currentTimeMillis() + "," + player.getName() + "," + player.getUniqueId() + "," + "playerQuit";
        ReadWriteFile.appendToFile("existence.txt", output);

        if (!StatusCountdown.countdownMap.containsKey(player)) {
            SeirStatus.allPlayerIDMap.put(String.valueOf(player.getUniqueId()), "S");
        }
        else {
            SeirStatus.allPlayerIDMap.put(String.valueOf(player.getUniqueId()), StatusCountdown.countdownMap.get(player));
        }
        ReadWriteFile.printStringMap(SeirStatus.allPlayerIDMap, "allPlayerIDMap");
        if (StatusCountdown.vaccineLag.containsKey(player)) {
            HashMap<String, String> vaccineLagOffline = ReadWriteFile.fileToMap("vaccineLagOffline.txt");
            vaccineLagOffline.put(String.valueOf(player.getUniqueId()), StatusCountdown.vaccineLag.get(player));
            ReadWriteFile.printStringMap(vaccineLagOffline, "vaccineLagOffline");
            ReadWriteFile.writeFile(vaccineLagOffline, "vaccineLagOffline.txt");
        }
        if (ListenerEvents.taskMap.containsKey(player)){
            ListenerEvents.taskMap.remove(player);
        }


        StatusCountdown.countdownMap.remove(player);
        SeirStatus.seirMap.remove(player);
        StatusCountdown.vaccineLag.remove(player);
    }

    public static String getRandomName() {
        char[] alpha = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        Random r = new Random();

        String random = "";
        for (int i = 0; i < 8; i++) {
            random += alpha[r.nextInt(alpha.length)];
        }
        return(random);
    }

    public static void villagerSetting(Entity entity) throws IOException {

        if (Objects.equals(entity.getCustomName(), null)) {
            entity.setCustomName(getRandomName());
            System.out.println("JUST FOR TEST:" + entity.getName());
        }
        if (!SeirStatus.seirMap.containsKey(entity)){
            String output = System.currentTimeMillis() + "," + entity.getCustomName() + "," + entity.getUniqueId() + "," + "spawn";
            ReadWriteFile.appendToFile("existence.txt", output);
            if (Math.random() < FromNull.villagerVaccinatedProb){
                output = System.currentTimeMillis() + "," + entity.getCustomName() + "," + entity.getUniqueId() + "," + "vaccination";
                ReadWriteFile.appendToFile("existence.txt", output);
                entity.setCustomName(ChatColor.DARK_GREEN + entity.getCustomName());

                int vaccineLagTime = StatusCountdown.normalDistribution(FromNull.vaccineMean, FromNull.vaccineStd);
                StatusCountdown.vaccineLag.put(entity, String.valueOf(vaccineLagTime));
            }
            SeirStatus.firstCheck(entity);
        }


    }
}
