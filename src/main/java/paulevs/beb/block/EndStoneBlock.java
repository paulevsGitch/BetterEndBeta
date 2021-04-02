package paulevs.beb.block;

import net.minecraft.block.material.Material;
import paulevs.beb.util.BlockTool;
import paulevs.beb.util.IBlockTool;

public class EndStoneBlock extends EndBlock implements IBlockTool {
	public EndStoneBlock(String name, int id) {
		super(name, id, Material.STONE);
		this.setHardness(STONE.getHardness());
		this.setBlastResistance(5);
	}

	@Override
	public BlockTool getTool() {
		return BlockTool.PICKAXE;
	}
}
