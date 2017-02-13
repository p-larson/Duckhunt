package com.wowserman.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.wowserman.Duckhunt;

public class BlockPlace implements Listener {

	@EventHandler
	public void onBreak(BlockPlaceEvent e) {
		if (Duckhunt.isInGame(e.getPlayer())) e.setCancelled(true);
	}
}
