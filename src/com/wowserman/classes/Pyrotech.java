package com.wowserman.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.wowserman.Config;
import com.wowserman.DamageManager;
import com.wowserman.Duckhunt;
import com.wowserman.arena.Arena;
import com.wowserman.arena.GameClassType;
import com.wowserman.arena.GamePlayer;
import com.wowserman.arena.Team;

public class Pyrotech implements Listener {

	private List<Fireball> fireballs = new ArrayList<Fireball>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void use(PlayerInteractEvent e) {
		if (!Duckhunt.isInGame(e.getPlayer())) return;
		Arena arena = Duckhunt.getArena(e.getPlayer());
		GamePlayer player = arena.getGamePlayer(e.getPlayer());
		if (player==null) return;
		if (player.getType()!=GameClassType.Pyrotech) return;
		// Player is a Runner
		if (e.getAction()!=Action.LEFT_CLICK_AIR) return;
		if (e.getPlayer().getItemInHand().getType()!=Config.getFireball(arena, e.getPlayer()).getType()) return;
		if (player.getItemCount(Config.getFireball(arena, e.getPlayer())) <= 0) return;
		player.removeItemInHand();
		Fireball fireball = (Fireball) e.getPlayer().getWorld().spawnEntity(e.getPlayer().getEyeLocation().add(e.getPlayer().getLocation().getDirection().multiply(2.5)), EntityType.FIREBALL);
		fireball.setShooter(e.getPlayer());
		fireball.setVelocity(e.getPlayer().getLocation().getDirection().multiply(2.5));
		fireballs.add(fireball);
		player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_GHAST_SHOOT, 1f, 1f);
	}
	
	@EventHandler
	public void explode(EntityExplodeEvent e) {
		if (e.getEntity() instanceof Fireball == false) return;
		Fireball fireball = (Fireball) e.getEntity();
		if (fireballs.contains(fireball)) {
			if (fireball.getShooter() instanceof Player == false) return;
			Player sh = (Player) fireball.getShooter();
			if (!Duckhunt.isInGame(sh)) return;
			Arena arena = Duckhunt.getArena(sh);
			GamePlayer shooter = arena.getGamePlayer(sh);
			for (Entity en:fireball.getWorld().getNearbyEntities(fireball.getLocation(), 3, 3, 3)) {
				if (en instanceof Player != true) continue;
				Player player = (Player) en;
				if (Duckhunt.isInGame(player)==false) continue;
				GamePlayer target = arena.getGamePlayer(player);
				if (target==null) continue;
				if (arena.getTeam(player)!=Team.Ducks) continue;
				fireballExplosion(target, shooter);
			}
			e.setCancelled(true);
			fireballs.remove(fireball);
		}
	}
	
	
	public void fireballExplosion(GamePlayer target, GamePlayer shooter) {
		DamageManager.damage(target, shooter, 15);
	}
}
