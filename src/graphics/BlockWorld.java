package graphics;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import blocks.Air;
import blocks.BlockIdentifier;
import blocks.bases.Block;
import blocks.bases.DynamicTextureBlock;
import blocks.bases.PhysicsBlock;

import utils.BufferedImageUtils;


import java.util.ArrayList;
import java.util.Random;


public class BlockWorld {
	GameWindow parent;
	Image renderImage;
	Graphics2D render;
	Block[][] worldMap;
	PhysicsThread phyThread;
	BufferedImage[] terrainTiles;
	ArrayList<DynamicTextureBlock> DTB = new ArrayList<DynamicTextureBlock>();
	int ySize, xSize;
	int[] ores;
	int px=0;
	int py=0;
	int x=0;
	int y=0;
	
	public BlockWorld(int x, int y, GameWindow owner) {
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
		
		phyThread = new PhysicsThread(this);
		
		System.out.println("Loading image tiles...");
	    terrainTiles = BufferedImageUtils.splitImage(BufferedImageUtils.loadImage(getClass().getResource("/graphics/terrain.png")), 16, 16);
	    
	    System.out.println("Initializing pre-render image...");
	    renderImage = new BufferedImage(xSize*25, ySize*25, BufferedImage.TYPE_INT_ARGB); //parent.createImage(xSize*25, ySize*25);
		render = (Graphics2D) renderImage.getGraphics();
	    
		System.out.println("Generating world...");
		System.out.println("Filling..");
    	System.out.println("Raising..");
		System.out.println("Eroding..");
		System.out.println("Growing..");
		System.out.println("Hiding..");
		System.out.println("Blocking..");
	    System.out.println("Pre-rendering world...");
	    worldMap = new Block[ySize][xSize];
		Random generator = new Random();
	    
	    int grassHeight = 10;
	    for (int xr = 0; xr<xSize; xr++) {
			for (int yr = 0; yr<ySize; yr++) {
				worldMap[yr][xr] = BlockIdentifier.toBlock(1);
			}
	    }
	    for (int xr = 0; xr<xSize; xr++) {
	    	int diff = 0;
	    	// Generate landscape height
	    	if (generator.nextInt(2)==generator.nextInt(2) && diff<=0 && grassHeight<15) {
	    		grassHeight += 1;
	    		diff=1;
	    	} else if (generator.nextInt(8)==generator.nextInt(8) && diff<=0 && grassHeight<20) {
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
	    		if (yr<grassHeight) { // Air layers
	    			worldMap[yr][xr] = BlockIdentifier.toBlock(253);
	    		} else if (yr==grassHeight) { 
	    			if (grassHeight<15) { // Grass layer
	    				worldMap[yr][xr] = BlockIdentifier.toBlock(3);
	    				if (generator.nextInt(4)==generator.nextInt(4) && xr%2!=0) { // Should we add a tree??
	    					int height = generator.nextInt(2)+3;
	    					for (int a=1; a<height+1; a++) {
	    						worldMap[yr-a][xr] = BlockIdentifier.toBlock(20);
	    						reDraw(xr,yr-a);
	    					}
	    				}
	    			} else { // Sand layer
	    				worldMap[yr][xr] = BlockIdentifier.toBlock(18);
	    			}
	    		} else if (yr>grassHeight && yr<=dirtEnd) {
    				if (grassHeight<15) {  // Dirt layer
    					worldMap[yr][xr] = BlockIdentifier.toBlock(2);
    				} else {  // Sand layer
    					worldMap[yr][xr] = BlockIdentifier.toBlock(18);
    				}
	    		} else if (generator.nextInt(32)==generator.nextInt(32)) {
	    			if (generator.nextInt(32)==generator.nextInt(32)) {
	    				worldMap[yr][xr] = BlockIdentifier.toBlock(ores[4]); // Diamond
	    			} else if (generator.nextInt(16)==generator.nextInt(16)) {
	    				worldMap[yr][xr] = BlockIdentifier.toBlock(ores[3]); // Redstone
	    			} else if (generator.nextInt(8)==generator.nextInt(8)) {
	    				worldMap[yr][xr] = BlockIdentifier.toBlock(ores[2]); // Gold
	    			} else if (generator.nextInt(4)==generator.nextInt(4)) {
	    				worldMap[yr][xr] = BlockIdentifier.toBlock(ores[1]); // Iron
	    			} else if (generator.nextInt(2)==generator.nextInt(2)) {
	    				worldMap[yr][xr] = BlockIdentifier.toBlock(ores[0]); // Coal
	    			}
	    		}
	    		if (yr==ySize-1) { // Bedrock layer
	    				worldMap[yr][xr] = BlockIdentifier.toBlock(17);
	    		}
				if (worldMap[yr][xr].hasPhysics) {
					((PhysicsBlock) worldMap[yr][xr]).onCreate(xr, yr, phyThread);
					reDraw(xr,yr);
				} else if (worldMap[yr][xr].hasDynamicTexture) {
					((DynamicTextureBlock) worldMap[yr][xr]).onCreate(xr, yr, DTB.size());
					DTB.add((DynamicTextureBlock) worldMap[yr][xr]);
				} else {
					worldMap[yr][xr].onCreate(xr, yr);
					reDraw(xr,yr);
				}
	    	}
	    }
		
		System.out.println("Starting physics..");
		phyThread.begin();
		
		System.out.println("Done!!");
	}

