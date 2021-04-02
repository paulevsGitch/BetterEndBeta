package paulevs.beb.util;

import java.util.Random;

import net.minecraft.util.Vec3i;
import net.minecraft.util.maths.MathHelper;

public class MHelper {
	public static final float PI2 = (float) (Math.PI * 2);

	public static int getSeed(int x, int z) {
		int h = x * 374761393 + z * 668265263;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}
	
	public static int getSeed(int seed, int x, int y) {
		int h = seed + x * 374761393 + y * 668265263;
		h = (h ^ (h >> 13)) * 1274126177;
		return h ^ (h >> 16);
	}
	
	public static int randRange(int min, int max, Random random) {
		return min + random.nextInt(max - min + 1);
	}
	
	public static double randRange(double min, double max, Random random) {
		return min + random.nextDouble() * (max - min);
	}

	public static float randRange(float min, float max, Random random) {
		return min + random.nextFloat() * (max - min);
	}
	
	public static Vec3i set(Vec3i res, Vec3i src) {
		res.x = src.x;
		res.y = src.y;
		res.z = src.z;
		return res;
	}
	
	public static Vec3i add(Vec3i a, Vec3i b) {
		return new Vec3i(a.x + b.x, a.y + b.y, a.z + b.z);
	}
	
	public static Vec3i subtract(Vec3i a, Vec3i b) {
		return new Vec3i(a.x - b.x, a.y - b.y, a.z - b.z);
	}
	
	public static float lengthSqr(float x, float y) {
		return x * x + y * y;
	}
	
	public static float lengthSqr(float x, float y, float z) {
		return x * x + y * y + z * z;
	}

	public static float length(float x, float y) {
		return MathHelper.sqrt(lengthSqr(x, y));
	}
	
	public static float length(float x, float y, float z) {
		return MathHelper.sqrt(lengthSqr(x, y, z));
	}
	
	public static int min(int a, int b) {
		return a < b ? a : b;
	}
	
	public static int min(int a, int b, int c) {
		return min(a, min(b, c));
	}
	
	public static int max(int a, int b) {
		return a > b ? a : b;
	}
	
	public static float min(float a, float b) {
		return a < b ? a : b;
	}
	
	public static float max(float a, float b) {
		return a > b ? a : b;
	}
	
	public static float max(float a, float b, float c) {
		return max(a, max(b, c));
	}
	
	public static int max(int a, int b, int c) {
		return max(a, max(b, c));
	}
	
	public static float dot(float x1, float y1, float z1, float x2, float y2, float z2) {
		return x1 * x2 + y1 * y2 + z1 * z2;
	}
	
	public static float dot(float x1, float y1, float x2, float y2) {
		return x1 * x2 + y1 * y2;
	}
	
	public static float clamp(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}

	public static float lerp(float delta, float start, float end) {
		return start + delta * (end - start);
	}
}
