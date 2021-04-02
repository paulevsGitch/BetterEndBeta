package paulevs.beb.world.biome;

import paulevs.beb.block.EndTerrainBlock;
import paulevs.beb.registry.EndBlocks;
import paulevs.beb.util.BlockState;
import paulevs.beb.world.structures.EndStructures;

public class FoggyMushroomlandBiome extends EndBiome {
	public FoggyMushroomlandBiome(String name) {
		super(name);
		this.setTerrain(
			new BlockState(EndBlocks.getBlockID("end_terrain"), EndTerrainBlock.getVariant("end_mycelium")),
			new BlockState(EndBlocks.getBlockID("end_terrain"), EndTerrainBlock.getVariant("end_moss"))
		);
		this.addStructure(EndStructures.MOSSY_GLOWSHROOM);
	}
}
