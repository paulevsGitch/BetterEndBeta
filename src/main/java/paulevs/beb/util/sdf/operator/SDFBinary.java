package paulevs.beb.util.sdf.operator;

import net.minecraft.util.Vec3i;
import paulevs.beb.util.BlockState;
import paulevs.beb.util.sdf.SDF;

public abstract class SDFBinary extends SDF {
	protected SDF sourceA;
	protected SDF sourceB;
	protected boolean firstValue;
	
	public SDFBinary setSourceA(SDF sourceA) {
		this.sourceA = sourceA;
		return this;
	}
	
	public SDFBinary setSourceB(SDF sourceB) {
		this.sourceB = sourceB;
		return this;
	}
	
	protected void selectValue(float a, float b) {
		firstValue = a < b;
	}
	
	@Override
	public BlockState getBlockState(Vec3i pos) {
		if (firstValue) {
			return sourceA.getBlockState(pos);
		} else {
			return sourceB.getBlockState(pos);
		}
	}
}
