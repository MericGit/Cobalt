package io.github.mericgit.cobalt;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class PlaySoundProcess implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            String filepath = "C:\\Users\\dongd\\Downloads\\Temp\\songTest.json";
            System.out.println("This ran");
            ResourcePackBuilder.registerFiles(filepath);
            System.out.println("Same here");
            Note.RRPoolToString();
            String path = Cobalt.getPlugin().getDataFolder().getAbsolutePath();
            String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);
            Player player = (Player) sender;
            String file = args[0];
            Note.setTargetSample(args[1]);
            System.out.println(file);
            player.sendMessage(ChatColor.GOLD + "Running! " + ChatColor.AQUA + " Found file: " + ChatColor.GRAY + file);
            try {
                File target = new File( path + "/songs/" + file);
                Engine.playSoundProcess(player, MidiUtils.midiToNoteSequence(target));
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(ChatColor.RED +"!!!Yo this bugged!!! Sus!");
            }
        }
        return false;
    }
}
