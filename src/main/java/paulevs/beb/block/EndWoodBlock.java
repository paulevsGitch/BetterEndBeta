package paulevs.beb.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.beb.registry.EndTextures;
import paulevs.beb.util.BlockTool;
import paulevs.beb.util.IBlockTool;

public class EndWoodBlock extends EndBlock implements BlockItemProvider, IBlockTool {
	protected static final String[] VARIANTS = new String[] {
		"dragon_tree",
		"end_lotus",
		"helix_tree",
		"jellyshroom",
		"lacugrove",
		"mossy_glowshroom",
		"pythadendron",
		"tenanea",
		"umbrella_tree"
	};
	
	public EndWoodBlock(String name, int id) {
		super(name, id, Material.WOOD);
		this.setBlastResistance(1);
		this.sounds(WOOD_SOUNDS);
		this.setHardness(1);
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		if (side == 0 || side == 1) {
			return EndTextures.getBlockTexture(VARIANTS[meta] + "_log_top");
		}
		else {
			return EndTextures.getBlockTexture(VARIANTS[meta] + "_log_side");
		}
	}
	
	@Override
	protected int droppedMeta(int i) {
		return i;
	}

	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntityWithMeta(i);
	}
	
	@Override
	public int getVariants() {
		return VARIANTS.length;
	}
	
	@Override
	public BlockTool getTool() {
		return BlockTool.AXE;
	}
	
	public static int getVariant(String name) {
		for (int i = 0; i < VARIANTS.length; i++) {
			if (VARIANTS[i].equals(name)) {
				return i;
			}
		}
		return 0;
	}
}
