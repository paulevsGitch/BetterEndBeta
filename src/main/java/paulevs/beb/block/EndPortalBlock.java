package paulevs.beb.block;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;

public class EndPortalBlock extends EndBlock {
	private static final Set<PlayerBase> PLAYERS = Sets.newHashSet();
	
	public EndPortalBlock(String name, int id) {
		super(name, id, Material.PORTAL);
		this.setLightEmittance(0.75F);
		this.sounds(GLASS_SOUNDS);
		this.setHardness(-1.0F);
	}
	
	@Override
	public Box getCollisionShape(Level level, int x, int y, int z) {
		return null;
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	public void onEntityCollision(Level level, int x, int y, int z, EntityBase entity) {
		if (entity.vehicle == null && entity.passenger == null) {
			if (entity instanceof PlayerBase) {
				PLAYERS.add((PlayerBase) entity);
			}
			entity.method_1388();
		}
	}
	
	public static boolean teleportingPlayer(PlayerBase player) {
		if (PLAYERS.contains(player)) {
			PLAYERS.remove(player);
			return true;
		}
		return false;
	}
}
