package paulevs.beb.mixin;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.level.Level;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	private static FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
	
	@Shadow
	private Level level;
	
	@Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
	private void beb_renderSky(float f, CallbackInfo info) {
		if (level.dimension.field_2179 == 2) {
			//GL11.glClearColor(100F, 0F, 0F, 1F);
			//beb_setFogColor(1, 0, 0);
			//info.cancel();
		}
	}
	
	private static void beb_setFogColor(float r, float g, float b) {
		buffer.rewind();
		buffer.put(r);
		buffer.put(g);
		buffer.put(b);
		buffer.put(1);
		buffer.rewind();
		GL11.glFog(GL11.GL_FOG_COLOR, buffer);
	}
}
