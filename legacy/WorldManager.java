package graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import backup.Character;

import utils.BufferedImageUtils;


public class WorldManager {
	World[][] worlds;
	int xWorlds, yWorlds;
	BufferedImage[] terrainTiles;
	
	public WorldManager(int xSize, int ySize, int worldSizeX, int worldSizeY, GameWindow parent) {
		xWorlds = xSize;
		yWorlds = ySize;
		
		System.out.println("Loading image tiles...");
	    terrainTiles = BufferedImageUtils.splitImage(BufferedImageUtils.loadImage(getClass().getResource("/graphics/terrain.png")), 16, 16);
		
		System.out.println("Generating "+Integer.toString(ySize*xSize)+" worlds...");
	    worlds = new World[ySize][xSize];
		for (int yr = 0; yr<ySize; yr++) {
			for (int xr = 0; xr<xSize; xr++) {
	    		worlds[yr][xr] = new World(worldSizeX, worldSizeY, parent, yr, xr, terrainTiles, this);
			}
		}
	}
	
	public void draw(Graphics2D g, Character p) {
		worlds[p.getYWorld()][p.getXWorld()].draw(g);
	}
	
	public boolean worldExists(int x, int y) {
		if ((x<xWorlds && x>=0) && (y<yWorlds && y>=0)){
			return true;
		}
		return false;
	}
	
	public World getWorld(int x, int y) {
		if (worldExists(x,y)) {
			return worlds[y][x];
		} else {
			throw new IllegalArgumentException("World["+x+","+y+"] does not exist");
		}
	}
	
	public void save(File savegameFolder) {
		for (int yr = 0; yr<yWorlds; yr++) {
			for (int xr = 0; xr<xWorlds; xr++) {
	    		worlds[yr][xr].save(savegameFolder, Integer.toString(yr)+","+Integer.toString(xr));
			}
		}
	}
	
	public void load(File folder) {
		File dir = folder;
		String[] children = dir.list();
		Pattern pattern = Pattern.compile("(\\d),(\\d).png");
		for (int i=0; i<children.length; i++) {
			 Matcher matcher = pattern.matcher(children[i]);
			 if (matcher.find()) {
				 String y = matcher.group(1);
				 String x = matcher.group(2);
				 System.out.println("Loading file: "+y+","+x+".png...");
				 worlds[Integer.parseInt(y)][Integer.parseInt(x)].load(folder, y+","+x+".png", terrainTiles);
			 }
		}
		
	}
}
