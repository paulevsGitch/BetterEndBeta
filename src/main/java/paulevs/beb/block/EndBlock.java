package paulevs.beb.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import paulevs.beb.BetterEndBeta;
import paulevs.beb.registry.EndTextures;

public class EndBlock extends BlockBase {
	protected final String name;
	
	public EndBlock(String name, int id, Material material) {
		super(id, material);
		//this.setName(BetterEndBeta.getID(name));
		this.setName(name);
		this.name = name;
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		return EndTextures.getBlockTexture(name);
	}
	
	public int getVariants() {
		return 1;
	}
}
