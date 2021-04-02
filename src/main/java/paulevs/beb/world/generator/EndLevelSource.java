package paulevs.beb.world.generator;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.source.OverworldLevelSource;
import paulevs.beb.noise.OpenSimplexNoise;
import paulevs.beb.registry.EndBlocks;
import paulevs.beb.util.BlockState;
import paulevs.beb.util.MHelper;
import paulevs.beb.world.biome.EndBiome;

public class EndLevelSource extends OverworldLevelSource {
	private byte[] metas = new byte[32768];
	private TerrainGenerator generator;
	private OpenSimplexNoise surfNoise;
	private Random random = new Random();
	private Level level;
	
	public EndLevelSource(Level level, long seed) {
		super(level, seed);
		this.generator = new TerrainGenerator(seed);
		this.surfNoise = new OpenSimplexNoise(seed);
		this.level = level;
	}
	
	@Override
	public void shapeChunk(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes, double[] temperatures) {
		byte coverID, coverMeta;
		generator.fillTerrainDensity(chunkX, chunkZ);
		random.setSeed(MHelper.getSeed(chunkX, chunkZ));
		byte fillID = (byte) EndBlocks.getBlockID("end_stone");
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int index = x << 4 | z;
				if (biomes[index] instanceof EndBiome) {
					EndBiome biome = (EndBiome) biomes[index];
					float value = (float) surfNoise.eval((chunkX + x) * 0.2, (chunkZ + z) * 0.2) * 0.4F + MHelper.randRange(0.4F, 0.6F, random);
					BlockState surf = biome.getTerrain(value);
					coverID = surf.getBlockID();
					coverMeta = surf.getBlockMeta();
				}
				else {
					coverID = biomes[index].topTileId;
					coverMeta = 0;
				}
				for (int y = 0; y < 128; y++) {
					if (generator.getDensity(x, y, z) > 0.0F) {
						int index2 = index << 7 | y;
						tiles[index2] = fillID;
						metas[index2] = 0;
					}
					else if (y > 3) {
						int index2 = index << 7 | (y - 1);
						if (tiles[index2] == fillID) {
							tiles[index2] = coverID;
							metas[index2] = coverMeta;
						}
					}
				}
			}
		}
	}
	
	@Override
	public void buildSurface(int chunkX, int chunkZ, byte[] tiles, Biome[] biomes) {}
	
	@Override
	public void decorate(LevelSource levelSource, int chunkX, int chunkZ) {
		int startX = chunkX << 4;
		int startZ = chunkZ << 4;
		Biome preBiome = level.getBiomeSource().getBiome(startX | 7, startZ | 7);
		if (preBiome instanceof EndBiome) {
			EndBiome biome = (EndBiome) preBiome;
			random.setSeed(MHelper.getSeed(chunkX, chunkZ));
			biome.decorate(level, random, startX, startZ);
		}
	}
	
	@Override
	public Chunk getChunk(int x, int z) {
		Chunk chunk = super.getChunk(x, z);
		for (int i = 0; i < metas.length; i++) {
			if (metas[i] > 0) {
				int bx = (i >> 11) & 15;
				int by = i & 127;
				int bz = (i >> 7) & 15;
				chunk.field_957.method_1704(bx, by, bz, metas[i]);
			}
		}
		return chunk;
	}
	
	@Environment(EnvType.CLIENT)
	public String toString() {
		return "EndLevelSource";
	}
}
