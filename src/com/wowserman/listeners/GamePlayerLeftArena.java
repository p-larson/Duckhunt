package com.wowserman.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.wowserman.Config;
import com.wowserman.bungee.BungeeCommunication;
import com.wowserman.events.GamePlayerLeftArenaEvent;

public class GamePlayerLeftArena implements Listener {

	@EventHandler
	public void leaveArena(GamePlayerLeftArenaEvent e) {
		
		if (!Config.bungeeEnabled) return; 
		
		e.getGamePlayer().getPlayer().getInventory().clear();
		
		BungeeCommunication.sendMessage("connect " + e.getGamePlayer().getPlayer().getName() + " " + Config.bungeeLobbyServerName + " " + false);
	}
}
