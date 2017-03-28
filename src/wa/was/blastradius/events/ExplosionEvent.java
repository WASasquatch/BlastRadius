package wa.was.blastradius.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import wa.was.blastradius.terrain.Blast;
import wa.was.blastradius.utils.ConsoleColor;

public class ExplosionEvent implements Listener {
	
	private static JavaPlugin plugin;
	
	private static boolean pluginEnabled;
	private static List<Material> protectedMaterials = new ArrayList<Material>();
	private static List<Material> obliterateMaterials = new ArrayList<Material>();
	private static List<Material> innerMaterials = new ArrayList<Material>();
	private static List<Material> outerMaterials = new ArrayList<Material>();
	private static boolean doObliterate = true;
	private static boolean doFires = true;
	private static boolean doSmoke = true;
	private static int radius = 8;
	private static int fireRadius = 8;
	private static int smokeCount = 5;
	private static double smokeOffset = 0.5;
	
	public ExplosionEvent(JavaPlugin plug) {
		
		plugin = plug;
		
		try {
			
			pluginEnabled = plugin.getConfig().getBoolean("enable-br-effects");
			
			if ( pluginEnabled ) {
				
				protectedMaterials = parseMaterials(plugin.getConfig().getStringList("protected-materials"), "protected-materials");
				obliterateMaterials = parseMaterials(plugin.getConfig().getStringList("obliterate-materials"), "obliterate-materials");
				innerMaterials = parseMaterials(plugin.getConfig().getStringList("inner-blast-materials"), "inner-blast-materials");
				outerMaterials = parseMaterials(plugin.getConfig().getStringList("outer-blast-materials"), "outer-blast-materials");
				
				doObliterate = plugin.getConfig().getBoolean("obliterate-obliterables");
				doFires = plugin.getConfig().getBoolean("blast-fires");
				doSmoke = plugin.getConfig().getBoolean("blast-smoke");
				
				if ( plugin.getConfig().isSet("blast-radius")  
						&& plugin.getConfig().getInt("blast-radius") < 5 
							&& plugin.getConfig().getInt("blast-radius") > 15 ) {
					plugin.getLogger().warning(ConsoleColor.YELLOW+ConsoleColor.BOLD+"Blast radius must be between 5 - 15"+ConsoleColor.RESET+" | Defaulting to "+ConsoleColor.BOLD+"8"+ConsoleColor.RESET);
				} else {
					radius = plugin.getConfig().getInt("blast-radius");
				}
				
				if ( doFires ) {
	            	
					if ( plugin.getConfig().isSet("fire-radius")  
							&& plugin.getConfig().getInt("fire-radius") < 5 
								&& plugin.getConfig().getInt("fire-radius") > 15 ) {
						plugin.getLogger().warning(ConsoleColor.YELLOW+ConsoleColor.BOLD+"Fire radius must be between 5 - 15"+ConsoleColor.RESET+" | Defaulting to "+ConsoleColor.BOLD+"8"+ConsoleColor.RESET);
					} else {
						fireRadius = plugin.getConfig().getInt("fire-radius");
					}
					
				}
				
				if ( doSmoke ) {
					if ( plugin.getConfig().isSet("smoke-count")  
							&& plugin.getConfig().getInt("smoke-count") < 5 
								&& plugin.getConfig().getInt("smoke-count") > 15 ) {
						plugin.getLogger().warning(ConsoleColor.YELLOW+ConsoleColor.BOLD+"Smoke count must be between 2 - 15"+ConsoleColor.RESET+" | Defaulting to "+ConsoleColor.BOLD+"5"+ConsoleColor.RESET);
					} else {
						smokeCount = plugin.getConfig().getInt("smoke-count");
					}
					
					if ( plugin.getConfig().isSet("smoke-offset")  
							&& plugin.getConfig().getDouble("smoke-offset") > 1.5 ) {
						plugin.getLogger().warning(ConsoleColor.YELLOW+ConsoleColor.BOLD+"Smoke offset must be lower than 1.5"+ConsoleColor.RESET+" | Defaulting to "+ConsoleColor.BOLD+"0.5"+ConsoleColor.RESET);
					} else {
						smokeOffset = plugin.getConfig().getDouble("smoke-offset");
					}
					
				}
			
			}
			
		} catch(Exception e) {
			
			plugin.getLogger().severe(ConsoleColor.RED+ConsoleColor.BOLD+"There is a problem with your configuration."+ConsoleColor.RESET);
			e.printStackTrace();
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void tntWaterlog(EntityExplodeEvent e) {
		
		if ( pluginEnabled && ! ( e.getEntity() instanceof TNTPrimed ) ) return;

		Blast.createBlastRadius(e.getEntity().getLocation(), e.getEntity().getWorld(), innerMaterials, outerMaterials, protectedMaterials, obliterateMaterials, doObliterate, doFires, doSmoke, smokeCount, smokeOffset, radius, fireRadius);
		
	}
	
	public static List<Material> parseMaterials(List<String> mats, String list) {
		
		List<Material> materials = new ArrayList<Material>();
		try {
			for ( String material : mats ) {
				Material newMaterial = Material.getMaterial(material);
				if ( newMaterial != null ) {
					materials.add(newMaterial);
				}
			}
		} catch(Exception e) {
			plugin.getLogger().severe(ConsoleColor.RED+ConsoleColor.BOLD+"Invalid material provided in "+ConsoleColor.RESET+ConsoleColor.BOLD+list+ConsoleColor.RESET+ConsoleColor.RED+" list."+ConsoleColor.RESET);
			e.printStackTrace();
		}
		return materials;
		
	}

}
