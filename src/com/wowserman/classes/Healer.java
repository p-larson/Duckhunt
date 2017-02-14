package com.wowserman.classes;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.wowserman.Config;
import com.wowserman.Duckhunt;
import com.wowserman.Tools;
import com.wowserman.arena.Arena;
import com.wowserman.arena.GameClassType;
import com.wowserman.arena.Team;

public class Healer implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void interact(PlayerInteractEntityEvent e) {
		Player player = e.getPlayer();
		if (!Duckhunt.isInGame(player)) return;
		Arena arena = Duckhunt.getArena(player);
		if (arena.getGameClassType(player)!=GameClassType.Healer) return;
		if (Tools.isItem(player.getItemInHand(), Config.getHealWand(arena, player))==false) return;
		if (e.getRightClicked() instanceof Player == false) return;
		Player rightclicked = (Player) e.getRightClicked();
		if (!Duckhunt.isInGame(rightclicked) || arena.getTeam(rightclicked)!=Team.Ducks) return;
		rightclicked.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 2));
		rightclicked.spawnParticle(Particle.HEART, rightclicked.getEyeLocation(), 10);
		this.heal(rightclicked, player);
	}
	
	public void heal(Player player, Player healer) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 2));
		player.getWorld().spawnParticle(Particle.HEART, player.getEyeLocation(), 10);
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1f, 1f);
	}
	
}
