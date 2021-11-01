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
        this.getCommand("KillSoundProcess").setExecutor(new KillSoundProcess());

        if(!this.getDataFolder().exists()) {
            try {
                this.getDataFolder().mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        if (MidiUtils.getFinalProcess().size() > 1){
            MidiUtils.getFinalProcess().clear();
            System.out.println("Cleared all converted SoundProcesses");
        }
        // Plugin shutdown logic
    }


    public static Cobalt getPlugin(){
        return instance;
    }
}