	public Block remove(int x, int y) {
		if (x<=xSize-1 && y<=ySize-1  && x>=0 && y>=0) {
			if (worldMap[y][x].breakable) {
				Block out = worldMap[y][x];
				worldMap[y][x].onRemove(0);
				if (out.hasDynamicTexture) {
					DTB.remove(out.dynamicBlockId);
					for (int i=out.dynamicBlockId+1; i<DTB.size(); i++) {
						((Block)DTB.get(i)).dynamicBlockId -= 1;
					}
				}
				worldMap[y][x] = BlockIdentifier.toBlock(253);
				worldMap[y][x].onCreate(x,y);
				reDraw(x,y);
				return out;
			}
		}
		return BlockIdentifier.toBlock(253);
	}
	
	public boolean add(int x, int y, Block block) {
		if (x<=xSize-1 && y<=ySize-1 && x>=0 && y>=0) {
			if (get(y,x).id==253) {
				worldMap[y][x] = block;
				if (worldMap[y][x].hasPhysics) {
					((PhysicsBlock) worldMap[y][x]).onCreate(x, y, phyThread);
					reDraw(x, y);
				} else if (worldMap[y][x].hasDynamicTexture) {
					((DynamicTextureBlock) worldMap[y][x]).onCreate(x, y, DTB.size());
					DTB.add((DynamicTextureBlock) worldMap[y][x]);
				} else {
					worldMap[y][x].onCreate(x, y);
					reDraw(x, y);
				}
				return true;
			}
		}
		return false;
	}
	
	public Block get(int y, int x) {
		try {
			return worldMap[y][x];
		} catch (ArrayIndexOutOfBoundsException e) {
			return BlockIdentifier.toBlock(17);
		}
	}
	
	public boolean isBlockTraversable(int x, int y) {
		if (get(y,x).id==253) {
			return true;
		} else {
			return false;
		}
	}
	
	public void draw(Graphics2D g) {
//		BufferedImage image = (BufferedImage) renderImage;
//		image.getSubimage(x, y, w, h);
		g.drawImage(renderImage, 0, 0, parent);
	}
	
