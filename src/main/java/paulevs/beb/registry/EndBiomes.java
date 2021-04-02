package paulevs.beb.registry;

import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Maps;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationloader.api.common.event.level.biome.BiomeRegister;
import paulevs.beb.world.biome.EndBarrensBiome;
import paulevs.beb.world.biome.FoggyMushroomlandBiome;
import paulevs.beb.world.generator.EndBiomeSource;

public class EndBiomes implements BiomeRegister {
	private static final Map<String, Biome> BIOMES = Maps.newHashMap();
	
	@Override
	public void registerBiomes() {
		register("End Barrens", EndBarrensBiome::new);
		register("Foggy Mushroomland", FoggyMushroomlandBiome::new);
	}
	
	private static void register(String name, Function<String, Biome> init) {
		Biome biome = init.apply(name);
		BIOMES.put(name, biome);
		EndBiomeSource.addBiome(biome);
	}
	
	public static Biome getBiome(String name) {
		return BIOMES.get(name);
	}
}
