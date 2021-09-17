package io.github.mericgit.cobalt;

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
            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    System.out.print("test");
                }
            };
            // Run the task on this plugin in 3 seconds (60 ticks)
            task.runTaskLater(Cobalt.getPlugin(), 20 * 3);



        }


        return false;
    }
}