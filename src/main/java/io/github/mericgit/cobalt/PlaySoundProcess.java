package io.github.mericgit.cobalt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class PlaySoundProcess implements CommandExecutor {
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            String path = Cobalt.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedPath;
            try {
                decodedPath = URLDecoder.decode(path, "UTF-8");
                Player player = (Player) sender;
                String file = args[0];
                System.out.println(file);
                player.sendMessage(ChatColor.GOLD + "Running! " + ChatColor.AQUA + " Found file: " + ChatColor.GRAY + file);
                try {
                    File target = new File("../" + decodedPath + "Cobalt\\songs" + file);
                    Engine.playSoundProcess(player, MidiUtils.midiToNoteSequence(target));
                } catch (Exception e) {
                    e.printStackTrace();
                    Bukkit.getServer().broadcastMessage("!!!Yo this bugged!!! sheesh");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
