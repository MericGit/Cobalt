package io.github.mericgit.cobalt;
import com.sun.tools.javac.Main;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Cobalt extends JavaPlugin {

    private static Cobalt instance;
    @Override
    public void onEnable() {
        instance = this;
        System.out.println("Attention all Crewmates. Cobalt has been loaded");
        this.getCommand("PlaySoundProcess").setExecutor(new PlaySoundProcess());
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static Cobalt getPlugin(){
        return instance;
    }
}