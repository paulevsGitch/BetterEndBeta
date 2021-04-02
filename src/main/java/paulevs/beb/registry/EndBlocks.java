package paulevs.beb.registry;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import paulevs.beb.block.EndGrassBlock;
import paulevs.beb.block.EndLuminophorBlock;
import paulevs.beb.block.EndOrganicBlock;
import paulevs.beb.block.EndPlanksBlock;
import paulevs.beb.block.EndPortalBlock;
import paulevs.beb.block.EndStoneBlock;
import paulevs.beb.block.EndTerrainBlock;
import paulevs.beb.block.EndWoodBlock;
import paulevs.beb.interfaces.BlockInit;

public class EndBlocks implements BlockRegister {
	private static final Map<String, BlockBase> BLOCKS = Maps.newHashMap();
	private static int startID = 100;
	private static BlockBase defBlock;
	
	@Override
	public void registerBlocks() {
		register("end_portal", EndPortalBlock::new);
		register("end_stone", EndStoneBlock::new);
		register("end_terrain", EndTerrainBlock::new);
		register("end_wood", EndWoodBlock::new);
		register("end_planks", EndPlanksBlock::new);
		register("end_luminophor", EndLuminophorBlock::new);
		register("end_organic", EndOrganicBlock::new);
		register("end_grass", EndGrassBlock::new);
		
		defBlock = getBlock("end_stone");
	}
	
	private static <T extends BlockBase> void register(String name, BlockInit<String, Integer, T> init) {
		BLOCKS.put(name, init.apply(name, startID++));
	}
	
	public static Collection<BlockBase> getModBlocks() {
		return BLOCKS.values();
	}

	public static BlockBase getBlock(String name) {
		return BLOCKS.getOrDefault(name, BlockBase.STONE);
	}
	
	public static int getBlockID(String name) {
		return getBlock(name).id;
	}
	
	public static BlockBase getDefaultBlock() {
		return defBlock;
	}
}
