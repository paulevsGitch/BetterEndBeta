package paulevs.beb.block;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.beb.registry.EndTextures;
import paulevs.beb.util.BlockTool;
import paulevs.beb.util.BlocksHelper;
import paulevs.beb.util.IBlockTool;

public class EndGrassBlock extends EndBlock implements BlockItemProvider, IBlockTool {
	private static final String[] VARIANTS = new String[] {
		"bushy_grass",
		"creeping_moss",
		"fracturn",
		"jungle_grass",
		"salteago",
		"shadow_plant",
		"small_jellyshroom",
		"twisted_umbrella_moss_small",
		"umbrella_moss_small",
		"vaiolush_fern"
	};
	
	public EndGrassBlock(String name, int id) {
		super(name, id, Material.PLANT);
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		return EndTextures.getBlockTexture(VARIANTS[meta]);
	}
	
	@Override
	protected int droppedMeta(int i) {
		return i;
	}

	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntityWithMeta(i) {
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				return EndGrassBlock.this.getTextureForSide(0, damage);
			}
		};
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
	
	@Override
	public Box getCollisionShape(Level level, int x, int y, int z) {
		return null;
	}

	@Override
	public boolean isFullOpaque() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Environment(EnvType.CLIENT)
	public int method_1621() {
		return 1;
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		return this.isGround(level.getTileId(x, y - 1, z));
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int id) {
		super.onAdjacentBlockUpdate(level, x, y, z, id);
		this.tick(level, x, y, z);
	}

	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		this.tick(level, x, y, z);
	}
	
	@Override
	public boolean canGrow(Level level, int x, int y, int z) {
		return canPlaceAt(level, x, y, z);
	}
	
	protected boolean isGround(int id) {
		BlockBase block = BlockBase.BY_ID[id];
		return BlocksHelper.isTerrain(block);
	}

	protected void tick(Level level, int x, int y, int z) {
		if (!this.canPlaceAt(level, x, y, z)) {
			this.drop(level, x, y, z, level.getTileMeta(x, y, z));
			level.setTile(x, y, z, 0);
		}
	}
}
