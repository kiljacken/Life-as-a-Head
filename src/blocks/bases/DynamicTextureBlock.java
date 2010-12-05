package blocks.bases;

import java.awt.Image;

public interface DynamicTextureBlock {
	public void onCreate(int x, int y, int id);
	public Image draw();
}
