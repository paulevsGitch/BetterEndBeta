package paulevs.beb.world.structures.placer;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.level.Level;
import net.minecraft.util.Vec3i;

public abstract class StructurePlacer {
	//private List<Vec3i> points = Lists.newArrayList();
	
	public abstract void process(Level level, Random rand, int startX, int startZ, List<Vec3i> points);
	
	public List<Vec3i> getPoints(Level level, Random rand, int startX, int startZ) {
		//points.clear();
		List<Vec3i> points = Lists.newArrayList();
		process(level, rand, startX, startZ, points);
		return points;
	}
}
