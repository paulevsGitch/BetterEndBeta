package paulevs.beb.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.level.dimension.McRegionDimensionFile;
import net.minecraft.level.storage.LevelStorage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.OtherServerLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerLevelListener;
import net.minecraft.util.Vec3i;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	private static ServerLevel endLevel;
	
	@Shadow
	private boolean running;
	
	@Inject(method = "prepareLevel", at = @At("TAIL"))
	private void beb_prepareLevel(LevelStorage arg, String levelName, long seed, CallbackInfo info) {
		McRegionDimensionFile dimFile = new McRegionDimensionFile(new File("."), levelName, true);
		MinecraftServer server = (MinecraftServer) (Object) this;
		endLevel = new OtherServerLevel(server, dimFile, levelName, 2, seed, server.levels[0]);
		endLevel.addLevelListener(new ServerLevelListener(server, endLevel));
		endLevel.difficulty = server.serverProperties.getBoolean("spawn-monsters", true) ? 1 : 0;
		endLevel.method_196(server.serverProperties.getBoolean("spawn-monsters", true), server.spawnAnimals);

		int size = 196;
		long time = System.currentTimeMillis();
		Vec3i spawn = endLevel.getSpawnPosition();
		for (int offsetX = -size; offsetX <= size && running; offsetX += 16) {
			for (int offsetZ = -size; offsetZ <= size && running; offsetZ += 16) {
				long newTime = System.currentTimeMillis();
				if (newTime < time) {
					time = newTime;
				}

				if (newTime > time + 1000L) {
					int total = (size * 2 + 1) * (size * 2 + 1);
					int count = (offsetX + size) * (size * 2 + 1) + offsetZ + 1;
					this.printProgress("Preparing spawn area", count * 100 / total);
					time = newTime;
				}

				endLevel.serverLevelSource.loadChunk(spawn.x + offsetX >> 4, spawn.z + offsetZ >> 4);

				while (endLevel.method_232() && this.running) {}
			}
		}
	}
	
	@Inject(method = "saveLevel", at = @At("TAIL"))
	private void beb_saveLevel(CallbackInfo info) {
		endLevel.saveLevel(true, null);
		endLevel.method_331();
	}
	
	@Inject(method = "getLevel", at = @At("HEAD"), cancellable = true)
	private void getLevel(int index, CallbackInfoReturnable<ServerLevel> info) {
		if (index == 2) {
			info.setReturnValue(endLevel);
			info.cancel();
		}
	}
	
	@Shadow
	private void printProgress(String name, int percentage) {}
}
