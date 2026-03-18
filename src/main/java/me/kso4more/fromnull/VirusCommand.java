package me.kso4more.fromnull;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;

import java.io.IOException;

public class VirusCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                for (Entity entity : SeirStatus.seirMap.keySet()) {
                    if (entity == null) continue;
                    if (args[0].equals(String.valueOf(entity.getUniqueId())) || args[0].equals(entity.getCustomName())) {
                        if (SeirStatus.seirMap.get(entity).equals("S")) {
                            try {
                                SeirStatus.update(entity, "E");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {
                            System.out.println("Target entity is not susceptible");
                        }
                        return true;
                    }
                }
                System.out.println("Target entity not found");
                return false;
            }
            else {
                System.out.println("Arguments length should be 1");
            }
        }
        System.out.println("Command should be called by console");
        return false;
    }
}
