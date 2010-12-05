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
		outG.drawString(Integer.toString(generator.nextInt(10)), 15,15);
		return out;
	}

	@Override
	public void onCreate(int x, int y, int id) {
		this.x=x;
		this.y=y;
		this.dynamicBlockId=id;
	}

}
