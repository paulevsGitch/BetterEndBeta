package paulevs.beb;

import net.modificationstation.stationloader.api.client.event.texture.TextureRegister;
import net.modificationstation.stationloader.api.common.config.Configuration;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import net.modificationstation.stationloader.api.common.event.item.tool.EffectiveBlocksProvider;
import net.modificationstation.stationloader.api.common.mod.StationMod;
import paulevs.beb.registry.EndBiomes;
import paulevs.beb.registry.EndBlocks;
import paulevs.beb.registry.EndTextures;
import paulevs.beb.registry.EndToolEffective;

public class BetterEndBeta implements StationMod {
	public static final String MOD_ID = "beb";
	public static Configuration config;
	
	@Override
	public void preInit() {
		BlockRegister.EVENT.register(new EndBlocks());
		TextureRegister.EVENT.register(new EndTextures());
		EffectiveBlocksProvider.EVENT.register(new EndToolEffective());
		new EndBiomes().registerBiomes();
		//BiomeRegister.EVENT.register(new EndBiomes());
		//ChunkGen.EVENT.register(new ChunkGen());
	}
	
	public static String getID(String name) {
		return MOD_ID + "." + name;
	}
}
