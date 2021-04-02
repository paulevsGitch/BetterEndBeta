package paulevs.beb.world.generator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.source.LevelSource;
import net.minecraft.util.maths.Vec3f;

public class EndDimension extends Dimension {
	private static final Vec3f SKY = Vec3f.from(0.0, 0.0, 0.0);
	
	public EndDimension() {
		this.field_2179 = 2; // Dimension ID
		this.field_2177 = true; // No Light update
		this.field_2175 = true; // No Sky
	}
	
	@Override
	protected void initBiomeSource() {
		this.biomeSource = new EndBiomeSource(this.level.getSeed());
	}
	
	@Override
	public LevelSource createLevelSource() {
		return new EndLevelSource(this.level, this.level.getSeed());
	}
	
	@Environment(EnvType.CLIENT)
	public Vec3f method_1762(float f, float f1) {
		return SKY;
	}
}
