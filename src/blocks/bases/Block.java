package blocks.bases;

public class Block {
	public int id=255; // Id of the block
	public int dropId=255; // Id of the dropped block
	
	public int max=64; // Max amount of this block in one stack
	
	public boolean breakable=true; // Is the block breakable?

	public int x=-1,y=-1; // The position of the block
	
	public boolean hasPhysics=false; // Block has physics?
	public int physicsId=-1; // Blocks physics id
	
	public boolean hasDynamicTexture=false; // Block has dynamic texture
	public int dynamicBlockId=-1; // Dynamic block id
	
	public void onCreate(int xPos, int yPos) {x=xPos; y=yPos;}; // Function called when the block is placed
	public void onRemove(int tool) {}; // Function called when the block is removed
	public void onBurned() {}; // Function called when the block is burned
	public void onUse(int tool) {}; // Function called when the black is used (Player right clicks on it)
}