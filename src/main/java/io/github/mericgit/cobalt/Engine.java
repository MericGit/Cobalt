package io.github.mericgit.cobalt;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class Engine {

    public static void playSoundProcess(Player player, ArrayList<Note> soundProcess) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        Mapper.updateRRTimeConv(MidiUtils.getTempo(),MidiUtils.getPPQ());
        System.out.println(MidiUtils.getFinalProcess());
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (MidiUtils.getFinalProcess().size() > 1  )
                    playSound(player, MidiUtils.getFinalProcess());
                else {
                    executor.shutdown();
                    System.out.print("TERMINATED THREAD");
                }
            }
        }, 0,1, TimeUnit.MILLISECONDS);
    }

    private static void playSound(Player player, ArrayList<Note> soundProcess) {
        if (soundProcess.get(0).getMcTick() <= 0 ) {
            if (soundProcess.get(0).getVelocity() != 0 && soundProcess.get(0).getDataF1() == 0 && !soundProcess.get(0).getSample().equals("TBD")) {
                player.sendMessage(ChatColor.GOLD + " Current tick: " + ChatColor.WHITE + soundProcess.get(0).getTick() + ChatColor.GREEN + " Playing note: " + ChatColor.AQUA + soundProcess.get(0).getKey() + " At sample " + soundProcess.get(0).getSample() + " At Volume " + ( (float) soundProcess.get(0).getVelocity() / 127) + " Accuracy: " + soundProcess.get(0).getMcTick());
                player.playSound(player.getLocation(),soundProcess.get(0).getSample(), ( (float) soundProcess.get(0).getVelocity() / 127), Note.advFreq(soundProcess.get(0)));
            }
            else if (soundProcess.get(0).getVelocity() == 0 && soundProcess.get(0).getDataF1() == 0 && !soundProcess.get(0).getSample().contains("stac")) {
                String sample = soundProcess.get(0).getSample().substring(0, soundProcess.get(0).getSample().lastIndexOf('_')).replaceFirst("_sus_","_rel_");
                player.playSound(player.getLocation(),sample, ( (float) 1), Note.advFreq(soundProcess.get(0)));
                player.stopSound(soundProcess.get(0).getSample());
                player.sendMessage(ChatColor.GOLD + " Current tick: " + ChatColor.WHITE + soundProcess.get(0).getTick() + ChatColor.RED + " Stopping note: " + ChatColor.AQUA + soundProcess.get(0).getKey() + " At sample " + soundProcess.get(0).getSample()  + " At Volume " + ( (float) soundProcess.get(0).getVelocity() / 127) + " Accuracy: " + soundProcess.get(0).getMcTick());
            }
            soundProcess.remove(0);
        }
        soundProcess.get(0).setMcTick(soundProcess.get(0).getMcTick() - 1);
    }
}



