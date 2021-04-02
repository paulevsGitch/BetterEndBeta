package paulevs.beb.util.sdf.operator;

import java.util.function.Function;

import paulevs.beb.math.Vec3F;

public class SDFDisplacement extends SDFUnary {
	private static final Vec3F POS = new Vec3F();
	private Function<Vec3F, Float> displace;
	
	public SDFDisplacement setFunction(Function<Vec3F, Float> displace) {
		this.displace = displace;
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		POS.set(x, y, z);
		return this.source.getDistance(x, y, z) + displace.apply(POS);
	}
}
