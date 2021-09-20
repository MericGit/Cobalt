package io.github.mericgit.cobalt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Engine {
    public static void playSoundProcess(Player player, ArrayList<Note> soundProcess) {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(Cobalt.getPlugin(), new Runnable() {
            @Override
            public void run() {
                playNote(player,MidiUtils.convertNonDelta(soundProcess));
            }
        }, 60L, 1L);
    }

//(long)((soundProcess.get(finalI).getTick() * MidiUtils.getTimeConverter()) / 0.05)
    private static long pitchConvert(int key) {
        long pitch = 2^(key-66);
        return pitch;
    }

    private static void playNote(Player player,ArrayList<Note> soundProcess) {
        if (soundProcess.get(0).getMcTick() <= 0) {
            if(soundProcess.get(0).getVelocity() != 0) {

                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, pitchConvert(soundProcess.get(0).getKey()));
                System.out.println("Playing note HZ: " + pitchConvert(soundProcess.get(0).getKey()));
            }
            soundProcess.remove(0);
        }
        soundProcess.get(0).setMcTick(soundProcess.get(0).getMcTick()-1);

        System.out.println("MIDI Ticks: " + soundProcess.get(0).getTick());
        System.out.println("Mc Ticks: " +  soundProcess.get(0).getMcTick());

    }



}

