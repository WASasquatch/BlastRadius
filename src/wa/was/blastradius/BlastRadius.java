package wa.was.blastradius;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import wa.was.blastradius.events.ExplosionEvent;
import wa.was.blastradius.utils.ConsoleColor;

public class BlastRadius extends JavaPlugin {
	
	@Override
	public void onEnable() {
		createConfig();
		getServer().getPluginManager().registerEvents(new ExplosionEvent(this), this);
	}
	
    private void createConfig() {
    	boolean success = false;
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info(ConsoleColor.GREEN+ConsoleColor.BOLD+"Creating configuration..."+ConsoleColor.RESET);
                saveDefaultConfig();
            } else {
                getLogger().info(ConsoleColor.GREEN+ConsoleColor.BOLD+"Loading configuration..."+ConsoleColor.RESET);
            }
            success = true;
        } catch (Exception e) {
            getLogger().severe(ConsoleColor.RED+ConsoleColor.BOLD+"Error creating configuration!"+ConsoleColor.RESET);
            e.printStackTrace();
            success = false;
        }
        if ( success ) {
            getLogger().info(ConsoleColor.GREEN+ConsoleColor.BOLD+"Configuration succesfully loaded."+ConsoleColor.RESET);
        }
    }

}
