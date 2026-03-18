package me.kso4more.fromnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.*;

public class Infectious {

    static void entityNearPlayer() throws IOException {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        String output = "";
        for (Entity e : SeirStatus.seirMap.keySet()){
            if (e == null) continue;
            Location location = e.getLocation();
            output = System.currentTimeMillis() + "," + e.getUniqueId() + "," + location;
            ReadWriteFile.appendToFile("Location.txt", output);
        }
        for (Player p : onlinePlayers){
            Location location = p.getLocation();

            Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities(location, FromNull.nearbyThreshold, FromNull.nearbyThreshold, FromNull.nearbyThreshold);
            for (Entity e : nearbyEntities){
                if (e.getType() == EntityType.VILLAGER || e.getType() == EntityType.PLAYER){
                    if(e.getType() == EntityType.VILLAGER) {ClassForListener.villagerSetting(e);}
                    LivingEntity entity1 = (LivingEntity) p;
                    LivingEntity entity2 = (LivingEntity) e;
                    if (Objects.equals(entity1, entity2)) {continue;}
                    if (entity1.hasLineOfSight(entity2) || entity2.hasLineOfSight(entity1)) {
                        output = System.currentTimeMillis() + "," + entity1.getUniqueId() + "," + entity2.getUniqueId() + "," + "direct";
                        ReadWriteFile.appendToFile("contact.txt", output);
                    } else {
                        output = System.currentTimeMillis() + "," + entity1.getUniqueId() + "," + entity2.getUniqueId() + "," + "nearby";
                        ReadWriteFile.appendToFile("contact.txt", output);
                    }
                }
            }
        }
    }
    public static HashMap<Entity, Entity> entityNearInfectious(Entity sourceEntity) throws IOException {
        LivingEntity livingEntity = (LivingEntity) sourceEntity;
        PotionEffect virus = new PotionEffect(PotionEffectType.WEAKNESS, FromNull.tickToRun * 3, 0, false, false);
        livingEntity.addPotionEffect(virus);

        HashMap<Entity,Entity> outputMap = new HashMap<>();
        Location location = sourceEntity.getLocation();
        Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities(location, FromNull.nearbyThreshold, FromNull.nearbyThreshold, FromNull.nearbyThreshold);

        for (Entity e : nearbyEntities){

            if (Objects.equals(e, sourceEntity)) {continue;}
            if (e.getType() == EntityType.VILLAGER || e.getType() == EntityType.PLAYER){
                if(e.getType() == EntityType.VILLAGER) {ClassForListener.villagerSetting(e);}
                LivingEntity entity1 = (LivingEntity) sourceEntity;
                LivingEntity entity2 = (LivingEntity) e;
                String contactType = null;
                if (entity1.hasLineOfSight(entity2) || entity2.hasLineOfSight(entity1)) {
                    contactType = "direct";
                }
                else {
                    contactType = "nearby";
                }
                String outputString = System.currentTimeMillis() + "," + entity1.getUniqueId() + "," + entity2.getUniqueId() + "," + contactType;
                ReadWriteFile.appendToFile("contact.txt", outputString);

                if (Objects.equals(SeirStatus.returnStatus(e), "S")){

                    if (contactType == "direct") {
                        if (FromNull.infectProb > Math.random())
                            outputMap.put(e, sourceEntity);
                    }
                }
            }
        }
        return outputMap;
    }
}
