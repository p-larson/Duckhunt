package com.wowserman.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.wowserman.Config;
import com.wowserman.Duckhunt;

public class PlayerConnect implements Listener {

	@EventHandler
	public void connect(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		if (!Config.bungeeAutoJoinGameOnConnect) return;
		
		if (Duckhunt.getMainArena()==null) return;
		
		player.getInventory().clear();
		
		Duckhunt.getMainArena().join(player);
	}
}
