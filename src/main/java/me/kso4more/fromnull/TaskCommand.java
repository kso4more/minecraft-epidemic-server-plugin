package me.kso4more.fromnull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class TaskCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        String output = System.currentTimeMillis() + "," + player.getUniqueId() + "," + "TaskCommand";
        try {
            ReadWriteFile.appendToFile("commandIssued.txt", output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File myObj = new File("outputData\\existence.txt");
        int death = 0;
        Scanner myReader = null;
        try {
            myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                if (Objects.equals(data.split(",")[2], String.valueOf(player.getUniqueId())) &
                        Objects.equals(data.split(",")[3], "death")
                ) {death++;}
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Inventory inventory = Bukkit.createInventory(player, 9, "Emerald (Count:" + ListenerEvents.taskMap.get(player) + ", Death: " + death + ")");

        player.openInventory(inventory);

        return true;
    }
}
