package blocks.bases;

import graphics.BlockWorld;
import graphics.PhysicsThread;

public interface PhysicsBlock {
	public void onCreate(int x, int y, PhysicsThread physThread);
	public void onTick(BlockWorld world);
	public void onRemove(int tool);
}