	public void draw(Graphics2D g, Character player, int resX, int resY) {
		int w = (int) Math.floor(resX/25);
		int h = (int) Math.floor(resY/25);
		x = (int) Math.floor(player.getX()-(w/2));
		y = (int) Math.floor(player.getY()-(h/2));
		px = (int) Math.floor((w/2));
		py = (int) Math.floor((h/2));
		if (player.getX()<w/2) {
			x = 0;
			px = player.getX();
		}
		if (player.getY()<h/2) {
			y = 0;
			py = player.getY();
		}
		if (player.getX()>=xSize-w/2) {
			x = xSize-w;
			px = player.getX()-(xSize-w);
		}
		if (player.getY()>=ySize-h/2) {
			y = ySize-h;
			py = player.getY()-(ySize-h);
		}
		
		BufferedImage image = (BufferedImage) renderImage;
		for (DynamicTextureBlock block : DTB) {
			render.drawImage(block.draw(), ((Block)block).x*25, ((Block)block).y*25, 25, 25, this.parent);
		}
		g.drawImage(image.getSubimage(x*25, y*25, w*25, h*25), 0, 0, parent);
		g.drawImage(player.sprite, px*25, py*25, 25, 25, parent);
	}
	
	public void save(File folder, String filename) {
		BufferedImage image = new BufferedImage(xSize, ySize,
                BufferedImage.TYPE_INT_ARGB);
		Graphics2D paint = image.createGraphics();
		
		for (int yr = 0; yr<ySize; yr++) {
			for (int xr = 0; xr<xSize; xr++) {
				paint.setColor(new Color(get(yr,xr).id,0,0));
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
	
	public void load(File folder, String filename) {
		BufferedImage image = BufferedImageUtils.loadImage(new File(folder, filename+".png").getAbsolutePath());
		
		for (int yr = 0; yr<ySize; yr++) {
			for (int xr = 0; xr<xSize; xr++) {
				int i = image.getRGB(xr, yr);
				int r = (i >> 16) & 0xff;
//				int g = (i >> 8) & 0xff;
//				int b = (i >> 0) & 0xff; 
				worldMap[yr][xr] = BlockIdentifier.toBlock(r);
				if (worldMap[yr][xr].hasPhysics) {
					((PhysicsBlock) worldMap[yr][xr]).onCreate(xr, yr, phyThread);
				} else if (worldMap[yr][xr].hasDynamicTexture) {
					DTB.add((DynamicTextureBlock) worldMap[yr][xr]);
				} else {
					worldMap[yr][xr].onCreate(xr, yr);
				}
			}
		}
		
	    System.out.println("Pre-rendering world...");
	    renderImage = parent.createImage(xSize*25, ySize*25);
		render = (Graphics2D) renderImage.getGraphics();
		for (int yr = 0; yr<ySize; yr++) {
			for (int xr = 0; xr<xSize; xr++) {
				render.drawImage(terrainTiles[worldMap[yr][xr].id], xr*25, yr*25, 25, 25, parent);
			}
		}
		phyThread = new PhysicsThread(this);
		phyThread.begin();
	}
	
	public void reDraw(int x, int y) {
		render.drawImage(terrainTiles[worldMap[y][x].id], x*25, y*25, 25, 25, parent);
	}
	
	public void move(int xStart, int xEnd, int yStart, int yEnd) {
		Block blc = get(yStart, xStart);
		if (isBlockTraversable(xEnd, yEnd)) {
			worldMap[yEnd][xEnd] = blc;
			worldMap[yEnd][xEnd].onCreate(xEnd, yEnd);
			worldMap[yStart][xStart] = new Air();
			worldMap[yStart][xStart].onCreate(xStart, yStart);
			reDraw(xStart, yStart);
			reDraw(xEnd, yEnd);
		}
	}
	
	public void move(Block start, int x, int y) {
		move(start.x,x,start.y,y);
	}
	
	public Point mousePointsAt(Point m, Character player, int resX, int resY) {
		int w = (int) Math.floor(resX/25);
		int h = (int) Math.floor(resY/25);
		int wx = (int)Math.floor(m.x/25);
		int wy = (int)Math.floor(m.y/25);
		if (player.getX()<w/2) {
			wx += 0;
		} else if (player.getX()>=xSize-w/2) {
			wx += xSize-w;
		} else {
			wx+=x;
		}
		if (player.getY()<h/2) {
			wy += 0;
		} else if (player.getY()>=ySize-h/2) {
			wy += ySize-h;
		} else {
			wy+=y;
		}
		return new Point(wx,wy);
	}
}
