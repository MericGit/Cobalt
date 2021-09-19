package io.github.mericgit.cobalt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Engine {

    public static void playSoundProcess(Player player, ArrayList<Note> soundProcess) {
        final long[] masterDelay = {0};
        for (int i = 0; i < soundProcess.size(); i++) {
            int finalI = i;
            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    System.out.println(finalI);
                    if (soundProcess.get(finalI).getVelocity() != 0) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 100, pitchConvert(soundProcess.get(finalI).getKey()));
                        player.sendMessage(ChatColor.GREEN + "Playing sound with delay of " + (long) (soundProcess.get(finalI).getTick() * MidiUtils.getTimeConverter() / 0.05));
                        System.out.println("Ran note play");
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Note off event");
                        System.out.println("Ran note off");

                    }
                    player.sendMessage(String.valueOf((long)((soundProcess.get(finalI).getTick() * MidiUtils.getTimeConverter()) / 0.05)));
                    masterDelay[0] +=(long)((soundProcess.get(finalI).getTick() * MidiUtils.getTimeConverter()) / 0.05);
                    System.out.println("MasterDelay is: " + masterDelay[0]);
                }
            };
            task.runTaskLater(Cobalt.getPlugin(), masterDelay[0] );
        }
    }

//(long)((soundProcess.get(finalI).getTick() * MidiUtils.getTimeConverter()) / 0.05)
    public static long pitchConvert(int key) {
        long pitch = 2^(key-12);
        return pitch;


    }
}

