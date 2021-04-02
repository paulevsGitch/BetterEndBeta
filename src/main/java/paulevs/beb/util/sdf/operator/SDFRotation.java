package paulevs.beb.util.sdf.operator;

import paulevs.beb.math.Matrix3F;
import paulevs.beb.math.Vec3F;

public class SDFRotation extends SDFUnary {
	private static final Vec3F POS = new Vec3F();
	private Matrix3F rotation;
	
	public SDFRotation setRotation(Vec3F axis, float rotationAngle) {
		rotation = Matrix3F.makeRotation(axis, rotationAngle);
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		POS.set(x, y, z);
		rotation.multiple(POS);
		return source.getDistance(POS.x, POS.y, POS.z);
	}
}
