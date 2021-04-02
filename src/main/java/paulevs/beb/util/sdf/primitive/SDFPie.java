package paulevs.beb.util.sdf.primitive;

import paulevs.beb.util.MHelper;

public class SDFPie extends SDFPrimitive {
	private float sin;
	private float cos;
	private float radius;
	
	public SDFPie setAngle(float angle) {
		this.sin = (float) Math.sin(angle);
		this.cos = (float) Math.cos(angle);
		return this;
	}
	
	public SDFPie setRadius(float radius) {
		this.radius = radius;
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		float px = Math.abs(x);
	    float l = MHelper.length(px, y, z) - radius;
	    float m = MHelper.dot(px, z, sin, cos);
	    m = MHelper.clamp(m, 0, radius);
		m = MHelper.length(px - sin * m, z - cos * m);
	    return MHelper.max(l, m * (float) Math.signum(cos * px - sin * z));
	}
}
