package wa.was.blastradius.terrain;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Blast {
	
	public static void createBlastRadius(Location center, World world, List<Material> imats, List<Material> omats, List<Material> pmats, List<Material> obmats, boolean obliterate, boolean fires, boolean smoke, int sc, double so, int radius, int fradius) {
		
		// Maybe some chunk checking... or something for performance... at a loss right now...
		try {
			obliterate(center, world, obmats, obliterate, radius);
			render(center, world, omats, pmats, radius, true);
			render(center, world, omats, pmats, (int)(radius/1.25), false);
			render(center, world, imats, pmats, (int)(radius/1.75), false);
			if ( fires ) {
				fires(center, world, pmats, fradius);
			}
			if ( smoke ) {
				smoke(center, world, sc, so, radius);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void smoke(Location center, World world, int sc, double so, int radius) {
		
        int cX = center.getBlockX();
        int cY = center.getBlockY();
        int cZ = center.getBlockZ();
        int radiusSquared = radius * radius;
        for ( int x = cX - radius; x <= cX + radius; x++ ) {
            for ( int y = cY - radius; y <= cY + radius; y++ ) {
                for ( int z = cZ - radius; z <= cZ + radius; z++ ) {
                    if ( (cX - x) * (cX - x) + (cY - y) * (cY - y) + (cZ - z) * (cZ - z) <= radiusSquared ) { 
                    	
                    	Block block = world.getBlockAt(x, y, z);
                    	Location smokeCenter = block.getLocation();
                    	int threshold = 20;
                    	
                    	if ( ! ( block.getType().equals(Material.AIR) ) ) {

                    		if ( randomInteger(0, threshold, new Random()) > 15 ) {
                    			createSmoke(smokeCenter, world, Particle.CLOUD, sc, so);
                    			createSmoke(smokeCenter, world, Particle.SMOKE_LARGE, sc, so);
                    		}
                    		
                    	}
                    	
                    }
                }
            }
        }

	}
	
	public static void fires(Location center, World world, List<Material> pmats, int radius) {
		
        int cX = center.getBlockX();
        int cY = center.getBlockY();
        int cZ = center.getBlockZ();
        int radiusSquared = radius * radius;
        for ( int x = cX - radius; x <= cX + radius; x++ ) {
            for ( int y = cY - radius; y <= cY + radius; y++ ) {
                for ( int z = cZ - radius; z <= cZ + radius; z++ ) {
                    if ( (cX - x) * (cX - x) + (cY - y) * (cY - y) + (cZ - z) * (cZ - z) <= radiusSquared ) { 
                    	
                    	Block block = world.getBlockAt(x, y, z);
                    	Block blockAbove = block.getRelative(BlockFace.UP, 1);
                    	int threshold = 10;
                    	
                    	if ( ! ( block.getType().equals(Material.AIR) ) ) {
                    		if ( ! ( pmats.contains(block.getType())) 
                    				&&  block.getType().isFlammable() 
                    					|| block.getType().equals(Material.NETHERRACK) ) {
                    				if ( blockAbove.getType().equals(Material.AIR) && randomInteger(0, threshold, new Random()) > 5 )
                    					blockAbove.setType(Material.FIRE);
                    			
                    		}
                    	}
                    	
                    }
                }
            }
        }

	}
	
	public static void obliterate(Location center, World world, List<Material> obmats, boolean obliterate, int radius) {
		
        int cX = center.getBlockX();
        int cY = center.getBlockY();
        int cZ = center.getBlockZ();
        int radiusSquared = radius * radius;
        for ( int x = cX - radius; x <= cX + radius; x++ ) {
            for ( int y = cY - radius; y <= cY + radius; y++ ) {
                for ( int z = cZ - radius; z <= cZ + radius; z++ ) {
                    if ( (cX - x) * (cX - x) + (cY - y) * (cY - y) + (cZ - z) * (cZ - z) <= radiusSquared ) { 
                    	
                    	Block block = world.getBlockAt(x, y, z);
                    	
                    	if ( ! ( block.getType().equals(Material.AIR) ) ) {
                    			
                    		if ( obmats.contains(block.getType()) ) {
                    			if ( ! ( obliterate ) ) {
                    				block.breakNaturally();
                    			}
                    			block.setType(Material.AIR);
                    		}
                    			
                    	}
                    	
                    }
                }
            }
        }

	}
	
	public static void render(Location center, World world, List<Material> mats, List<Material> pmats, int radius, boolean doRandom) {
		
        int cX = center.getBlockX();
        int cY = center.getBlockY();
        int cZ = center.getBlockZ();
        int radiusSquared = radius * radius;
        for ( int x = cX - radius; x <= cX + radius; x++ ) {
            for ( int y = cY - radius; y <= cY + radius; y++ ) {
                for ( int z = cZ - radius; z <= cZ + radius; z++ ) {
                    if ( (cX - x) * (cX - x) + (cY - y) * (cY - y) + (cZ - z) * (cZ - z) <= radiusSquared ) { 
                    	
                    	Block block = world.getBlockAt(x, y, z);
                    	int mc = mats.size()-1;
                    	int threshold = 10;
                    	
                    	if ( ! ( block.getType().equals(Material.AIR) ) ) {
                    		if ( ! ( pmats.contains(block.getType())) ) {
                    			
                    			if ( doRandom ) {
                    			
                    				if ( randomInteger(0, threshold, new Random()) > 5 ) {
		                    			Material newMaterial = mats.get(randomInteger(0, mc, new Random()));
		                    			block.setType(newMaterial);
                    				}
                    			
                    			} else {
	                    			Material newMaterial = mats.get(randomInteger(0, mc, new Random()));
	                    			block.setType(newMaterial);
                    			}
                    			
                    		}
                    	}
                    	
                    }
                }
            }
        }

	}
	
	public static void createSmoke(Location center, World world, Particle type, int count, double offset) {
		if ( count > 15 || count <= 2 ) {
			count = 5;
		}
		if ( offset > 1.5 ) {
			offset = 0.5;
		}
		world.spawnParticle(type, center, count, offset, 0, offset);
	}

	public static int randomInteger(int aStart, int aEnd, Random aRandom) {
		long range = (long)aEnd - (long)aStart + 1;
		long fraction = (long)(range * aRandom.nextDouble());
		int random = (int)(fraction + aStart);    
		return random;
	}	

}
