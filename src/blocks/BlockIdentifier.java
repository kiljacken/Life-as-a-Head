package blocks;

import blocks.bases.Block;

public class BlockIdentifier {
	public static Block toBlock(int id) {
		switch (id) {
			// Normal blocks + Physics blocks
			case 0: return new Grass();
			case 1: return new Stone();
			case 2: return new Dirt();
			case 3: return new GrassDirt();
			case 16: return new CobbleStone();
			case 17: return new Bedrock();
			case 18: return new Sand();
			case 20: return new Wood();
			case 32: return new Gold();
			case 33: return new Iron();
			case 34: return new Coal();
			case 50: return new Diamond();
			case 51: return new Redstone();
			case 253: return new Air();
			default: return new Air();
			// Dynamic Texture blocks
			case 256: return new DTBlockTest();
		}
	}
}
