package paulevs.beb.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.level.LevelManager;
import net.minecraft.level.chunk.ChunkIO;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.DimensionFile;
import paulevs.beb.world.generator.EndDimension;

@Mixin(DimensionFile.class)
public class DimensionFileMixin {
	@Final
	@Shadow
	private File parentFolder;

	@Inject(method = "getChunkIO", at = @At("HEAD"), cancellable = true)
	private void beb_getChunkIO(Dimension dimension, CallbackInfoReturnable<ChunkIO> info) {
		if (dimension instanceof EndDimension) {
			File file = new File(parentFolder, "DIM-END");
			file.mkdirs();
			info.setReturnValue(new LevelManager(file, true));
			info.cancel();
		}
	}
}
