package me.kso4more.fromnull;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.joml.Math;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.kso4more.fromnull.ReadWriteFile.appendToFile;
import static me.kso4more.fromnull.ReadWriteFile.entityToUuid;

public final class FromNull extends JavaPlugin {

    // parameters
    static final int nearbyThreshold = 4;
    static final float deathRateV = 0.05f;
    static final float deathRateI = 0.3f;
    static final float exposedMean = 1f;
    static final float exposedStd = 0f;
    static final float infectiousMean = 7f;
    static final float infectiousStd = (float) Math.sqrt(2);
    static final float removedMean = 36f;
    static final float removedStd = 0f;
    static final float vaccineMean =3f;
    static final float vaccineStd = 0f;
    static final float infectProb = 0.02f;
    static int confirmed = -99;
    static int tickToRun = 60;
    static float villagerVaccinatedProb = 0.1f;
    static ArrayList<String> vaccinePlayer = new ArrayList<>();


    @Override
    public void onEnable() {

        System.out.println("FromNull Plugin onEnable!!!!");
        Bukkit.getServer().getWorlds().get(0).setTime(60);
        // Plugin startup logic

        // read infected file
        try {
            ReadWriteFile.appendToFile("confirmedList.txt", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File myObj = new File("outputData\\confirmedList.txt");
        Scanner myReader = null;
        try {
            myReader = new Scanner(myObj);
            confirmed = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data != "") {confirmed++;}
            }
        } catch (FileNotFoundException e) {
            try {
                ReadWriteFile.appendToFile("confirmedList.txt", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

        // read vaccination file
        try {
            ReadWriteFile.appendToFile("vaccination.txt", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        myObj = new File("outputData\\vaccination.txt");
        myReader = null;
        try {
            myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data != "") {
                    System.out.println(data.split(",")[2]);
                    if (Objects.equals(data.split(",")[2], "success"))
                    {
                        if (!vaccinePlayer.contains(data.split(",")[1])){
                            vaccinePlayer.add(data.split(",")[1]);
                        }
                    }
                    if (Objects.equals(data.split(",")[2], "death"))
                    {
                        if (vaccinePlayer.contains(data.split(",")[1])){
                            vaccinePlayer.remove(data.split(",")[1]);
                        }
                    }
                }
            }
            System.out.println("JUST FOR TEST: vaccineCount=" + vaccinePlayer.size());
        } catch (FileNotFoundException e) {
            try {
                ReadWriteFile.appendToFile("confirmedList.txt", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

        // get command
        getCommand("vaccine").setExecutor(new MenuCommand());
        getCommand("task").setExecutor(new TaskCommand());
        getCommand("virus").setExecutor(new VirusCommand());
        getCommand("lobby").setExecutor(new LobbyCommand());
        getCommand("confirmed").setExecutor(new ConfirmedCommand());
        getCommand("vaccineCoverage").setExecutor(new VaccineCoverageCommand());


        BukkitScheduler scheduler = getServer().getScheduler();
        HashMap<String, Entity> allEntity = ReadWriteFile.getAllEntity();
        getServer().getPluginManager().registerEvents(new ListenerEvents(), this);
        SeirStatus seirStatusObject = new SeirStatus();
        StatusCountdown statusCountdownObject = new StatusCountdown();
        for(Entity entity : Bukkit.getServer().getWorld("World").getEntities()) {
            if(entity.getType() == EntityType.VILLAGER) {
                try {
                    ClassForListener.villagerSetting(entity);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' hh:mm:ss");

        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {

                int worldTime = (int) Bukkit.getServer().getWorlds().get(0).getTime();
                Bukkit.getServer().getWorlds().get(0).setTime(worldTime + tickToRun * 3);
                if (Bukkit.getServer().getWorlds().get(0).getTime() > 23040){
                    Bukkit.getServer().getWorlds().get(0).setTime(0);
                }
                long now = System.currentTimeMillis();
                Date date = new Date();
                System.out.println("MABSINFO: currentTime: " + now + ", worldTime: " + worldTime + ", " + ft.format(date));
                try {
                    Infectious.entityNearPlayer();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    StatusCountdown.minusOne();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }

        }, 0L, tickToRun); //1000 milliseconds = 1 second = 20 ticks

    }

    @Override
    public void onDisable() {
        System.out.println("FromNull Plugin onDisable!!!!");

        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            if (player == null) continue;
            try {
                ClassForListener.playerQuit(player);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        ReadWriteFile.writeFile(entityToUuid(SeirStatus.seirMap), "seirMap.txt");
        ReadWriteFile.writeFile(entityToUuid(StatusCountdown.countdownMap), "countdownMap.txt");
        ReadWriteFile.writeFile(SeirStatus.allPlayerIDMap, "allPlayerIDMap.txt");
        ReadWriteFile.printEntityMap(SeirStatus.seirMap, "seirMap");
        ReadWriteFile.printEntityMap(StatusCountdown.countdownMap, "countdownMap");
        ReadWriteFile.printEntityMap(StatusCountdown.vaccineLag, "vaccineLag");
        ReadWriteFile.printStringMap(SeirStatus.allPlayerIDMap, "allPlayerIDMap");

    }



}
