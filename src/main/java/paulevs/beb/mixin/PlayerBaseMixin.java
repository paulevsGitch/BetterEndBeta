package paulevs.beb.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.Dimension;

@Mixin(PlayerBase.class)
public abstract class PlayerBaseMixin extends Living {
	@Shadow
	public int dimensionId;
	
	public PlayerBaseMixin(Level arg) {
		super(arg);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void beb_onTick(CallbackInfo info) {
		if (this.isAlive() && dimensionId != this.level.dimension.field_2179) {
			System.out.println("Player in wrong dimension!");
			if (((Object) this) instanceof AbstractClientPlayer) {
				System.out.println("Fixing Client Dim!");
				AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
				@SuppressWarnings("deprecation")
				Minecraft mc = (Minecraft) FabricLoader.getInstance().getGameInstance();
				
				mc.level.removeEntity(player);
				player.removed = false;
				
				player.setPositionAndAngles(player.x, player.y, player.z, player.yaw, player.pitch);
				if (player.isAlive()) {
					mc.level.method_193(player, false);
				}
	
				Level newLevel = new Level(mc.level, Dimension.getByID(player.dimensionId));
				mc.showLevelProgress((Level) newLevel, "Entering the End", player);
		
				player.level = mc.level;
			}
			else {
				System.out.println("Fixing Server Dim! <later...>");
			}
		}
	}
}
