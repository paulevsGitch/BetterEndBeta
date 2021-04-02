package paulevs.beb.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.beb.registry.EndTextures;
import paulevs.beb.util.BlockTool;
import paulevs.beb.util.IBlockTool;

public class EndPlanksBlock extends EndBlock implements BlockItemProvider, IBlockTool {
	private static final String[] VARIANTS = EndWoodBlock.VARIANTS;
	
	public EndPlanksBlock(String name, int id) {
		super(name, id, Material.WOOD);
		this.setBlastResistance(1F);
		this.sounds(WOOD_SOUNDS);
		this.setHardness(1);
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		return EndTextures.getBlockTexture(VARIANTS[meta] + "_planks");
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
	
	public static int getVariant(String name) {
		for (int i = 0; i < VARIANTS.length; i++) {
			if (VARIANTS[i].equals(name)) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public BlockTool getTool() {
		return BlockTool.AXE;
	}
}
