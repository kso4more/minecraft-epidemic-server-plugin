package me.kso4more.fromnull;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ConfirmedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        String output = System.currentTimeMillis() + "," + player.getUniqueId() + "," + "ConfirmedCommand";
        try {
            ReadWriteFile.appendToFile("commandIssued.txt", output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player.sendMessage(ChatColor.YELLOW + "Current confirmed cases: " + FromNull.confirmed);

        return true;
    }
}
