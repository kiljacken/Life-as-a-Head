package graphics;

import java.util.ArrayList;

import blocks.bases.Block;
import blocks.bases.PhysicsBlock;

public class PhysicsThread implements Runnable {
	Thread t;
	BlockWorld world;
	ArrayList<PhysicsBlock> physicsBlocks;
	
	public PhysicsThread(BlockWorld world) {
		this.world = world;
		this.physicsBlocks = new ArrayList<PhysicsBlock>();
		
		// Initialize our thread
		this.t = new Thread(this);
	}
	
	public void begin() {this.t.start();}

	@Override
	public void run() {
		// Run 1 iteration of the physics loop
		while(true)
		{
			for (PhysicsBlock block : physicsBlocks) {
				try {
					block.onTick(world);
				} catch (Throwable e) {}
			}
			try {
				Thread.sleep(1000/5);
			} catch (InterruptedException e) {}
		}
	}

	public int register(PhysicsBlock block) {
		this.physicsBlocks.add(block);
		return physicsBlocks.size()-1;
	}
	
	public void unregister(int id) {
		this.physicsBlocks.remove(id);
		for (int i=id+1; i<this.physicsBlocks.size(); i++) {
			((Block)this.physicsBlocks.get(i)).dynamicBlockId -= 1;
		}
	}
	
}
