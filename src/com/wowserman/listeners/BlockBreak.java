package com.wowserman.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.wowserman.Duckhunt;

public class BlockBreak implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (Duckhunt.isInGame(e.getPlayer())) e.setCancelled(true);
		if (Duckhunt.isArenaSign(e.getBlock().getLocation())) Duckhunt.getArenaSign(e.getBlock().getLocation()).delete();
		if (Duckhunt.isBungeeSign(e.getBlock().getLocation())) Duckhunt.getBungeeArenaSign(e.getBlock().getLocation()).delete();
	}
}
