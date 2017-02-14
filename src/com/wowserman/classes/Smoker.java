package com.wowserman.classes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.wowserman.Duckhunt;
import com.wowserman.arena.Arena;
import com.wowserman.arena.GameClassType;
import com.wowserman.arena.GamePlayer;

public class Smoker implements Listener {
	
	private void smokeGrenadeLands(Location pos) {
		new BukkitRunnable() {
			int counter = 40;
			@Override
			public void run() {
				if (counter<=0) {
					this.cancel();
					return;
				}
				pos.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, pos, 25, 1, 1, 1, 0);
				counter += -1;
			}
		}.runTaskTimer(Duckhunt.plugin, 0, 5);
	}
	
	@EventHandler
	public void smokegrenadelands(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Snowball == false) return;
		if (e.getEntity().getShooter() instanceof Player == false) return;
		Snowball snowball = (Snowball) e.getEntity();
		Player player = (Player) snowball.getShooter();
		if (!Duckhunt.isInGame(player)) return;
		Arena arena = Duckhunt.getArena(player);
		GamePlayer gp = arena.getGamePlayer(player);
		if (gp.getType()!=GameClassType.Smoker) return;
		smokeGrenadeLands(snowball.getLocation());
		player.getWorld().playSound(snowball.getLocation(), Sound.BLOCK_ANVIL_LAND, 10f, 10f);
	}
}
