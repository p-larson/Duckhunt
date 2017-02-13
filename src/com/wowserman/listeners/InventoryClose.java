package com.wowserman.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.wowserman.Duckhunt;
import com.wowserman.arena.Arena;
import com.wowserman.arena.GameClassType;
import com.wowserman.arena.Team;

public class InventoryClose implements Listener {
	@EventHandler
	public void close(InventoryCloseEvent e) {
		Player player = (Player) e.getPlayer();
		if (Duckhunt.isInGame(player)) {
			Arena arena = Duckhunt.getArena(player);
			Team team = Duckhunt.getTeam(player);
			GameClassType type = arena.getGameClassType(player);
			if (type==GameClassType.None) {
				switch (team) {
				case Ducks:
					arena.getGamePlayer(player).changeType(GameClassType.Runner);
					break;
				case Hunters:
					arena.getGamePlayer(player).changeType(GameClassType.Cyborg);
					break;
				default:
					break;
				}
			}
		}
	}
}
