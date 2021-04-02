package paulevs.beb.mixin;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import paulevs.beb.tab.CreativeScreen;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends PlayerBase {
	@Shadow
	protected Minecraft minecraft;
	
	public AbstractClientPlayerMixin(Level arg) {
		super(arg);
	}

	@Inject(method = "method_136", at = @At("HEAD"))
	private void beb_openCreativeTab(int i, boolean flag, CallbackInfo info) {
		if (this.isAlive() && i == Keyboard.KEY_G) {
			AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
			minecraft.openScreen(new CreativeScreen(player.inventory));
		}
		else if (i == Keyboard.KEY_R) {
			AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
			player.respawn();
		}
	}
}
