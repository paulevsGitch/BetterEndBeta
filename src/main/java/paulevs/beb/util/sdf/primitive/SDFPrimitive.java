package paulevs.beb.util.sdf.primitive;

import java.util.function.Function;

import net.minecraft.block.BlockBase;
import net.minecraft.util.Vec3i;
import paulevs.beb.util.BlockState;
import paulevs.beb.util.sdf.SDF;

public abstract class SDFPrimitive extends SDF {
	protected Function<Vec3i, BlockState> placerFunction;
	
	public SDFPrimitive setBlock(Function<Vec3i, BlockState> placerFunction) {
		this.placerFunction = placerFunction;
		return this;
	}
	
	public SDFPrimitive setBlock(BlockState state) {
		this.placerFunction = (pos) -> {
			return state;
		};
		return this;
	}
	
	public SDFPrimitive setBlock(BlockBase block) {
		BlockState state = new BlockState(block);
		this.placerFunction = (pos) -> {
			return state;
		};
		return this;
	}
	
	public BlockState getBlockState(Vec3i pos) {
		return placerFunction.apply(pos);
	}
}
