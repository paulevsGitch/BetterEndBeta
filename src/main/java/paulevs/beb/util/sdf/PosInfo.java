package paulevs.beb.util.sdf;

import java.util.Map;

import net.minecraft.util.Vec3i;
import paulevs.beb.util.BlockState;
import paulevs.beb.util.Direction;

public class PosInfo implements Comparable<PosInfo> {
	private static final BlockState AIR = new BlockState(0, 0);
	private final Map<Vec3i, PosInfo> blocks;
	private final Map<Vec3i, PosInfo> add;
	private final Vec3i pos;
	private BlockState state;
	
	public static PosInfo create(Map<Vec3i, PosInfo> blocks, Map<Vec3i, PosInfo> add, Vec3i pos) {
		return new PosInfo(blocks, add, pos);
	}
	
	private PosInfo(Map<Vec3i, PosInfo> blocks, Map<Vec3i, PosInfo> add, Vec3i pos) {
		this.blocks = blocks;
		this.add = add;
		this.pos = pos;
		blocks.put(pos, this);
	}
	
	public BlockState getState() {
		return state;
	}
	
	public BlockState getState(Vec3i pos) {
		PosInfo info = blocks.get(pos);
		if (info == null) {
			info = add.get(pos);
			return info == null ? AIR : info.getState();
		}
		return info.getState();
	}
	
	public void setState(BlockState state) {
		this.state = state;
	}
	
	public void setState(Vec3i pos, BlockState state) {
		PosInfo info = blocks.get(pos);
		if (info != null) {
			info.setState(state);
		}
	}
	
	public BlockState getState(Direction dir) {
		PosInfo info = blocks.get(dir.offset(pos));
		if (info == null) {
			info = add.get(dir.offset(pos));
			return info == null ? AIR : info.getState();
		}
		return info.getState();
	}
	
	public BlockState getState(Direction dir, int distance) {
		PosInfo info = blocks.get(dir.offset(pos, distance));
		if (info == null) {
			return AIR;
		}
		return info.getState();
	}
	
	public BlockState getStateUp() {
		return getState(Direction.UP);
	}
	
	public BlockState getStateDown() {
		return getState(Direction.DOWN);
	}
	
	@Override
	public int hashCode() {
		return pos.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PosInfo)) {
			return false;
		}
		return pos.equals(((PosInfo) obj).pos);
	}

	@Override
	public int compareTo(PosInfo info) {
		return this.pos.y - info.pos.y;
	}

	public Vec3i getPos() {
		return pos;
	}
	
	public void setVec3i(Vec3i pos, BlockState state) {
		PosInfo info = new PosInfo(blocks, add, pos);
		info.state = state;
		add.put(pos, info);
	}
}
