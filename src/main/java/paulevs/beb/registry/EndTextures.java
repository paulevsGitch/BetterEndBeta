package paulevs.beb.registry;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.common.collect.Maps;

import net.modificationstation.stationloader.api.client.event.texture.TextureRegister;
import net.modificationstation.stationloader.api.client.texture.TextureFactory;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;
import paulevs.beb.BetterEndBeta;
import paulevs.beb.util.ResourceUtil;

public class EndTextures implements TextureRegister {
	private static final Map<String, Integer> BLOCK_TEXTURES = Maps.newHashMap();
	
	@Override
	public void registerTextures() {
		TextureFactory textureFactory = TextureFactory.INSTANCE;
		TextureRegistry terrain = TextureRegistry.getRegistry("TERRAIN");
		String pathBlock = "/assets/" + BetterEndBeta.MOD_ID + "/textures/block/";
		loadTextureMap(textureFactory, terrain, pathBlock, BLOCK_TEXTURES);
	}
	
	private void loadTextureMap(TextureFactory factory, TextureRegistry registry, String path, Map<String, Integer> map) {
		List<String> list = ResourceUtil.getResourceFiles(path);
		list.forEach((texture) -> {
			if (texture.endsWith(".png")) {
				int width = 1;
				int height = 1;
				try {
					BufferedImage img = ImageIO.read(ResourceUtil.getResourceAsStream(path + "/" + texture));
					width = img.getWidth();
					height = img.getHeight();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				boolean normal = width == height;
				int index = normal ? factory.addTexture(registry, path + texture) : factory.addAnimatedTexture(registry, path + texture, 1);
				map.put(texture.substring(0, texture.lastIndexOf('.')), index);
			}
		});
	}
	
	public static int getBlockTexture(String name) {
		return BLOCK_TEXTURES.getOrDefault(name, 0);
	}
}
