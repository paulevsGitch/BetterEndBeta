package paulevs.beb.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.level.dimension.Dimension;
import paulevs.beb.world.generator.EndDimension;

@Mixin(Dimension.class)
public class DimensionMixin {
	@Inject(method = "getByID", at = @At("HEAD"), cancellable = true)
	private static void beb_getByID(int id, CallbackInfoReturnable<Dimension> info) {
		if (id == 2) {
			info.setReturnValue(new EndDimension());
			info.cancel();
		}
	}
}
