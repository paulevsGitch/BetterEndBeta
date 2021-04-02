package paulevs.beb.math;

public class Vec3F {
	public static final Vec3F POS_X = new Vec3F(1, 0, 0);
	public static final Vec3F POS_Y = new Vec3F(0, 1, 0);
	public static final Vec3F POS_Z = new Vec3F(0, 0, 1);
	
	public float x;
	public float y;
	public float z;

	public Vec3F(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3F() {
		this(0, 0, 0);
	}

	public Vec3F clone() {
		return new Vec3F(x, y, z);
	}

	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
