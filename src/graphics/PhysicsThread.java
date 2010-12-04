package graphics;

import java.util.ArrayList;

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
			for (int i=0; i<physicsBlocks.size(); i++) {
				try {
					this.physicsBlocks.get(i).onTick(world);
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
	}
	
}
