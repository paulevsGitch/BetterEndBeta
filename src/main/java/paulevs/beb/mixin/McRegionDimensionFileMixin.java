package paulevs.beb.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.level.chunk.ChunkIO;
import net.minecraft.level.chunk.LevelChunkLoader;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.DimensionFile;
import net.minecraft.level.dimension.McRegionDimensionFile;
import paulevs.beb.world.generator.EndDimension;

@Mixin(McRegionDimensionFile.class)
public class McRegionDimensionFileMixin extends DimensionFile {
	public McRegionDimensionFileMixin(File file, String worldName, boolean mkdirs) {
		super(file, worldName, mkdirs);
	}

	@Inject(method = "getChunkIO", at = @At("HEAD"), cancellable = true)
	private void beb_getChunkIO(Dimension dimension, CallbackInfoReturnable<ChunkIO> info) {
		File parent = this.getParentFolder();
		if (dimension instanceof EndDimension) {
			File file = new File(parent, "DIM-END");
			file.mkdirs();
			info.setReturnValue(new LevelChunkLoader(file));
			info.cancel();
		}
	}
}
