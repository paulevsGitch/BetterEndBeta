package paulevs.beb.util.sdf.operator;

import java.util.function.Consumer;

import paulevs.beb.math.Vec3F;

public class SDFCoordModify extends SDFUnary {
	private static final Vec3F POS = new Vec3F();
	private Consumer<Vec3F> function;
	
	public SDFCoordModify setFunction(Consumer<Vec3F> function) {
		this.function = function;
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		POS.set(x, y, z);
		function.accept(POS);
		return this.source.getDistance(POS.x, POS.y, POS.z);
	}
}
