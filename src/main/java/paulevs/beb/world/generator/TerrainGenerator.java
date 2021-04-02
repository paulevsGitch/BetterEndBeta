package paulevs.beb.world.generator;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.util.maths.MathHelper;
import paulevs.beb.noise.OpenSimplexNoise;
import paulevs.beb.util.MHelper;

public class TerrainGenerator {
	private static final float SCALE = 8.0F;
	private static final int SIDE_XZ = (int) (16 / SCALE + 1);
	private static final int SIDE_MAX = SIDE_XZ - 1;
	private static final int SIDE_Y = (int) (128 / SCALE + 1);
	private static final ReentrantLock LOCKER = new ReentrantLock();
	private static final float[][][] DENSITY = new float[SIDE_XZ][SIDE_XZ][SIDE_Y];
	
	private IslandLayer largeIslands;
	private IslandLayer mediumIslands;
	private IslandLayer smallIslands;
	private OpenSimplexNoise noise1;
	private OpenSimplexNoise noise2;
	
	public TerrainGenerator(long seed) {
		Random random = new Random(seed);
		largeIslands = new IslandLayer(random.nextInt(), 300, 200, 70, 10, false);
		mediumIslands = new IslandLayer(random.nextInt(), 150, 100, 70, 20, true);
		smallIslands = new IslandLayer(random.nextInt(), 60, 50, 70, 30, false);
		noise1 = new OpenSimplexNoise(random.nextInt());
		noise2 = new OpenSimplexNoise(random.nextInt());
	}
	
	public void fillTerrainDensity(int cx, int cz) {
		LOCKER.lock();
		
		largeIslands.clearCache();
		mediumIslands.clearCache();
		smallIslands.clearCache();
		
		for (int px = 0; px < SIDE_XZ; px++) {
			double fillX = (double) px / SIDE_MAX + cx;
			for (int pz = 0; pz < SIDE_XZ; pz++) {
				double fillZ = (double) pz / SIDE_MAX + cz;
				fillTerrainDensity(DENSITY[px][pz], fillX, fillZ);
			}
		}
		
		LOCKER.unlock();
	}
	
	public float getDensity(int x, int y, int z) {
		float dx = x / SCALE;
		float dy = y / SCALE;
		float dz = z / SCALE;
		int x1 = (int) dx;
		int y1 = (int) dy;
		int z1 = (int) dz;
		int x2 = x1 + 1;
		int y2 = y1 + 1;
		int z2 = z1 + 1;
		dx -= x1;
		dy -= y1;
		dz -= z1;
		
		float v1 = DENSITY[x1][z1][y1];
		float v2 = DENSITY[x2][z1][y1];
		float v3 = DENSITY[x1][z2][y1];
		float v4 = DENSITY[x2][z2][y1];
		
		float v5 = DENSITY[x1][z1][y2];
		float v6 = DENSITY[x2][z1][y2];
		float v7 = DENSITY[x1][z2][y2];
		float v8 = DENSITY[x2][z2][y2];
		
		v1 = MHelper.lerp(dx, v1, v2);
		v3 = MHelper.lerp(dx, v3, v4);
		v5 = MHelper.lerp(dx, v5, v6);
		v7 = MHelper.lerp(dx, v7, v8);
		
		v1 = MHelper.lerp(dz, v1, v3);
		v2 = MHelper.lerp(dz, v5, v7);
		
		return MHelper.lerp(dy, v1, v2);
	}
	
	private void fillTerrainDensity(float[] buffer, double x, double z) {
		double distortion1 = noise1.eval(x * 0.1, z * 0.1) * 20 + noise2.eval(x * 0.2, z * 0.2) * 10 + noise1.eval(x * 0.4, z * 0.4) * 5;
		double distortion2 = noise2.eval(x * 0.1, z * 0.1) * 20 + noise1.eval(x * 0.2, z * 0.2) * 10 + noise2.eval(x * 0.4, z * 0.4) * 5;
		double px = (double) x * SCALE + distortion1;
		double pz = (double) z * SCALE + distortion2;
		
		largeIslands.updatePositions(px, pz);
		mediumIslands.updatePositions(px, pz);
		smallIslands.updatePositions(px, pz);
		
		for (int y = 0; y < buffer.length; y++) {
			double py = (double) y * SCALE;
			float dist = largeIslands.getDensity(px, py, pz);
			dist = dist > 1 ? dist : MHelper.max(dist, mediumIslands.getDensity(px, py, pz));
			dist = dist > 1 ? dist : MHelper.max(dist, smallIslands.getDensity(px, py, pz));
			if (dist > -0.5F) {
				dist += noise1.eval(px * 0.01, py * 0.01, pz * 0.01) * 0.02 + 0.02;
				dist += noise2.eval(px * 0.05, py * 0.05, pz * 0.05) * 0.01 + 0.01;
				dist += noise1.eval(px * 0.1, py * 0.1, pz * 0.1) * 0.005 + 0.005;
			}
			buffer[y] = dist;
		}
	}
	
