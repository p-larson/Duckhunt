package com.wowserman.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.wowserman.Duckhunt;

public class ItemDrop implements Listener {

	@EventHandler
	public void drop(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		if (!Duckhunt.isInGame(player)) return;
		e.setCancelled(true);
	}
}
