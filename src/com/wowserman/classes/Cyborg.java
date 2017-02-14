package com.wowserman.classes;

import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.wowserman.Config;
import com.wowserman.DamageManager;
import com.wowserman.Duckhunt;
import com.wowserman.arena.Arena;
import com.wowserman.arena.GameClassType;
import com.wowserman.arena.GamePlayer;
import com.wowserman.arena.Team;
import com.wowserman.tools.Fireworks;

public class Cyborg implements Listener {
	
	public void laserHitPlayer(GamePlayer target, GamePlayer shooter) {
		DamageManager.damage(target, shooter, 10);
		target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.ENTITY_WITHER_HURT, 1f, 1f);
		shooter.getPlayer().playSound(shooter.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f);
	}
	
	private void laserHit(Location loc, GamePlayer shooter) {
		for (Entity e:loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
			if (e instanceof Player != true) continue;
			Player player = (Player) e;
			if (Duckhunt.isInGame(player)==false) continue;
			Arena arena = Duckhunt.getArena(player);
			GamePlayer gp = Duckhunt.getArena(player).getGamePlayer(player);
			if (gp==null) continue;
			if (arena.getTeam(player)!=Team.Ducks) continue;
			laserHitPlayer(gp, shooter);
		}
	}
	
	private boolean willHit(Location loc) {
		return (loc.getBlock().getType()!=Material.AIR || loc.getWorld().getNearbyEntities(loc, 1, 1, 1).size() > 0);
	}
	
	private void shootLaser(GamePlayer player) {
		Location pos = player.getPlayer().getEyeLocation();
		Vector direction = player.getPlayer().getLocation().getDirection();
		int i = 0; while (i < 100) {
			pos.add(direction);
			if (i > 1 && willHit(pos)) {
				laserHit(pos, player);
				Fireworks.createFirework(pos, 255, 255, 255, Type.BALL, true, false, 1, null);
				break;
			}
			pos.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, pos, 1, 0, 0, 0, 0);
			i+=1;
		}
		player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_WITHER_SHOOT, 1f, 1f);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void laser(PlayerInteractEvent e) {
		if (!Duckhunt.isInGame(e.getPlayer())) return;
		Arena arena = Duckhunt.getArena(e.getPlayer());
		GamePlayer player = arena.getGamePlayer(e.getPlayer());
		if (player==null) return;
		if (player.getType()!=GameClassType.Cyborg) return;
		// Player is a Cyborg
		if (e.getAction()!=Action.LEFT_CLICK_AIR) return;
		if (e.getPlayer().getItemInHand().getType()!=Config.getLaserGun(arena, e.getPlayer()).getType()) return;
		if (player.getItemCount(Config.getPlasma(arena, e.getPlayer())) <= 0) return;
		player.removeItem(Config.getPlasma(arena, e.getPlayer()));
		shootLaser(player);
	}
}
