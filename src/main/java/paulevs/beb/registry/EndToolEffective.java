package paulevs.beb.registry;

import java.util.List;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.Hatchet;
import net.minecraft.item.tool.Pickaxe;
import net.minecraft.item.tool.Shovel;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationloader.api.common.event.item.tool.EffectiveBlocksProvider;
import paulevs.beb.util.BlockTool;
import paulevs.beb.util.IBlockTool;

public class EndToolEffective implements EffectiveBlocksProvider {
	@Override
	public void getEffectiveBlocks(ToolBase toolBase, ToolMaterial toolMaterial, List<BlockBase> list) {
		EndBlocks.getModBlocks().forEach((block) -> {
			if (block instanceof IBlockTool) {
				BlockTool tool = ((IBlockTool) block).getTool();
				if (tool == BlockTool.AXE && toolBase instanceof Hatchet) {
					list.add(block);
				}
				else if (tool == BlockTool.PICKAXE && toolBase instanceof Pickaxe) {
					list.add(block);
				}
				else if (tool == BlockTool.SHOVEL && toolBase instanceof Shovel) {
					list.add(block);
				}
			}
		});
	}
}
