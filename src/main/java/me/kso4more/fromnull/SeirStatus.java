package me.kso4more.fromnull;

import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import static me.kso4more.fromnull.ReadWriteFile.allEntity;
import static me.kso4more.fromnull.ReadWriteFile.entityToUuid;

public class SeirStatus {
    static HashMap<String, String> seirMapTemp = ReadWriteFile.fileToMap("seirMap.txt");
    static HashMap<Entity, String> seirMap = ReadWriteFile.uuidToEntity(seirMapTemp, ReadWriteFile.allEntity);
    static HashMap<String, String> allPlayerIDMap = ReadWriteFile.fileToMap("allPlayerIDMap.txt"); //id:status

    public static void firstCheck(Entity mapKey) throws IOException {
        if (EntityType.VILLAGER == mapKey.getType()){
            if (!seirMap.containsKey(mapKey)){
                update(mapKey, "S");
            }
        } else if (EntityType.PLAYER == mapKey.getType()) {
            ReadWriteFile.allEntity.put(String.valueOf(mapKey.getUniqueId()), mapKey);
            ReadWriteFile.printAllEntity(ReadWriteFile.allEntity);
            if (!allPlayerIDMap.containsKey(String.valueOf(mapKey.getUniqueId()))){
                allPlayerIDMap.put(String.valueOf(mapKey.getUniqueId()), "S");
                ReadWriteFile.writeFile(allPlayerIDMap, "allPlayerIDMap.txt");
            }
            else {

                String value = allPlayerIDMap.get(String.valueOf(mapKey.getUniqueId()));
                if (Objects.equals(value, "S"))
                {
                    update(mapKey, "S");
                }
                else {
                    String status = value.split(":")[0];
                    update(mapKey, status);
                    StatusCountdown.update(mapKey, value);
                }
            }

        }
    }

    public static void update(Entity mapKey, String mapValue) throws IOException {
        String originalStatus = null;
        if (seirMap.containsKey(mapKey)) {
            originalStatus = seirMap.get(mapKey);
            String output = System.currentTimeMillis() + "," + mapKey.getUniqueId() + "," + seirMap.get(mapKey) + "," + mapValue;
            ReadWriteFile.appendToFile("statusUpdate.txt", output);

        }
        else {
            String output = System.currentTimeMillis() + "," + mapKey.getUniqueId() + "," + "null" + "," + mapValue;
            ReadWriteFile.appendToFile("statusUpdate.txt", output);
        }

        seirMap.put(mapKey, mapValue);
        if (mapKey.getType() == EntityType.PLAYER) {
            SeirStatus.allPlayerIDMap.put(String.valueOf(mapKey.getUniqueId()), mapValue);
        }
        if (Objects.equals(mapValue, "V") || Objects.equals(mapValue, "E")) {
            int countdownTime = StatusCountdown.getCountdownTime(mapValue);
            String string = String.format("%s:%d", mapValue, countdownTime);
            StatusCountdown.update(mapKey, string);

        }

        ReadWriteFile.writeFile(entityToUuid(seirMap), "seirMap.txt");

    }

    public static String returnStatus(Entity e) {
        String s = seirMap.get(e);
        return s;
    }




}
