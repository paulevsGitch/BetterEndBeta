package paulevs.beb.world.generator;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.gen.BiomeSource;

public class EndBiomeSource extends BiomeSource {
	private static final List<Biome> BIOMES = Lists.newArrayList();
	private static final double[] EMPTY = new double[256];
	private BiomeMap map;
	
	public EndBiomeSource(long seed) {
		map = new BiomeMap(seed, 64, BIOMES);
		temperatureNoises = EMPTY;
		rainfallNoises = EMPTY;
		detailNoises = EMPTY;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public double getTemperature(int x, int z) {
		return 1.0;
	}
	
	@Override
	public double[] getTemperatures(double[] temperatures, int x, int z, int xSize, int zSize) {
		if (temperatures == null || temperatures.length < xSize * zSize) {
			temperatures = new double[xSize * zSize];
		}
		
		Arrays.fill(temperatures, 1.0);
		
		return temperatures;
	}
	
	@Override
	public Biome getBiome(int x, int z) {
		return map.getBiome(x, z);
	}
	
	@Override
	public Biome[] getBiomes(Biome[] biomes, int x, int z, int xSize, int zSize) {
		if (biomes == null || biomes.length < xSize * zSize) {
			biomes = new Biome[xSize * zSize];
		}
		
		int i = 0;
		for (int px = 0; px < xSize; px++) {
			for (int pz = 0; pz < zSize; pz++) {
				biomes[i++] = map.getBiome(px + x, pz + z);
			}
		}
		
		return biomes;
	}
	
	public static void addBiome(Biome biome) {
		BIOMES.add(biome);
	}
}
