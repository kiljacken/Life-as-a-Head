package blocks;

import graphics.BlockWorld;
import graphics.PhysicsThread;
import blocks.bases.Block;
import blocks.bases.PhysicsBlock;

public class Sand extends Block implements PhysicsBlock {
	PhysicsThread physThread;
	public Sand() {
		super();
		max=64;
		id=18;
		dropId=18;
		hasPhysics=true;
		physicsId=0;
	}

	@Override
	public void onTick(BlockWorld world) {
		world.move(this, x, y+1);
	}

	@Override
	public void onCreate(int x, int y, PhysicsThread physThread) {
		this.x = x;
		this.y = y;
		this.physThread = physThread; 
		this.physicsId = physThread.register(this);
	}
	
	@Override
	public void onRemove(int tool){
		this.physThread.unregister(physicsId);
	}
}