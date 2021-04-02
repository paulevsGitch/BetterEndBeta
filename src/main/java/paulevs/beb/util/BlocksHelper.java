package paulevs.beb.util;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.util.Vec3i;
import paulevs.beb.block.EndTerrainBlock;
import paulevs.beb.registry.EndBlocks;

public class BlocksHelper {
	public static boolean isTerrain(BlockBase block) {
		return block instanceof EndTerrainBlock || block == EndBlocks.getDefaultBlock();
	}
	
	public static boolean isNonSolid(int tile) {
		return tile == 0 || BlockBase.BY_ID[tile] == null || BlockBase.BY_ID[tile].material.isReplaceable();
	}
	
	public static boolean isNonSolidNoLava(int tile) {
		return tile != BlockBase.STILL_LAVA.id && tile != BlockBase.FLOWING_LAVA.id && isNonSolid(tile);
	}
	
	public static void setWithoutUpdate(Level level, Vec3i pos, BlockState state) {
		level.setTileWithMetadata(pos.x, pos.y, pos.z, state.getBlockID(), state.getBlockMeta());
	}
}
