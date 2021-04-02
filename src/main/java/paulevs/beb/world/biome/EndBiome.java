package paulevs.beb.world.biome;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.entity.EntityEntry;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.util.maths.MathHelper;
import paulevs.beb.registry.EndBlocks;
import paulevs.beb.util.BlockState;
import paulevs.beb.world.structures.EndStructure;

public class EndBiome extends Biome {
	private static BlockState defBlock;
	private List<EndStructure> structures;
	private BlockState[] surface;
	
	public EndBiome(String name) {
		this.setName(name);
		this.topTileId = (byte) EndBlocks.getDefaultBlock().id;
		this.underTileId = (byte) EndBlocks.getDefaultBlock().id;
		this.structures = Lists.newArrayList();
	}
	
	@SuppressWarnings("unchecked")
	public void addMonsterSpawn(Class<?> entryClass, int rarity) {
		this.monsters.add(new EntityEntry(entryClass, rarity));
	}
	
	@SuppressWarnings("unchecked")
	public void addCreatureSpawn(Class<?> entryClass, int rarity) {
		this.creatures.add(new EntityEntry(entryClass, rarity));
	}
	
	public void setTerrain(BlockState... surface) {
		this.surface = surface;
	}
	
	public BlockState getTerrain(float value) {
		if (surface == null || surface.length == 0) {
			if (defBlock == null) {
				defBlock = new BlockState(EndBlocks.getBlock("end_stone"));
			}
			return defBlock;
		}
		int index = MathHelper.floor(value * surface.length);
		return surface[index == surface.length ? index - 1: index];
	}
	
	public void decorate(Level level, Random rand, int startX, int startZ) {
		structures.forEach((structure) -> {
			structure.generate(level, rand, startX, startZ);
		});
	}
	
	public void addStructure(EndStructure structure) {
		structures.add(structure);
	}
}
