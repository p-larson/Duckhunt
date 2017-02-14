package com.wowserman.classes;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.wowserman.Config;
import com.wowserman.Duckhunt;
import com.wowserman.Tools;
import com.wowserman.arena.Arena;
import com.wowserman.arena.GameClassType;
import com.wowserman.arena.GamePlayer;

public class Cloaker implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void use(PlayerInteractEvent e) {
		if (!Duckhunt.isInGame(e.getPlayer())) return;
		Arena arena = Duckhunt.getArena(e.getPlayer());
		GamePlayer player = arena.getGamePlayer(e.getPlayer());
		if (player==null) return;
		if (player.getType()!=GameClassType.Cloaker) return;
		// Player is a Runner
		if (e.getAction()!=Action.RIGHT_CLICK_AIR && e.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
		if (Tools.isItem(player.getPlayer().getItemInHand(), Config.getInvisibilityCloak(arena, player.getPlayer())) != true) return;
		player.removeItemInHand();
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1, false, false));
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1f, 1f);
		new BukkitRunnable() {
			int i = 100;
			@Override
			public void run() {
				if (i<=0) {
					this.cancel();
				} else {
					player.getPlayer().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getPlayer().getLocation(), 1, 0, 0, 0, 0);
					player.getPlayer().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getPlayer().getLocation().add(0, 0.4, 0), 1, 0, 0, 0, 0);
					player.getPlayer().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getPlayer().getLocation().add(0, 0.8, 0), 1, 0, 0, 0, 0);
				}
				i += -1;
			}
			
		}.runTaskTimer(Duckhunt.plugin, 0, 1);
		e.setCancelled(true);
	}
}
