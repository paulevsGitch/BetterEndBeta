package paulevs.beb.world.structures.placer;

import java.util.List;
import java.util.Random;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.Vec3i;

public class RandomStructureScatter extends StructurePlacer {
	private final int maxCount;
	
	public RandomStructureScatter(int maxCount) {
		this.maxCount = maxCount;
	}
	
	@Override
	public void process(Level level, Random rand, int startX, int startZ, List<Vec3i> points) {
		Chunk chunk = level.getChunk(startX, startZ);
		int count = rand.nextInt(maxCount);
		for (int i = 0; i < count; i++) {
			int x = rand.nextInt(16);
			int z = rand.nextInt(16);
			int y = chunk.getHeight(x, z);
			if (y > 0) {
				points.add(new Vec3i(x | startX, y, z | startZ));
			}
		}
	}
}
