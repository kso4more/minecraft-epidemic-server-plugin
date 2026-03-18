package me.kso4more.fromnull;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

import static java.lang.Math.round;

public class VaccineCoverageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        String output = System.currentTimeMillis() + "," + player.getUniqueId() + "," + "VaccineCoverageCommand";
        try {
            ReadWriteFile.appendToFile("commandIssued.txt", output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("JUST FOR TEST: vaccineCount = " + FromNull.vaccinePlayer.size());
        System.out.println("JUST FOR TEST: playerCount = " + SeirStatus.allPlayerIDMap.size());
        int vaccineCoverage = round((float) FromNull.vaccinePlayer.size() / (float) SeirStatus.allPlayerIDMap.size() * 100);
        player.sendMessage(ChatColor.YELLOW + "Vaccine Coverage: " + vaccineCoverage + " %");

        return true;
    }
}
