package com.wowserman.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wowserman.Duckhunt;
import com.wowserman.bungee.BungeeCommunication;

public class PlayerDisconnect implements Listener {

	@EventHandler
	public void disconnect(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		if (!Duckhunt.isInGame(player)) return;
		
		player.getInventory().clear();
		
		Duckhunt.getArena(player).leave(player);
		
		if (BungeeCommunication.serverName!=null && Duckhunt.getMainArena()!=null) 
			BungeeCommunication.sendMessage("statusupdate " + BungeeCommunication.getServerStatusMessage());
	}
	
}