	/**
	 * Check if this is land
	 * @param x - biome pos x
	 * @param z - biome pos z
	 */
	public boolean isLand(int x, int z) {
		LOCKER.lock();
		
		double px = (x >> 1) + 0.5;
		double pz = (z >> 1) + 0.5;
		
		double distortion1 = noise1.eval(px * 0.1, pz * 0.1) * 20 + noise2.eval(px * 0.2, pz * 0.2) * 10 + noise1.eval(px * 0.4, pz * 0.4) * 5;
		double distortion2 = noise2.eval(px * 0.1, pz * 0.1) * 20 + noise1.eval(px * 0.2, pz * 0.2) * 10 + noise2.eval(px * 0.4, pz * 0.4) * 5;
		px = px * SCALE + distortion1;
		pz = pz * SCALE + distortion2;
		
		largeIslands.updatePositions(px, pz);
		mediumIslands.updatePositions(px, pz);
		smallIslands.updatePositions(px, pz);
		
		for (int y = 0; y < 32; y++) {
			double py = (double) y * SCALE;
			float dist = largeIslands.getDensity(px, py, pz);
			dist = dist > 1 ? dist : MHelper.max(dist, mediumIslands.getDensity(px, py, pz));
			dist = dist > 1 ? dist : MHelper.max(dist, smallIslands.getDensity(px, py, pz));
			if (dist > -0.5F) {
				dist += noise1.eval(px * 0.01, py * 0.01, pz * 0.01) * 0.02 + 0.02;
				dist += noise2.eval(px * 0.05, py * 0.05, pz * 0.05) * 0.01 + 0.01;
				dist += noise1.eval(px * 0.1, py * 0.1, pz * 0.1) * 0.005 + 0.005;
			}
			if (dist > -0.01) {
				LOCKER.unlock();
				return true;
			}
		}
		
		LOCKER.unlock();
		return false;
	}
	
	/**
	 * Get something like height
	 * @param x - block pos x
	 * @param z - block pos z
	 */
	public int getHeight(int x, int z) {
		LOCKER.lock();
		
		double px = (double) x / 8.0;
		double pz = (double) z / 8.0;
		
		double distortion1 = noise1.eval(px * 0.1, pz * 0.1) * 20 + noise2.eval(px * 0.2, pz * 0.2) * 10 + noise1.eval(px * 0.4, pz * 0.4) * 5;
		double distortion2 = noise2.eval(px * 0.1, pz * 0.1) * 20 + noise1.eval(px * 0.2, pz * 0.2) * 10 + noise2.eval(px * 0.4, pz * 0.4) * 5;
		px = (double) x * SCALE + distortion1;
		pz = (double) z * SCALE + distortion2;
		
		largeIslands.updatePositions(px, pz);
		mediumIslands.updatePositions(px, pz);
		smallIslands.updatePositions(px, pz);
		
		for (int y = 32; y >= 0; y--) {
			double py = (double) y * SCALE;
			float dist = largeIslands.getDensity(px, py, pz);
			dist = dist > 1 ? dist : MHelper.max(dist, mediumIslands.getDensity(px, py, pz));
			dist = dist > 1 ? dist : MHelper.max(dist, smallIslands.getDensity(px, py, pz));
			if (dist > -0.5F) {
				dist += noise1.eval(px * 0.01, py * 0.01, pz * 0.01) * 0.02 + 0.02;
				dist += noise2.eval(px * 0.05, py * 0.05, pz * 0.05) * 0.01 + 0.01;
				dist += noise1.eval(px * 0.1, py * 0.1, pz * 0.1) * 0.005 + 0.005;
			}
			if (dist > 0) {
				LOCKER.unlock();
				return MathHelper.floor(MHelper.clamp(y + dist, y, y + 1) * SCALE);
			}
		}
		
		LOCKER.unlock();
		return 0;
	}
}
