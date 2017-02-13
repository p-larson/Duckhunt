package com.wowserman.classes;

import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.wowserman.Config;
import com.wowserman.Duckhunt;
import com.wowserman.arena.Arena;
import com.wowserman.arena.GameClassType;
import com.wowserman.arena.GamePlayer;

public class Runner implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void use(PlayerInteractEvent e) {
		if (!Duckhunt.isInGame(e.getPlayer())) return;
		Arena arena = Duckhunt.getArena(e.getPlayer());
		GamePlayer player = arena.getGamePlayer(e.getPlayer());
		if (player==null) return;
		if (player.getType()!=GameClassType.Runner) return;
		// Player is a Runner
		if (e.getAction()!=Action.RIGHT_CLICK_AIR && e.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
		if (e.getPlayer().getItemInHand().getType()!=Config.getSpeedFeather(arena, e.getPlayer()).getType()) return;
		if (player.getItemCount(Config.getSpeedFeather(arena, e.getPlayer())) <= 0) return;
		player.removeItemInHand();
		e.getPlayer().getWorld().spawnParticle(Particle.CLOUD, e.getPlayer().getEyeLocation(), 10, 0.5, 0.5, 0.5, 0);
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 2, false, false));
	}
}
