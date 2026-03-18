package me.kso4more.fromnull;

//import jdk.javadoc.internal.doclint.HtmlTag;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import static me.kso4more.fromnull.ReadWriteFile.entityToUuid;
import static org.bukkit.Bukkit.getServer;

public class ListenerEvents implements Listener {
    public static HashMap<Player, Integer> taskMap = new HashMap<Player, Integer>();

    /*
    @EventHandler
    public void onPlayerSleep(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        String locX = String.valueOf(Math.round(loc.getX() * 100) / 100);
        String locY = String.valueOf(Math.round(loc.getY() * 100) / 100);
        String locZ = String.valueOf(Math.round(loc.getZ() * 100) / 100);
        getServer().broadcastMessage( player.getName() + " is sleeping at: " + locX + ", " + locY + ", " + locZ);
    }

     */



    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) throws IOException {
        Entity entity = event.getEntity();
        EntityType entityType = entity.getType();
        if (entityType == EntityType.VILLAGER){
            System.out.println("JUST FOR TEST: " + entity.getUniqueId() + " spawn");
            ClassForListener.villagerSetting(entity);
        }
    }




    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {

        Player player = event.getPlayer();
        String output = System.currentTimeMillis() + "," + player.getName() + "," + player.getUniqueId() + "," + "playerJoin";
        ReadWriteFile.appendToFile("existence.txt", output);
        SeirStatus.firstCheck(player);

        HashMap<String, String> vaccineLagOffline = ReadWriteFile.fileToMap("vaccineLagOffline.txt");
        if (vaccineLagOffline.containsKey(String.valueOf(player.getUniqueId()))) {
            StatusCountdown.vaccineLag.put(player, vaccineLagOffline.get(String.valueOf(player.getUniqueId())));
            vaccineLagOffline.remove(String.valueOf(player.getUniqueId()));
            ReadWriteFile.writeFile(vaccineLagOffline, "vaccineLagOffline.txt");
        }
        try {
            File myObj = new File("outputData\\" + player.getUniqueId() + ".txt");
            if(myObj.exists() && !myObj.isDirectory()) {
                Scanner myReader = new Scanner(myObj);
                Integer emeraldCount = 0;
                while (myReader.hasNextLine()) {
                    String s = myReader.nextLine();
                    emeraldCount += Integer.parseInt(s.split(",")[1]);
                }
                taskMap.put(player, emeraldCount);
            } else {
                ReadWriteFile.appendToFile(player.getUniqueId() + ".txt",  System.currentTimeMillis() + ",0");
                taskMap.put(player, 0);
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
            taskMap.put(player, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (FromNull.vaccinePlayer.contains(player.getUniqueId().toString())){
            System.out.println("JUST FOR TEST: " + player.getUniqueId() + " has vaccinated");
            //player.setDisplayName(ChatColor.DARK_GREEN + ChatColor.stripColor(player.getDisplayName()));
            player.setPlayerListName(ChatColor.DARK_GREEN + player.getName());
        } else {
            System.out.println("JUST FOR TEST: " + player.getUniqueId() + " has NOT vaccinated, start to print vaccinePlayer: ");
            for (String uuid: FromNull.vaccinePlayer){
                System.out.println(uuid);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) throws IOException {
        Player player = event.getPlayer();
        ClassForListener.playerQuit(player);
    }
    /*
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


        StatusCountdown.countdownMap.remove(player);
        SeirStatus.seirMap.remove(player);
        StatusCountdown.vaccineLag.remove(player);
    }

     */
    @EventHandler
    public void onDamage (EntityDamageEvent e) throws IOException {

        if (e.getEntityType() == EntityType.VILLAGER){
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onInventoryClosed (InventoryCloseEvent e) throws IOException {
        String title = e.getView().getTitle();
        if (title.length() >= 7 && title.substring(0, 7).equals("Emerald")) {
            Inventory inventory = e.getInventory();


            for(int i = 0; i < 9; i++){
                if (inventory.getItem(i) != null) {

                    if (String.valueOf(inventory.getItem(i).getType()).equals("EMERALD")) {

                        String s = System.currentTimeMillis() + "," + inventory.getItem(i).getAmount();
                        Player player = (Player) e.getPlayer();
                        //System.out.println(player.getUniqueId() + " has handed " + inventory.getItem(i).getAmount() + " emerald(s)");
                        taskMap.put(player, taskMap.get(player) + inventory.getItem(i).getAmount());
                        //getServer().broadcastMessage( player.getName() + " handed in " + inventory.getItem(i).getAmount() + " emeralds");
                        ReadWriteFile.appendToFile( e.getPlayer().getUniqueId()+ ".txt", s);
                    }
                }

            }
            ReadWriteFile.printPlayerMap(taskMap, "taskMap");
        }

    }

    @EventHandler
    public void onConsumeVaccine (PlayerItemConsumeEvent e) throws IOException {

        ItemStack item = e.getItem();

        if (item.getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN + "Vaccine")) {
            Player player = e.getPlayer();
            String status = SeirStatus.seirMap.get(player);
            String output = null;
            switch (status.strip()) {
                case "I":
                    output = System.currentTimeMillis() + "," + player.getUniqueId() + "," + "failedCauseByInfectious";
                    ReadWriteFile.appendToFile("vaccination.txt", output);
                    player.sendMessage(ChatColor.RED + "People who are infected cannot be vaccinated");
                    break;
                case "V":
                    output = System.currentTimeMillis() + "," + player.getUniqueId() + "," + "failedCauseByVaccinated";
                    ReadWriteFile.appendToFile("vaccination.txt", output);
                    player.sendMessage(ChatColor.RED + "You've been vaccinated already.");
                    break;
                case "S":
                case "E":
                case "R":
                case "D":
                case "null":
                    if (!StatusCountdown.vaccineLag.containsKey(player)) {
                        int vaccineLagTime = StatusCountdown.normalDistribution(FromNull.vaccineMean, FromNull.vaccineStd);
                        StatusCountdown.vaccineLag.put(player, String.valueOf(vaccineLagTime));
                        //player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, vaccineLagTime * FromNull.tickToRun, 0));
                        output = System.currentTimeMillis() + "," + player.getUniqueId() + "," + "success";
                        FromNull.vaccinePlayer.add(String.valueOf(player.getUniqueId()));
                        ReadWriteFile.appendToFile("vaccination.txt", output);
                        //player.setDisplayName(ChatColor.DARK_GREEN + ChatColor.stripColor(player.getDisplayName()));
                        player.setPlayerListName(ChatColor.DARK_GREEN + player.getName());
                        player.sendMessage(ChatColor.GREEN + "You have completed vaccination");
                    }
                    else {
                        output = System.currentTimeMillis() + "," + player.getUniqueId() + "," + "failedCauseByVaccinationLag";
                        ReadWriteFile.appendToFile("vaccination.txt", output);
                        player.sendMessage(ChatColor.RED + "You've been vaccinated already.");
                    }
                    break;
                default:
                    System.out.println("Unexpected value of onConsumeVaccine: " + status + ";" + status.strip());
                    output = System.currentTimeMillis() + "," + "onConsumeVaccine" + "," + status;
                    ReadWriteFile.appendToFile("exception.txt", output);
            }



        }



    }


    @EventHandler
    public void onEntityDeath (EntityDeathEvent e) throws IOException {

        Entity entity = e.getEntity();

        String output = null;
        if (e.getEntityType() == EntityType.VILLAGER) {
            output = System.currentTimeMillis() + "," + entity.getCustomName() + "," + entity.getUniqueId() + "," + "death";
            ReadWriteFile.appendToFile("existence.txt", output);
        }
        else if (e.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) entity;

            output = System.currentTimeMillis() + "," + entity.getName() + "," + entity.getUniqueId() + "," + "death";
            ReadWriteFile.appendToFile("existence.txt", output);
            player.setPlayerListName(ChatColor.stripColor(player.getName()));

            output = System.currentTimeMillis() + "," + player.getUniqueId() + "," + "death";
            ReadWriteFile.appendToFile("vaccination.txt", output);
            /*

            Player player = (Player) entity;
            String s = System.currentTimeMillis() + "," + (FromNull.loseEmerald * -1);
            ReadWriteFile.appendToFile( player.getUniqueId()+ ".txt", s);
            taskMap.put(player, taskMap.get(player) - FromNull.loseEmerald);

             */
            /*
            int minus = ListenerEvents.taskMap.get(entity) * -1;
            String s = System.currentTimeMillis() + "," + minus;
            ReadWriteFile.appendToFile( player.getUniqueId()+ ".txt", s);
            taskMap.put(player, 0);

             */
        }


        if ((entity.getType() == EntityType.VILLAGER) || (entity.getType() == EntityType.PLAYER)){
            if(StatusCountdown.vaccineLag.containsKey(entity)){
                StatusCountdown.vaccineLag.remove(entity);
            }
            if(FromNull.vaccinePlayer.contains(String.valueOf(entity.getUniqueId()))){
                FromNull.vaccinePlayer.remove(String.valueOf(entity.getUniqueId()));
            }
            StatusCountdown.deadEntity.add(entity);
        }

        /*
        if (SeirStatus.seirMap.containsKey(entity)) {
            SeirStatus.update(entity, "D");
        }

         */


    }


    @EventHandler
    public void onSendingChat (AsyncPlayerChatEvent e) throws IOException {
        Player player = e.getPlayer();
        String messsage = e.getMessage();
        String output = System.currentTimeMillis() + "," + player.getUniqueId() + "," + messsage;
        ReadWriteFile.appendToFile("chat.txt", output);

    }

}
