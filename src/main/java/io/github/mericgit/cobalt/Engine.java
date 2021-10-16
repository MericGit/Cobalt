package io.github.mericgit.cobalt;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class Engine {

    public static void playSoundProcess(Player player, ArrayList<Note> soundProcess) {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Cobalt.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (MidiUtils.getFinalProcess().size() > 1  )
                    playSound(player, MidiUtils.getFinalProcess());
            }
        }, 1000, 1);
    }
    private static void playSound(Player player, ArrayList<Note> soundProcess) {

        if (soundProcess.get(0).getMcTick() <= 0) {
            //System.out.println("CURRENT: " + soundProcess.get(0));
            if (soundProcess.get(0).getVelocity() != 0 || soundProcess.get(0).getVelocity() != 128) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, calcFreq(soundProcess.get(0).getKey()));
            } else if (soundProcess.get(0).getVelocity() == 0) {
            }
            soundProcess.remove(0);
        }
        soundProcess.get(0).setMcTick(soundProcess.get(0).getMcTick() - 1);
    }

    private static float calcFreq(int key) {
        int rel = (key-20) % 12;



        return 1F;
    }
}

