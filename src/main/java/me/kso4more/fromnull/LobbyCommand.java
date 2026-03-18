package me.kso4more.fromnull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.material.MaterialData;

import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class LobbyCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            if (args.length == 1)
            {
                World world = getServer().getWorlds().get(0);
                Location spawn = world.getSpawnLocation();
                int X = spawn.getBlockX();
                int Y = spawn.getBlockY();
                int Z = spawn.getBlockZ();
                Location loc = null;
                if (Objects.equals(args[0], "close")){

                    loc = new Location(world, X + 3, Y, Z);
                    world.getBlockAt(loc).setType(Material.OBSIDIAN);
                    loc = new Location(world, X + 3, Y + 1, Z);
                    world.getBlockAt(loc).setType(Material.OBSIDIAN);
                    System.out.println("JUST FOR TEST: " + loc);
                    return true;
                /*
                for(int i = (X - 2); i <= (X + 2); i ++) {
                    for(int j = (Z - 2); j <= (Z + 2); j ++) {
                        Location loc = new Location(world, i, Y - 1, j);
                        world.getBlockAt(loc).setType(Material.OBSIDIAN);
                        loc =
                    }

                }

                 */
                }
                if (Objects.equals(args[0], "open")){
                    //loc = new Location(world, X + 3, Y, Z);
                    //world.getBlockAt(loc).setType(Material.AIR);
                    //loc = new Location(world, X + 3, Y + 1, Z);
                    //world.getBlockAt(loc).setType(Material.AIR);

                    loc = new Location(world, X + 3, Y, Z);
                    world.getBlockAt(loc).setType(Material.CRIMSON_FENCE_GATE);
                    loc = new Location(world, X + 3, Y + 1, Z);
                    world.getBlockAt(loc).setType(Material.AIR);
                    //final Block bottom = world.getBlockAt(loc);
                    //final Block top = bottom.getRelative(BlockFace.UP, 1);
                    //top.setType(Material.CRIMSON_DOOR);
                    //bottom.setType(Material.CRIMSON_DOOR);

                    //Door bd = (Door) bottom;
                    //bd.setHalf(Bisected.Half.BOTTOM);

                    //Door td = (Door) top;
                    //td.setHalf(Bisected.Half.TOP);


                    System.out.println("JUST FOR TEST: " + loc);
                    return true;
                }
                System.out.println("JUST FOR TEST: failed, args[0]: " + args[0]);

            }
        }

        return false;
    }
}
