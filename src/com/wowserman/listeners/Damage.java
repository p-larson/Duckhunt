package com.wowserman.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;


import com.wowserman.Duckhunt;

public class Damage implements Listener {

	@EventHandler
	public void damage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player == false) return;
		Player player = (Player) e.getEntity();
		if (!Duckhunt.isInGame(player)) return;
		if (e.getCause()!=DamageCause.CUSTOM) e.setCancelled(true);
	}
}
