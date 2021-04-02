package paulevs.beb.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.class_467;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.Dimension;
import paulevs.beb.block.EndPortalBlock;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(method = "switchDimension", at = @At("HEAD"), cancellable = true)
	private void beb_switchDimension(CallbackInfo info) {
		Minecraft mc = (Minecraft) (Object) this;
		AbstractClientPlayer player = mc.player;
		if (EndPortalBlock.teleportingPlayer(player)) {
			System.out.println("Toggle End!");
			if (player.dimensionId != 0) {
				player.dimensionId = 0;
			}
			else {
				player.dimensionId = 2;
			}
	
			mc.level.removeEntity(player);
			player.removed = false;
			if (player.dimensionId != 0) {
				player.setPositionAndAngles(player.x, player.y, player.z, player.yaw, player.pitch);
				if (player.isAlive()) {
					mc.level.method_193(player, false);
				}
	
				Level newLevel = new Level(mc.level, Dimension.getByID(2));
				mc.showLevelProgress((Level) newLevel, "Entering the End", player);
			}
			else {
				player.setPositionAndAngles(player.x, player.y, player.z, player.yaw, player.pitch);
				if (player.isAlive()) {
					mc.level.method_193(player, false);
				}
	
				Level newLevel = new Level(mc.level, Dimension.getByID(0));
				mc.showLevelProgress(newLevel, "Leaving the End", player);
			}
	
			player.level = mc.level;
			if (player.isAlive()) {
				player.setPositionAndAngles(player.x, player.y, player.z, player.yaw, player.pitch);
				mc.level.method_193(player, false);
				(new class_467()).method_1530(mc.level, player);
			}
			
			info.cancel();
		}
	}
}
