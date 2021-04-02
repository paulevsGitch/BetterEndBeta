package paulevs.beb.util.sdf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.level.Level;
import net.minecraft.util.Vec3i;
import net.minecraft.util.maths.Box;
import paulevs.beb.util.BlockState;
import paulevs.beb.util.BlocksHelper;
import paulevs.beb.util.Direction;
import paulevs.beb.util.MHelper;

public abstract class SDF {
	private List<Function<PosInfo, BlockState>> postProcesses = Lists.newArrayList();
	private Function<BlockState, Boolean> canReplace = (state) -> {
		return state.getMaterial().isReplaceable();
	};

	public abstract float getDistance(float x, float y, float z);
	
	public abstract BlockState getBlockState(Vec3i pos);
	
	public SDF addPostProcess(Function<PosInfo, BlockState> postProcess) {
		this.postProcesses.add(postProcess);
		return this;
	}
	
	public SDF setReplaceFunction(Function<BlockState, Boolean> canReplace) {
		this.canReplace = canReplace;
		return this;
	}
	
	public void fillRecursive(Level world, Vec3i start) {
		Map<Vec3i, PosInfo> mapWorld = Maps.newHashMap();
		Map<Vec3i, PosInfo> addInfo = Maps.newHashMap();
		Set<Vec3i> blocks = Sets.newHashSet();
		Set<Vec3i> ends = Sets.newHashSet();
		Set<Vec3i> add = Sets.newHashSet();
		ends.add(new Vec3i(0, 0, 0));
		boolean run = true;
		
		Vec3i bPos = new Vec3i();
		
		while (run) {
			for (Vec3i center: ends) {
				for (Direction dir: Direction.VALUES) {
					MHelper.set(bPos, center);
					dir.move(bPos);
					Vec3i wpos = MHelper.add(bPos, start);
					
					if (!blocks.contains(bPos) && canReplace.apply(BlockState.getState(world, wpos))) {
						if (this.getDistance(bPos.x, bPos.y, bPos.z) < 0) {
							BlockState state = getBlockState(wpos);
							PosInfo.create(mapWorld, addInfo, wpos).setState(state);
							add.add(new Vec3i(bPos));
						}
					}
				}
			}
			
			blocks.addAll(ends);
			ends.clear();
			ends.addAll(add);
			add.clear();
			
			run &= !ends.isEmpty();
		}
		
		List<PosInfo> infos = new ArrayList<PosInfo>(mapWorld.values());
		if (infos.size() > 0) {
			Collections.sort(infos);
			postProcesses.forEach((postProcess) -> {
				infos.forEach((info) -> {
					info.setState(postProcess.apply(info));
				});
			});
			infos.forEach((info) -> {
				BlocksHelper.setWithoutUpdate(world, info.getPos(), info.getState());
			});

			infos.clear();
			infos.addAll(addInfo.values());
			Collections.sort(infos);
			postProcesses.forEach((postProcess) -> {
				infos.forEach((info) -> {
					info.setState(postProcess.apply(info));
				});
			});
			infos.forEach((info) -> {
				if (canReplace.apply(BlockState.getState(world, info.getPos()))) {
					BlocksHelper.setWithoutUpdate(world, info.getPos(), info.getState());
				}
			});
		}
	}
	
	public void fillArea(Level world, Vec3i center, Box box) {
		Map<Vec3i, PosInfo> mapWorld = Maps.newHashMap();
		Map<Vec3i, PosInfo> addInfo = Maps.newHashMap();
		
		Vec3i mut = new Vec3i();
		for (int y = (int) box.minY; y <= box.maxY; y++) {
			mut.y = y;
			for (int x = (int) box.minX; x <= box.maxX; x++) {
				mut.x = x;
				for (int z = (int) box.minZ; z <= box.maxZ; z++) {
					mut.z = z;
					if (canReplace.apply(BlockState.getState(world, mut))) {
						Vec3i fpos = MHelper.subtract(mut, center);
						if (this.getDistance(fpos.x, fpos.y, fpos.z) < 0) {
							PosInfo.create(mapWorld, addInfo, new Vec3i(mut)).setState(getBlockState(mut));
						}
					}
				}
			}
		}
		
		List<PosInfo> infos = new ArrayList<PosInfo>(mapWorld.values());
		if (infos.size() > 0) {
			Collections.sort(infos);
			postProcesses.forEach((postProcess) -> {
				infos.forEach((info) -> {
					info.setState(postProcess.apply(info));
				});
			});
			infos.forEach((info) -> {
				BlocksHelper.setWithoutUpdate(world, info.getPos(), info.getState());
			});

			infos.clear();
			infos.addAll(addInfo.values());
			Collections.sort(infos);
			postProcesses.forEach((postProcess) -> {
				infos.forEach((info) -> {
					info.setState(postProcess.apply(info));
				});
			});
			infos.forEach((info) -> {
				if (canReplace.apply(BlockState.getState(world, info.getPos()))) {
					BlocksHelper.setWithoutUpdate(world, info.getPos(), info.getState());
				}
			});
		}
	}
	
	public void fillRecursiveIgnore(Level world, Vec3i start, Function<BlockState, Boolean> ignore) {
		Map<Vec3i, PosInfo> mapWorld = Maps.newHashMap();
		Map<Vec3i, PosInfo> addInfo = Maps.newHashMap();
		Set<Vec3i> blocks = Sets.newHashSet();
		Set<Vec3i> ends = Sets.newHashSet();
		Set<Vec3i> add = Sets.newHashSet();
		ends.add(new Vec3i(0, 0, 0));
		boolean run = true;
		
		Vec3i bPos = new Vec3i();
		
		while (run) {
			for (Vec3i center: ends) {
				for (Direction dir: Direction.VALUES) {
					MHelper.set(bPos, center);
					dir.move(bPos);
					Vec3i wpos = MHelper.add(bPos, start);
					BlockState state = BlockState.getState(world, wpos);
					boolean ign = ignore.apply(state);
					if (!blocks.contains(bPos) && (ign || canReplace.apply(state))) {
						if (this.getDistance(bPos.x, bPos.y, bPos.z) < 0) {
							PosInfo.create(mapWorld, addInfo, wpos).setState(ign ? state : getBlockState(bPos));
							add.add(new Vec3i(bPos));
						}
					}
				}
			}
			
			blocks.addAll(ends);
			ends.clear();
			ends.addAll(add);
			add.clear();
			
			run &= !ends.isEmpty();
		}
		
		List<PosInfo> infos = new ArrayList<PosInfo>(mapWorld.values());
		if (infos.size() > 0) {
			Collections.sort(infos);
			postProcesses.forEach((postProcess) -> {
				infos.forEach((info) -> {
					info.setState(postProcess.apply(info));
				});
			});
			infos.forEach((info) -> {
				BlocksHelper.setWithoutUpdate(world, info.getPos(), info.getState());
			});

			infos.clear();
			infos.addAll(addInfo.values());
			Collections.sort(infos);
			postProcesses.forEach((postProcess) -> {
				infos.forEach((info) -> {
					info.setState(postProcess.apply(info));
				});
			});
			infos.forEach((info) -> {
				if (canReplace.apply(BlockState.getState(world, info.getPos()))) {
					BlocksHelper.setWithoutUpdate(world, info.getPos(), info.getState());
				}
			});
		}
	}
}
