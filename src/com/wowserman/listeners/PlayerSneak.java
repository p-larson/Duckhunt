package com.wowserman.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.wowserman.Duckhunt;
import com.wowserman.arena.Arena;
import com.wowserman.arena.Team;

public class PlayerSneak implements Listener {

	@EventHandler
	public void sneak(PlayerToggleSneakEvent e) {
		if (!e.isSneaking()) return;
		
		if (!Duckhunt.isInGame(e.getPlayer())) return;

		Arena arena = Duckhunt.getArena(e.getPlayer());
		
		if (arena.getTeam(e.getPlayer())==Team.None || arena.getTeam(e.getPlayer())==Team.Spectators) return;
		
		switch (arena.getTeam(e.getPlayer())) {
		case Ducks:
			if (arena.getDuckSpawn().distance(e.getPlayer().getLocation())>3) return;
			break;
		case Hunters:
			if (arena.getHunterSpawn().distance(e.getPlayer().getLocation())>5) return;
			break;
		default:
			break;
		}
		
		arena.openClassSelector(e.getPlayer());
	}
}
