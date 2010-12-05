package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jnbt.*;

import utils.BufferedImageUtils;

import blocks.Air;
import blocks.BlockIdentifier;
import blocks.bases.Block;
import blocks.bases.DynamicTextureBlock;

public class ItemBar {
	Block[] items = new Block[9];
	int[] itemAmounts = new int[9];
	BufferedImage[] guiImages = BufferedImageUtils.splitImage(BufferedImageUtils.loadImage(getClass().getResource("/ui/gui.png")), 2, 1);
	BufferedImage[] terrainTiles = BufferedImageUtils.splitImage(BufferedImageUtils.loadImage(getClass().getResource("/graphics/terrain.png")), 16, 16);
	public int selected = 0;
	Object dad;
	
	public ItemBar(Object parent) {
		dad=parent;
		for (int i=0; i<9; i++) {
			items[i] = new Air();
			itemAmounts[i] = 0;
		}
	}
	
	public void addBlock(Block block, int amount) {
		for (int i=0; i<9; i++) {
			if (items[i].id==255 || itemAmounts[i] == 0 || items[i].id==block.id) {
				items[i]=block;
				if (amount+itemAmounts[i]<=block.max) {
					itemAmounts[i] = amount+itemAmounts[i];
				} else {
					itemAmounts[i] = block.max;
				}
				break;
			}
		}
	}
	
	public Block removeBlock(int where, int amount) {
//		for (int i=0; i<9; i++) {
//			if (items[i].id==block.id) {
//				if (amount<=itemAmounts[i]) {
//					itemAmounts[i] -= amount;
//					if (itemAmounts[i]==0) {
//						items[i] = new Air();
//					}
//				} else {
//					itemAmounts[i] = block.max;
//				}
//			}
//		}
		Block block = items[where];
		if (amount<=itemAmounts[where]) {
			itemAmounts[where] -= amount;
			if (itemAmounts[where]==0) {
				items[where] = new Air();
			}
		}
		return block;
	}
	
	public void draw(Graphics2D g, int x, int y) {
		for (int i=0; i<9; i++) {
			if (selected==i) {
				g.drawImage(guiImages[1], x+(i*80+40), y, 80, 80, (ImageObserver) dad);
			} else {
				g.drawImage(guiImages[0], x+(i*80+40), y, 80, 80, (ImageObserver) dad);
			}
			if (items[i].hasDynamicTexture) {
				g.drawImage(((DynamicTextureBlock)items[i]).draw(), x+(i*80+40)+8, y+8, 64, 64, (ImageObserver) dad);
			} else {
				g.drawImage(terrainTiles[items[i].id], x+(i*80+40)+8, y+8, 64, 64, (ImageObserver) dad);
			}
			if (itemAmounts[i] > 0) {
				g.setColor(Color.black);
				g.drawString(Integer.toString(itemAmounts[i]), x+(i*80+40)+8, y+8+10);
			}
		}
	}
	
	public void select(int which) {
		if (which < 9 && which > -1) {
			selected = which;
		}
	}
	
	public void save(String filename) {
		try {
			NBTOutputStream out = new NBTOutputStream(new FileOutputStream(filename));
			HashMap<String, Tag> data = new HashMap<String, Tag>();
			for (int i=0; i<9; i++) {
				data.put("id"+i, new IntTag("id"+i, this.items[i].id));
				data.put("amount"+i, new IntTag("amount"+i, itemAmounts[i]));
			}
			out.writeTag(new CompoundTag("inventory", data));
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void load(String filename) {
		try {
			NBTInputStream in = new NBTInputStream(new FileInputStream(filename));
			Tag tag = in.readTag();
			System.out.println(tag.getValue());
			System.out.println(tag.getName());
			@SuppressWarnings("unchecked")
			Map<String, Tag> data = (Map<String, Tag>) tag.getValue();
			for (int i=0; i<9; i++) {
				items[i] = BlockIdentifier.toBlock((Integer) data.get("id"+i).getValue());
				itemAmounts[i] = (Integer) data.get("amount"+i).getValue();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
