package io.github.mericgit.cobalt;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class PlaySoundProcess implements CommandExecutor {
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            //String file = args[1];
            //System.out.println(file);
            try {
                Engine.playSoundProcess(player,MidiUtils.midiToNoteSequence(MidiUtils.fileBuilder("test4.mid")));
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getServer().broadcastMessage("Yo this bugged");
            }


        }


        return false;
    }
}