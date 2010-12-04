package graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import utils.BufferedImageUtils;


import java.util.Random;


public class World {
	GameWindow parent;
	Image renderImage;
	Graphics2D render;
	int[][] worldMap;
	int ySize, xSize;
	int[] ores;
	
	public World(int x, int y, GameWindow owner, int yPos, int xPos, BufferedImage[] terrainTiles, WorldManager map) {
		System.out.println("Saving world size, and class parent..");
		ySize = y;
		xSize = x;
		parent = owner;
		ores = new int[5];
		ores[0] = 34; // Coal
		ores[1] = 33; // Iron
		ores[2] = 32; // Gold
		ores[3] = 51; // Redstone
		ores[4] = 50; // Diamond
		Random generator = new Random();
		
		System.out.println("Generating world...");
	    worldMap = new int[ySize][xSize];

	    
	    int grassHeight = 10;
	    if (xPos>0) {
	    	for (int yr = 0; yr<ySize; yr++) {
	    		if (map.getWorld(xPos-1, yPos).get(yr, xSize-1)==3) {
	    			grassHeight = yr;
	    		} else {
	    			continue;
	    		}
	    	}
	    }
	    for (int xr = 0; xr<xSize; xr++) {
	    	int diff = 0;
	    	// Generate landscape height
	    	if (generator.nextInt(2)==generator.nextInt(2) && diff<=0 && grassHeight<15) {
	    		grassHeight += 1;
	    		diff=1;
	    	} else if (generator.nextInt(2)==generator.nextInt(2) && diff>=0 && grassHeight>3) {
	    		grassHeight -= 1;
	    		diff=-1;
	    	}
	    	// Generate dirt layer height
	    	int dirtEnd = generator.nextInt(7)+grassHeight+2;
	    	generator = new Random(generator.nextInt(1024));
			for (int yr = 0; yr<ySize; yr++) {
	    		if (yr<grassHeight && yPos==0) { // Air layers
	    			worldMap[yr][xr] = 255;
	    		} else if (yr==grassHeight && yPos==0) { // Grass layer
	    			worldMap[yr][xr] = 3;
	    			if (generator.nextInt(4)==generator.nextInt(4) && xr%2!=0) {
	    				int height = generator.nextInt(2)+3;
	    				for (int a=1; a<height+1; a++) {
	    					worldMap[yr-a][xr] = 20;
	    				}
	    			}
	    		} else if (yPos==0 && yr>grassHeight && yr<dirtEnd) { // Dirt layer
	    			worldMap[yr][xr] = 2;
	    		} else if (generator.nextInt(10)==5 && yPos!=0) {
	    			if (yPos==map.yWorlds-1 && yr+1!=ySize) {
	    				worldMap[yr][xr] = 2;
	    			} else {
	    				worldMap[yr][xr] = 2;
	    			}
	    		} else if (generator.nextInt(32)==generator.nextInt(32)) {
	    			if (generator.nextInt(32)==generator.nextInt(32)) {
	    				worldMap[yr][xr] = ores[4]; // Diamond
	    			} else if (generator.nextInt(16)==generator.nextInt(16)) {
	    				worldMap[yr][xr] = ores[3]; // Redstone
	    			} else if (generator.nextInt(8)==generator.nextInt(8)) {
	    				worldMap[yr][xr] = ores[2]; // Gold
	    			} else if (generator.nextInt(4)==generator.nextInt(4)) {
	    				worldMap[yr][xr] = ores[1]; // Iron
	    			} else if (generator.nextInt(2)==generator.nextInt(2)) {
	    				worldMap[yr][xr] = ores[0]; // Coal
	    			} else {
	    				worldMap[yr][xr] = 1;
	    			}
				} else { // Stone layer
	    			worldMap[yr][xr] = 1;
	    		}
	    		if (yr+1==ySize && yPos==map.yWorlds-1) { // Bedrock layer
	    			worldMap[yr][xr] = 17;
	    		}
			}
		}
	    
	    System.out.println("Pre-rendering world...");
	    renderImage = new BufferedImage(xSize*25, ySize*25, BufferedImage.TYPE_INT_ARGB); //parent.createImage(xSize*25, ySize*25);
		render = (Graphics2D) renderImage.getGraphics();
		for (int yr = 0; yr<ySize; yr++) {
			for (int xr = 0; xr<xSize; xr++) {
				render.drawImage(terrainTiles[worldMap[yr][xr]], xr*25, yr*25, 25, 25, parent);
			}
		}
		
		System.out.println("Done!!");
	}

	public void remove(int x, int y, Image[] terrainTiles) {
		if (x<=xSize-1 && y<=ySize-1  && x>=0 && y>=0) {
			if (!(worldMap[y][x]==17)) {
				worldMap[y][x] = 255;
				render.drawImage(terrainTiles[worldMap[y][x]], x*25, y*25, 25, 25, parent);
			}
		}
	}
	
	public void add(int x, int y, int id, Image[] terrainTiles ) {
		if (x<=xSize-1 && y<=ySize-1 && x>=0 && y>=0) {
			if (get(y,x)==255 | get(y,x)==83) {
				worldMap[y][x] = id;
				render.drawImage(terrainTiles[worldMap[y][x]], x*25, y*25, 25, 25, parent);
			}
		}
	}
	
	public int get(int y, int x) {
		try {
			return worldMap[y][x];
		} catch (ArrayIndexOutOfBoundsException e) {
			return 17;
		}
	}
	
	public boolean isBlockTraversable(int x, int y) {
		if (get(y,x)==255 | get(y,x)==83) {
			return true;
		} else if (y<0) {
			return true;
		} else if (y>=ySize) {
			return true;
		} else {
			return false;
		}
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(renderImage, 0, 0, parent);
	}
	
	public void save(File folder, String filename) {
		BufferedImage image = new BufferedImage(xSize, ySize,
                BufferedImage.TYPE_INT_ARGB);
		Graphics2D paint = image.createGraphics();
		
		for (int yr = 0; yr<ySize; yr++) {
			for (int xr = 0; xr<xSize; xr++) {
				paint.setColor(new Color(get(yr,xr),0,0));
				paint.drawRect(xr, yr, 1, 1);
			}
		}
		
        File file = new File(folder, filename + ".png");
        try {
            ImageIO.write(image, "png", file);  // ignore returned boolean
        } catch(IOException e) {
            System.out.println("Write error for " + file.getPath() +
                               ": " + e.getMessage());
        }
		
	}
	
	public void load(File folder, String filename, Image[] terrainTiles) {
		BufferedImage image = BufferedImageUtils.loadImage(new File(folder, filename).getAbsolutePath());
		
		for (int yr = 0; yr<ySize; yr++) {
			for (int xr = 0; xr<xSize; xr++) {
				int i = image.getRGB(xr, yr);
				int r = (i >> 16) & 0xff;
//				int g = (i >> 8) & 0xff;
//				int b = (i >> 0) & 0xff; 
				worldMap[yr][xr] = r;
			}
		}
		
	    System.out.println("Pre-rendering world...");
	    renderImage = parent.createImage(xSize*25, ySize*25);
		render = (Graphics2D) renderImage.getGraphics();
		for (int yr = 0; yr<ySize; yr++) {
			for (int xr = 0; xr<xSize; xr++) {
				render.drawImage(terrainTiles[worldMap[yr][xr]], xr*25, yr*25, 25, 25, parent);
			}
		}
	}

	public boolean isSeethrough(int x, int y) {
		if (get(y,x)==255 | get(y,x)==33 | get(y,x)==83) {
			return true;
		} else {
			return false;
		}
	}
}
