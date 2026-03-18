package me.kso4more.fromnull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionData;

import org.bukkit.potion.PotionType;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import static java.lang.Math.round;
import static org.bukkit.Bukkit.createMap;
import static org.bukkit.Bukkit.getWorld;

public class MenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player player = (Player) commandSender;
        String output = System.currentTimeMillis() + "," + player.getUniqueId() + "," + "MenuCommand";
        try {
            ReadWriteFile.appendToFile("commandIssued.txt", output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Inventory inventory = Bukkit.createInventory(player, 9, "Menu Inventory");

        ItemStack vaccine = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) vaccine.getItemMeta();
        potionMeta.setDisplayName(ChatColor.DARK_GREEN + "Vaccine");
        potionMeta.setColor(Color.GREEN);

        ArrayList<String> potionLore = new ArrayList<>();
        potionLore.add("This vaccine will give you immunity");
        potionLore.add("after a while,");
        potionLore.add("with " + round(FromNull.deathRateV * 100) + "% chance of death");
        potionLore.add("**It will not give you any other effects");
        potionMeta.setLore(potionLore);
        PotionData potionData = new PotionData(PotionType.WATER);
        potionMeta.setBasePotionData(potionData);
        vaccine.setItemMeta(potionMeta);
        /*

        ItemStack news = new ItemStack(Material.PAPER);
        ItemMeta meta = news.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "News");
        ArrayList<String> lore = new ArrayList<>();
        LocalDateTime dateTimeNow = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateTimeNow.format(myFormatObj);
        lore.add("Comfirmed Cases: " + FromNull.confirmed);
        lore.add("");
        lore.add("Update Time: " + formattedDate);
        meta.setLore(lore);
        news.setItemMeta(meta);

         */


        ItemStack map = new ItemStack(Material.FILLED_MAP, 1);
        MapView mapView = createMap(Bukkit.getServer().getWorlds().get(0));
        MapView.Scale scale = MapView.Scale.NORMAL;
        mapView.setScale(scale);
        mapView.setCenterX(0);
        mapView.setCenterZ(0);
        mapView.setTrackingPosition(true);
        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        mapMeta.setMapView(mapView);

        map.setItemMeta(mapMeta);

        ItemStack seed1 = new ItemStack(Material.WHEAT_SEEDS,8);
        ItemStack seed2 = new ItemStack(Material.BEETROOT_SEEDS,8);
        ItemStack seed3 = new ItemStack(Material.POTATO,8);
        ItemStack seed4 = new ItemStack(Material.CARROT,8);



        inventory.setItem(0, vaccine);

        ItemStack armor1 = new ItemStack(Material.IRON_CHESTPLATE);
        inventory.setItem(1, armor1);

        ItemStack armor2 = new ItemStack(Material.IRON_LEGGINGS);
        inventory.setItem(2, armor2);

        inventory.setItem(3,map);
        inventory.setItem(4,seed1);
        inventory.setItem(5,seed2);

        //inventory.setItem(1, news);
        player.openInventory(inventory);

        return true;
    }
}
