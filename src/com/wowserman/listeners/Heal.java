package com.wowserman.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.wowserman.Duckhunt;

public class Heal implements Listener {

	@EventHandler
	public void heal(EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player == false) return;
		Player player = (Player) e.getEntity();
		if (!Duckhunt.isInGame(player)) return;
		if (e.getRegainReason()==RegainReason.SATIATED) e.setCancelled(true);
	}
}
