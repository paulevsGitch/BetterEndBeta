package paulevs.beb.world.structures;

import java.util.Random;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import paulevs.beb.world.structures.placer.StructurePlacer;

public class EndStructure {
	private final StructurePlacer placer;
	private final Structure structure;
	
	public EndStructure(Structure structure, StructurePlacer placer) {
		this.structure = structure;
		this.placer = placer;
	}
	
	public void generate(Level level, Random rand, int chunkX, int chunkZ) {
		placer.getPoints(level, rand, chunkX, chunkZ).forEach((pos) -> {
			structure.generate(level, rand, pos.x, pos.y, pos.z);
		});
	}
}
