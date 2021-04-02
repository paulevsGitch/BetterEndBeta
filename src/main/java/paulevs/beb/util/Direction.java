package paulevs.beb.util;

import net.minecraft.util.Vec3i;

public enum Direction {
	UP(0, 1, 0),
	DOWN(0, -1, 0),
	NORTH(0, 0, -1),
	SOUTH(0, 0, 1),
	EAST(1, 0, 0),
	WEST(-1, 0, 0);
	
	public static final Direction[] HORIZONTAL = new Direction[] {NORTH, EAST, SOUTH, WEST};
	public static final Direction[] VALUES = values();
	
	final int x;
	final int y;
	final int z;
	
	Direction(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3i move(Vec3i vec) {
		vec.x += x;
		vec.y += y;
		vec.z += z;
		return vec;
	}
	
	public Vec3i move(Vec3i vec, int dist) {
		vec.x += x * dist;
		vec.y += y * dist;
		vec.z += z * dist;
		return vec;
	}
	
	public Vec3i offset(Vec3i vec) {
		return new Vec3i(vec.x + x, vec.y + y, vec.z + z);
	}
	
	public Vec3i offset(Vec3i vec, int dist) {
		return new Vec3i(vec.x + x * dist, vec.y + y * dist, vec.z + z * dist);
	}
}
