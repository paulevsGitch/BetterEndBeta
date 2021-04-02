package paulevs.beb.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.class_467;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.packet.Id9Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPlayerConnectionManager;
import net.minecraft.server.level.ServerLevel;
import paulevs.beb.block.EndPortalBlock;

@Mixin(ServerPlayerConnectionManager.class)
public class ServerPlayerConnectionManagerMixin {
	@Shadow
	private MinecraftServer server;

	@Inject(method = "sendToOppositeDimension", at = @At("HEAD"), cancellable = true)
	private void beb_sendToOppositeDimension(ServerPlayer player, CallbackInfo info) {
		if (EndPortalBlock.teleportingPlayer(player)) {
			ServerLevel level = this.server.getLevel(player.dimensionId);
			int dim = 0;
	
			if (player.dimensionId != 0) {
				dim = 0;
			}
			else {
				dim = 2;
			}
	
			player.dimensionId = dim;
			ServerLevel newLevel = this.server.getLevel(player.dimensionId);
			player.packetHandler.send(new Id9Packet((byte) player.dimensionId));
			level.removeEntityServer(player);
			player.removed = false;
			if (player.dimensionId == -1) {
				player.setPositionAndAngles(player.x, player.y, player.z, player.yaw, player.pitch);
				if (player.isAlive()) {
					level.method_193(player, false);
				}
			}
			else {
				player.setPositionAndAngles(player.x, player.y, player.z, player.yaw, player.pitch);
				if (player.isAlive()) {
					level.method_193(player, false);
				}
			}
	
			if (player.isAlive()) {
				newLevel.spawnEntity(player);
				player.setPositionAndAngles(player.x, player.y, player.z, player.yaw, player.pitch);
				newLevel.method_193(player, false);
				newLevel.serverLevelSource.field_933 = true;
				(new class_467()).method_1530(newLevel, player);
				newLevel.serverLevelSource.field_933 = false;
			}
	
			this.method_554(player);
			player.packetHandler.method_832(player.x, player.y, player.z, player.yaw, player.pitch);
			player.setLevel(newLevel);
			this.sendPlayerTime(player, newLevel);
			this.method_581(player);
			info.cancel();
		}
	}

	@Shadow
	public void method_554(ServerPlayer player) {}

	@Shadow
	public void sendPlayerTime(ServerPlayer serverPlayer, ServerLevel serverLevel) {}

	@Shadow
	public void method_581(ServerPlayer serverPlayer) {}
}
