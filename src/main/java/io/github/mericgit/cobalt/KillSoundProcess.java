package io.github.mericgit.cobalt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class KillSoundProcess implements CommandExecutor {
        // This method is called, when somebody uses our command
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (MidiUtils.getFinalProcess().size() > 1) {
                    player.sendMessage(ChatColor.GOLD + "WARNING: BUGGY BETA COMMAND! " + ChatColor.AQUA + " Found Running Process:");
                    MidiUtils.getFinalProcess().clear();
                    player.sendMessage(ChatColor.DARK_RED + "Killed Process");
                }



            }


            return false;
        }
}
