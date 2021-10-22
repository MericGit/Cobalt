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
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (MidiUtils.getFinalProcess().size() > 1  )
                    playSound(player, MidiUtils.getFinalProcess());
            }
        }, 1000,1, TimeUnit.MILLISECONDS);
    }





    private static void playSound(Player player, ArrayList<Note> soundProcess) {

        if (soundProcess.get(0).getMcTick() <= 0) {
            //System.out.println("CURRENT: " + soundProcess.get(0));
            if (soundProcess.get(0).getVelocity() != 0 || soundProcess.get(0).getVelocity() != 128) {
                soundProcess.get(0).setSample(String.valueOf(soundProcess.get(0).getKey() - 48));
                soundProcess.get(0).setFreq(NotePitch.getPitch(soundProcess.get(0).getKey() -48));

                player.sendMessage(ChatColor.GOLD + "Playing note: " + ChatColor.AQUA + soundProcess.get(0));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, soundProcess.get(0).getFreq());
            } else if (soundProcess.get(0).getVelocity() == 0) {
            }
            soundProcess.remove(0);
        }
        soundProcess.get(0).setMcTick(soundProcess.get(0).getMcTick() - 1);
    }

    public enum NotePitch {
        NOTE_0(0, 0.5F, 0.50000F),
        NOTE_1(1, 0.53F, 0.52973F),
        NOTE_2(2, 0.56F, 0.56123F),
        NOTE_3(3, 0.6F, 0.59461F),
        NOTE_4(4, 0.63F, 0.62995F),
        NOTE_5(5, 0.67F, 0.66741F),
        NOTE_6(6, 0.7F, 0.70711F),
        NOTE_7(7, 0.76F, 0.74916F),
        NOTE_8(8, 0.8F, 0.79370F),
        NOTE_9(9, 0.84F, 0.84089F),
        NOTE_10(10, 0.9F, 0.89091F),
        NOTE_11(11, 0.94F, 0.94386F),
        NOTE_12(12, 1.0F, 1.00000F),
        NOTE_13(13, 1.06F, 1.05945F),
        NOTE_14(14, 1.12F, 1.12245F),
        NOTE_15(15, 1.18F, 1.18920F),
        NOTE_16(16, 1.26F, 1.25993F),
        NOTE_17(17, 1.34F, 1.33484F),
        NOTE_18(18, 1.42F, 1.41420F),
        NOTE_19(19, 1.5F, 1.49832F),
        NOTE_20(20, 1.6F, 1.58741F),
        NOTE_21(21, 1.68F, 1.68180F),
        NOTE_22(22, 1.78F, 1.78180F),
        NOTE_23(23, 1.88F, 1.88775F),
        NOTE_24(24, 2.0F, 2.00000F);

        public int note;
        public float pitchPre1_9;
        public float pitchPost1_9;

        NotePitch(int note, float pitchPre1_9, float pitchPost1_9) {
            this.note = note;
            this.pitchPre1_9 = pitchPre1_9;
            this.pitchPost1_9 = pitchPost1_9;
        }

        public static float getPitch(int note) {
            boolean pre1_9 = Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.7");
            for (NotePitch notePitch : values()) {
                if (notePitch.note == note) {
                    return pre1_9 ? notePitch.pitchPre1_9 : notePitch.pitchPost1_9;
                }
            }

            return 0.0F;
        }
    }
}



