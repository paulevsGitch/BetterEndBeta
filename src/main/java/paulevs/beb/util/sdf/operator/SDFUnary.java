package paulevs.beb.util.sdf.operator;

import net.minecraft.util.Vec3i;
import paulevs.beb.util.BlockState;
import paulevs.beb.util.sdf.SDF;

public abstract class SDFUnary extends SDF {
	protected SDF source;
	
	public SDFUnary setSource(SDF source) {
		this.source = source;
		return this;
	}
	
	@Override
	public BlockState getBlockState(Vec3i pos) {
		return source.getBlockState(pos);
	}
}
