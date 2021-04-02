package paulevs.beb.world.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.util.Vec3i;
import net.minecraft.util.maths.MathHelper;
import paulevs.beb.noise.OpenSimplexNoise;
import paulevs.beb.util.MHelper;
import paulevs.beb.util.sdf.SDF;
import paulevs.beb.util.sdf.operator.SDFScale;
import paulevs.beb.util.sdf.operator.SDFSmoothUnion;
import paulevs.beb.util.sdf.operator.SDFTranslate;
import paulevs.beb.util.sdf.primitive.SDFCappedCone;

public class IslandLayer {
	private static final Random RANDOM = new Random();
	private static final SDF ISLAND;
	
	private final List<Vec3i> positions = new ArrayList<Vec3i>(9);
	private final Map<Vec3i, SDF> islands = Maps.newHashMap();
	private final OpenSimplexNoise density;
	private final double distance;
	private final float scale;
	private final int seed;
	private final int minY;
	private final int maxY;
	private int lastX = Integer.MIN_VALUE;
	private int lastZ = Integer.MIN_VALUE;
	
	public IslandLayer(int seed, double distance, float scale, int center, int heightVariation, boolean hasCentralIsland) {
		this.distance = distance;
		this.density = new OpenSimplexNoise(seed);
		this.scale = scale;
		this.seed = seed;
		this.minY = center - heightVariation;
		this.maxY = center + heightVariation;
	}
	
	private int getSeed(int x, int z) {
		int h = seed + x * 374761393 + z * 668265263;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}
	
	public void updatePositions(double x, double z) {
		int ix = MathHelper.floor(x / distance);
		int iz = MathHelper.floor(z / distance);
		if (lastX != ix || lastZ != iz) {
			lastX = ix;
			lastZ = iz;
			positions.clear();
			for (int pox = -1; pox < 2; pox++) {
				int px = pox + ix;
				for (int poz = -1; poz < 2; poz++) {
					int pz = poz + iz;
					RANDOM.setSeed(getSeed(px, pz));
					double posX = (px + RANDOM.nextFloat()) * distance;
					double posY = MHelper.randRange(minY, maxY, RANDOM);
					double posZ = (pz + RANDOM.nextFloat()) * distance;
					if (density.eval(posX * 0.01, posZ * 0.01) > 0) {
						positions.add(new Vec3i((int) posX, (int) posY, (int) posZ));
					}
				}
			}
		}
	}
	
	private SDF getIsland(Vec3i pos) {
		SDF island = islands.get(pos);
		if (island == null) {
			if (pos.x == 0 && pos.z == 0) {
				island = new SDFScale().setScale(1.3F).setSource(ISLAND);
			}
			else {
				RANDOM.setSeed(getSeed(pos.x, pos.z));
				island = new SDFScale().setScale(RANDOM.nextFloat() + 0.5F).setSource(ISLAND);
			}
			islands.put(pos, island);
		}
		return island;
	}
	
	private float getRelativeDistance(SDF sdf, Vec3i center, double px, double py, double pz) {
		float x = (float) (px - center.x) / scale;
		float y = (float) (py - center.y) / scale;
		float z = (float) (pz - center.z) / scale;
		return sdf.getDistance(x, y, z);
	}
	
	private float calculateSDF(double x, double y, double z) {
		float distance = 10;
		for (Vec3i pos: positions) {
			SDF island = getIsland(pos);
			float dist = getRelativeDistance(island, pos, x, y, z);
			distance = MHelper.min(distance, dist);
		}
		return distance;
	}
	
	public float getDensity(double x, double y, double z) {
		return -calculateSDF(x, y, z);
	}
	
	public void clearCache() {
		if (islands.size() > 128) {
			islands.clear();
		}
	}
	
	private static SDF makeCone(float radiusBottom, float radiusTop, float height, float minY) {
		float hh = height * 0.5F;
		SDF sdf = new SDFCappedCone().setHeight(hh).setRadius1(radiusBottom).setRadius2(radiusTop);
		return new SDFTranslate().setTranslate(0, minY + hh, 0).setSource(sdf);
	}
	
	static {
		SDF cone1 = makeCone(0, 0.4F, 0.2F, -0.3F);
		SDF cone2 = makeCone(0.4F, 0.5F, 0.1F, -0.1F);
		SDF cone3 = makeCone(0.5F, 0.45F, 0.03F, 0.0F);
		SDF cone4 = makeCone(0.45F, 0, 0.02F, 0.03F);
		
		SDF coneBottom = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone1).setSourceB(cone2);
		SDF coneTop = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone3).setSourceB(cone4);
		
		ISLAND = new SDFSmoothUnion().setRadius(0.01F).setSourceA(coneTop).setSourceB(coneBottom);
	}
}
