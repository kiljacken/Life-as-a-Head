package blocks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import blocks.bases.Block;
import blocks.bases.DynamicTextureBlock;

public class DTBlockTest extends Block implements DynamicTextureBlock {
	Random generator = new Random();

	public DTBlockTest() {
		super();
		hasDynamicTexture = true;
		id = 256;
		dropId = 256;
		
	}

	@Override
	public Image draw() {
		BufferedImage out = new BufferedImage(25, 25, BufferedImage.TYPE_INT_RGB);
		Graphics outG = out.getGraphics();
		outG.setColor(Color.red);
		int r = generator.nextInt(256);
		int g = generator.nextInt(256);
		int b = generator.nextInt(256);
		outG.setColor(new Color(r, g, b));
		outG.drawRect(0, 0, 24, 24);
		outG.drawRect(1, 1, 22, 22);
		outG.drawRect(2, 2, 20, 20);
		outG.drawRect(3, 3, 18, 18);
		outG.drawRect(4, 4, 16, 16);
		outG.drawRect(5, 5, 14, 14);
		outG.drawRect(6, 6, 12, 12);
		outG.drawRect(7, 7, 10, 10);
		outG.drawRect(8, 8, 8, 8);
		return out;
	}

	@Override
	public void onCreate(int x, int y, int id) {
		this.x=x;
		this.y=y;
		this.dynamicBlockId=id;
	}

}
