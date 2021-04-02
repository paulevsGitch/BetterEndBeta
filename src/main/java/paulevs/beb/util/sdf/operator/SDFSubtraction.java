package paulevs.beb.util.sdf.operator;

import paulevs.beb.util.MHelper;

public class SDFSubtraction extends SDFBinary {
	@Override
	public float getDistance(float x, float y, float z) {
		float a = this.sourceA.getDistance(x, y, z);
		float b = this.sourceB.getDistance(x, y, z);
		this.selectValue(a, b);
		return MHelper.max(a, -b);
	}
}
