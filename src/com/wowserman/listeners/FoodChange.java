package com.wowserman.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.wowserman.Duckhunt;

public class FoodChange implements Listener {

	@EventHandler
	public void foodchange(FoodLevelChangeEvent e) {
		if (e.getEntity() instanceof Player == false) return;
		Player player = (Player) e.getEntity();
		if (Duckhunt.isInGame(player)) e.setCancelled(true);
	}
}
