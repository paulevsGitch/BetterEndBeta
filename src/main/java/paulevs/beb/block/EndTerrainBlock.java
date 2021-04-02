package paulevs.beb.block;

import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.beb.registry.EndTextures;

public class EndTerrainBlock extends EndStoneBlock implements BlockItemProvider {
	private static final String[] VARIANTS = new String[] {
		"amber_moss",
		"cave_moss",
		"chorus_nylium",
		"crystal_moss",
		"end_moss",
		"end_mycelium",
		"jungle_moss",
		"pink_moss",
		"shadow_grass"
	};
	
	public EndTerrainBlock(String name, int id) {
		super(name, id);
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		if (side == 0) {
			return EndTextures.getBlockTexture("end_stone");
		}
		else if (side == 1) {
			return EndTextures.getBlockTexture(VARIANTS[meta] + "_top");
		}
		else {
			return EndTextures.getBlockTexture(VARIANTS[meta] + "_side");
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
	
	public static int getVariant(String name) {
		for (int i = 0; i < VARIANTS.length; i++) {
			if (VARIANTS[i].equals(name)) {
				return i;
			}
		}
		return 0;
	}
}
