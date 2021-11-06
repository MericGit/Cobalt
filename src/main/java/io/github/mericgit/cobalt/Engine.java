package io.github.mericgit.cobalt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class Engine {

    public static void playSoundProcess(Player player, ArrayList<Note> soundProcess) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        Note.updateRRTimeConv(MidiUtils.getTempo(),MidiUtils.getPPQ());
        System.out.println("TEMPO: " + MidiUtils.getTempo());
        System.out.println("PPQ" + MidiUtils.getPPQ());
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (MidiUtils.getFinalProcess().size() > 1  )
                    playSound(player, MidiUtils.getFinalProcess());
            }
        }, 1500,1, TimeUnit.MILLISECONDS);
    }





    private static void playSound(Player player, ArrayList<Note> soundProcess) {

        if (soundProcess.get(0).getMcTick() <= 0) {
            //System.out.println("CURRENT: " + soundProcess.get(0));
            if (soundProcess.get(0).getVelocity() != 0 && soundProcess.get(0).getDataF1() == 0) {
                soundProcess.get(0).setSample(String.valueOf(soundProcess.get(0).getKey() - 48));
                soundProcess.get(0).setFreq(Note.advFreq(soundProcess.get(0)));
                player.sendMessage(ChatColor.RED + " Current tick: " + ChatColor.GREEN + soundProcess.get(0).getTick() + ChatColor.GOLD + " Playing note: " + ChatColor.AQUA + soundProcess.get(0).getKey() + " At sample " + Note.advSample2(soundProcess.get(0)) + " At freq " + Note.advFreq(soundProcess.get(0)) + " At Volume " + ( (float) soundProcess.get(0).getVelocity() / 127) + " Accuracy: " + soundProcess.get(0).getMcTick());
                player.playSound(player.getLocation(), Note.advSample2(soundProcess.get(0)), ( (float) soundProcess.get(0).getVelocity() / 127), Note.advFreq(soundProcess.get(0)));
            } else if (soundProcess.get(0).getDataF1() == 1) {
                Note.updateRRTimeConv(soundProcess.get(0).getFreq(),MidiUtils.getPPQ());
            }
            soundProcess.remove(0);
        }
        soundProcess.get(0).setMcTick(soundProcess.get(0).getMcTick() - 1);
    }


}



