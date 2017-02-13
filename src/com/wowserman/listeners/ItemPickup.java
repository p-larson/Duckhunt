package com.wowserman.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.wowserman.Duckhunt;

public class ItemPickup implements Listener {

	@EventHandler
	public void pickup(PlayerPickupItemEvent e) {
		Player player = e.getPlayer();
		if (!Duckhunt.isInGame(player)) return;
		e.setCancelled(true);
	}
}
