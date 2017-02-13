package com.wowserman.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.wowserman.Duckhunt;
import com.wowserman.arena.Arena;
import com.wowserman.arena.ArenaStatus;
import com.wowserman.arena.Team;

public class PlayerMove implements Listener {

	@EventHandler
	public void move(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		if (!Duckhunt.isInGame(player)) return;
		Arena arena = Duckhunt.getArena(player);
		Block block = player.getLocation().getBlock().getRelative(0, -1, 0);
		if (block.getType()==Material.PISTON_BASE) {
			Vector up = new Vector();
			up.setY(1.5);
			player.setVelocity(up);
			return;
		}
		
		if (block.getType()==Material.CLAY) {
			player.addPotionEffect(PotionEffectType.SPEED.createEffect(20, 2), true);
			return;
		}
		
		if (block.getType()==Material.SPONGE) {
			player.addPotionEffect(PotionEffectType.SLOW.createEffect(20, 2), true);
			return;
		}
		
		if (block.getType()==Material.EMERALD) {
			player.addPotionEffect(PotionEffectType.JUMP.createEffect(20, 2), true);
			return;
		}
		
		if (block.getType()==Material.DIAMOND_BLOCK) {
			if (arena.getStatus()==ArenaStatus.Ending) return;
			if (arena.getTeam(player)==Team.Ducks) arena.duckFinish(arena.getGamePlayer(player));
			return;
		}
		
		if (block.getType()==Material.BARRIER) {
			arena.teleportToSpawn(player);
			return;
		}
	}
}
