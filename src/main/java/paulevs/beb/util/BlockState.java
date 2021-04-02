package paulevs.beb.util;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.util.Vec3i;

public class BlockState {
	private byte blockID;
	private byte blockMeta;
	
	public BlockState() {
		this(0, 0);
	}
	
	public BlockState(BlockBase block) {
		this(block.id, 0);
	}
	
	public BlockState(BlockBase block, int meta) {
		this(block.id, meta);
	}
	
	public BlockState(int id, int meta) {
		this.blockID = (byte) id;
		this.blockMeta = (byte) meta;
	}
	
	public void setBlockID(int id) {
		blockID = (byte) id;
	}
	
	public byte getBlockID() {
		return blockID;
	}
	
	public void setBlockMeta(int meta) {
		blockMeta = (byte) meta;
	}
	
	public byte getBlockMeta() {
		return blockMeta;
	}
	
	public BlockBase getBlock() {
		return BlockBase.BY_ID[blockID];
	}
	
	public Material getMaterial() {
		BlockBase block = getBlock();
		return block == null ? Material.AIR : block.material;
	}
	
	public static BlockState getState(Level level, Vec3i pos) {
		int id = level.getTileId(pos.x, pos.y, pos.z);
		int meta = level.getTileMeta(pos.x, pos.y, pos.z);
		return new BlockState(id, meta);
	}
	
	public static BlockState getState(Level level, Vec3i pos, BlockState res) {
		res.blockID = (byte) level.getTileId(pos.x, pos.y, pos.z);
		res.blockMeta = (byte) level.getTileMeta(pos.x, pos.y, pos.z);
		return res;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		BlockState state = (BlockState) obj;
		return state == null ? false : state.blockID == blockID && state.blockMeta == blockMeta;
	}
}
