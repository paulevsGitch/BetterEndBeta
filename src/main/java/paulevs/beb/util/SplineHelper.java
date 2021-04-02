package paulevs.beb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.level.Level;
import net.minecraft.util.Vec3i;
import net.minecraft.util.maths.MathHelper;
import paulevs.beb.math.Vec3F;
import paulevs.beb.util.sdf.SDF;
import paulevs.beb.util.sdf.operator.SDFUnion;
import paulevs.beb.util.sdf.primitive.SDFLine;

public class SplineHelper {
	public static List<Vec3F> makeSpline(float x1, float y1, float z1, float x2, float y2, float z2, int points) {
		List<Vec3F> spline = Lists.newArrayList();
		spline.add(new Vec3F(x1, y1, z1));
		int count = points - 1;
		for (int i = 1; i < count; i++) {
			float delta = (float) i / (float) count;
			float x = MHelper.lerp(delta, x1, x2);
			float y = MHelper.lerp(delta, y1, y2);
			float z = MHelper.lerp(delta, z1, z2);
			spline.add(new Vec3F(x, y, z));
		}
		spline.add(new Vec3F(x2, y2, z2));
		return spline;
	}
	
	public static void offsetParts(List<Vec3F> spline, Random random, float dx, float dy, float dz) {
		int count = spline.size();
		for (int i = 1; i < count; i++) {
			Vec3F pos = spline.get(i);
			float x = pos.x + (float) random.nextGaussian() * dx;
			float y = pos.y + (float) random.nextGaussian() * dy;
			float z = pos.z + (float) random.nextGaussian() * dz;
			pos.set(x, y, z);
		}
	}
	
	public static void powerOffset(List<Vec3F> spline, float distance, float power) {
		int count = spline.size();
		float max = count + 1;
		for (int i = 1; i < count; i++) {
			Vec3F pos = spline.get(i);
			float x = (float) i / max;
			float y = pos.y + (float) Math.pow(x, power) * distance;
			pos.set(pos.x, y, pos.z);
		}
	}
	
	public static SDF buildSDF(List<Vec3F> spline, float radius1, float radius2, Function<Vec3i, BlockState> placerFunction) {
		int count = spline.size();
		float max = count - 2;
		SDF result = null;
		Vec3F start = spline.get(0);
		for (int i = 1; i < count; i++) {
			Vec3F pos = spline.get(i);
			float delta = (float) (i - 1) / max;
			SDF line = new SDFLine()
				.setRadius(MHelper.lerp(delta, radius1, radius2))
				.setStart(start.x, start.y, start.z)
				.setEnd(pos.x, pos.y, pos.z)
				.setBlock(placerFunction);
			result = result == null ? line : new SDFUnion().setSourceA(result).setSourceB(line);
			start = pos;
		}
		return result;
	}
	
	public static SDF buildSDF(List<Vec3F> spline, Function<Float, Float> radiusFunction, Function<Vec3i, BlockState> placerFunction) {
		int count = spline.size();
		float max = count - 2;
		SDF result = null;
		Vec3F start = spline.get(0);
		for (int i = 1; i < count; i++) {
			Vec3F pos = spline.get(i);
			float delta = (float) (i - 1) / max;
			SDF line = new SDFLine()
				.setRadius(radiusFunction.apply(delta))
				.setStart(start.x, start.y, start.z)
				.setEnd(pos.x, pos.y, pos.z)
				.setBlock(placerFunction);
			result = result == null ? line : new SDFUnion().setSourceA(result).setSourceB(line);
			start = pos;
		}
		return result;
	}
	
	public static boolean fillSpline(List<Vec3F> spline, Level world, BlockState state, Vec3i pos, Function<BlockState, Boolean> replace) {
		Vec3F startPos = spline.get(0);
		for (int i = 1; i < spline.size(); i++) {
			Vec3F endPos = spline.get(i);
			if (!(fillLine(startPos, endPos, world, state, pos, replace))) {
				return false;
			}
			startPos = endPos;
		}
		
		return true;
	}
	
	public static void fillSplineForce(List<Vec3F> spline, Level world, BlockState state, Vec3i pos, Function<BlockState, Boolean> replace) {
		Vec3F startPos = spline.get(0);
		for (int i = 1; i < spline.size(); i++) {
			Vec3F endPos = spline.get(i);
			fillLineForce(startPos, endPos, world, state, pos, replace);
			startPos = endPos;
		}
	}
	
	public static boolean fillLine(Vec3F start, Vec3F end, Level level, BlockState state, Vec3i pos, Function<BlockState, Boolean> replace) {
		float dx = end.x - start.x;
		float dy = end.y - start.y;
		float dz = end.z - start.z;
		float max = MHelper.max(Math.abs(dx), Math.abs(dy), Math.abs(dz));
		int count = MathHelper.floor(max + 1);
		dx /= max;
		dy /= max;
		dz /= max;
		float x = start.x;
		float y = start.y;
		float z = start.z;
		boolean down = Math.abs(dy) > 0.2;
		
		BlockState bState = new BlockState();
		Vec3i bPos = new Vec3i();
		for (int i = 0; i < count; i++) {
			bPos.x = (int) (x + pos.x);
			bPos.y = (int) (y + pos.y);
			bPos.z = (int) (z + pos.z);
			BlockState.getState(level, bPos, bState);
			if (bState.equals(state) || replace.apply(bState)) {
				BlocksHelper.setWithoutUpdate(level, bPos, state);
				bPos.y = bPos.y - 1;
				BlockState.getState(level, bPos, bState);
				if (down && bState.equals(state) || replace.apply(bState)) {
					BlocksHelper.setWithoutUpdate(level, bPos, state);
				}
			}
			else {
				return false;
			}
			x += dx;
			y += dy;
			z += dz;
		}
		bPos.x = (int) (end.x + pos.x);
		bPos.y = (int) (end.y + pos.y);
		bPos.z = (int) (end.z + pos.z);
		BlockState.getState(level, bPos, bState);
		if (bState.equals(state) || replace.apply(bState)) {
			BlocksHelper.setWithoutUpdate(level, bPos, state);
			bPos.y = bPos.y - 1;
			BlockState.getState(level, bPos, bState);
			if (down && bState.equals(state) || replace.apply(bState)) {
				BlocksHelper.setWithoutUpdate(level, bPos, state);
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void fillLineForce(Vec3F start, Vec3F end, Level level, BlockState state, Vec3i pos, Function<BlockState, Boolean> replace) {
		float dx = end.x - start.x;
		float dy = end.y - start.y;
		float dz = end.z - start.z;
		float max = MHelper.max(Math.abs(dx), Math.abs(dy), Math.abs(dz));
		int count = MathHelper.floor(max + 1);
		dx /= max;
		dy /= max;
		dz /= max;
		float x = start.x;
		float y = start.y;
		float z = start.z;
		boolean down = Math.abs(dy) > 0.2;
		
		BlockState bState = new BlockState();
		Vec3i bPos = new Vec3i();
		for (int i = 0; i < count; i++) {
			bPos.x = (int) (x + pos.x);
			bPos.y = (int) (y + pos.y);
			bPos.z = (int) (z + pos.z);
			BlockState.getState(level, bPos, bState);
			if (replace.apply(bState)) {
				BlocksHelper.setWithoutUpdate(level, bPos, state);
				bPos.y = bPos.y - 1;
				BlockState.getState(level, bPos, bState);
				if (down && replace.apply(bState)) {
					BlocksHelper.setWithoutUpdate(level, bPos, state);
				}
			}
			x += dx;
			y += dy;
			z += dz;
		}
		bPos.x = (int) (end.x + pos.x);
		bPos.y = (int) (end.y + pos.y);
		bPos.z = (int) (end.z + pos.z);
		BlockState.getState(level, bPos, bState);
		if (replace.apply(bState)) {
			BlocksHelper.setWithoutUpdate(level, bPos, state);
			bPos.y = bPos.y - 1;
			BlockState.getState(level, bPos, bState);
			if (down && replace.apply(bState)) {
				BlocksHelper.setWithoutUpdate(level, bPos, state);
			}
		}
	}
	
	public static boolean canGenerate(List<Vec3F> spline, float scale, Vec3i start, Level level, Function<BlockState, Boolean> canReplace) {
		int count = spline.size();
		Vec3F vec = spline.get(0);
		Vec3i mut = new Vec3i();
		float x1 = start.x + vec.x * scale;
		float y1 = start.y + vec.y * scale;
		float z1 = start.z + vec.z * scale;
		for (int i = 1; i < count; i++) {
			vec = spline.get(i);
			float x2 = start.x + vec.x * scale;
			float y2 = start.y + vec.y * scale;
			float z2 = start.z + vec.z * scale;
			
			for (float py = y1; py < y2; py += 3) {
				if (py - start.y < 10) continue;
				float lerp = (py - y1) / (y2 - y1);
				float x = MHelper.lerp(lerp, x1, x2);
				float z = MHelper.lerp(lerp, z1, z2);
				mut.x = (int) x;
				mut.y = (int) py;
				mut.z = (int) z;
				if (!canReplace.apply(BlockState.getState(level, mut))) {
					return false;
				}
			}
			
			x1 = x2;
			y1 = y2;
			z1 = z2;
		}
		return true;
	}
	
	public static boolean canGenerate(List<Vec3F> spline, Vec3i start, Level level, Function<BlockState, Boolean> canReplace) {
		int count = spline.size();
		Vec3F vec = spline.get(0);
		Vec3i mut = new Vec3i();
		float x1 = start.x + vec.x;
		float y1 = start.y + vec.y;
		float z1 = start.z + vec.z;
		for (int i = 1; i < count; i++) {
			vec = spline.get(i);
			float x2 = start.x + vec.x;
			float y2 = start.y + vec.y;
			float z2 = start.z + vec.z;
			
			for (float py = y1; py < y2; py += 3) {
				if (py - start.y < 10) continue;
				float lerp = (py - y1) / (y2 - y1);
				float x = MHelper.lerp(lerp, x1, x2);
				float z = MHelper.lerp(lerp, z1, z2);
				mut.x = (int) x;
				mut.y = (int) py;
				mut.z = (int) z;
				if (!canReplace.apply(BlockState.getState(level, mut))) {
					return false;
				}
			}
			
			x1 = x2;
			y1 = y2;
			z1 = z2;
		}
		return true;
	}
	
	public static Vec3F getPos(List<Vec3F> spline, float index) {
		int i = (int) index;
		int last = spline.size() - 1;
		if (i >= last) {
			return spline.get(last);
		}
		float delta = index - i;
		Vec3F p1 = spline.get(i);
		Vec3F p2 = spline.get(i + 1);
		float x = MHelper.lerp(delta, p1.x, p2.x);
		float y = MHelper.lerp(delta, p1.y, p2.y);
		float z = MHelper.lerp(delta, p1.z, p2.z);
		return new Vec3F(x, y, z);
	}
	
	public static void rotateSpline(List<Vec3F> spline, float angle) {
		for (Vec3F v: spline) {
			float sin = (float) Math.sin(angle);
			float cos = (float) Math.cos(angle);
			float x = v.x * cos + v.z * sin;
			float z = v.x * sin + v.z * cos;
			v.set(x, v.y, z);
		}
	}
	
	public static List<Vec3F> copySpline(List<Vec3F> spline) {
		List<Vec3F> result = new ArrayList<Vec3F>(spline.size());
		for (Vec3F v: spline) {
			result.add(new Vec3F(v.x, v.y, v.z));
		}
		return result;
	}
	
	public static void scale(List<Vec3F> spline, float scale) {
		scale(spline, scale, scale, scale);
	}
	
	public static void scale(List<Vec3F> spline, float x, float y, float z) {
		for (Vec3F v: spline) {
			v.set(v.x * x, v.y * y, v.z * z);
		}
	}
	
	public static void offset(List<Vec3F> spline, Vec3F offset) {
		for (Vec3F v: spline) {
			v.set(offset.x + v.x, offset.y + v.y, offset.z + v.z);
		}
	}
}
