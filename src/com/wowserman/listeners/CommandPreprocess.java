package com.wowserman.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.wowserman.Config;
import com.wowserman.Duckhunt;
import com.wowserman.Message;
import com.wowserman.arena.Arena;

public class CommandPreprocess implements Listener {

	@EventHandler
	public void precommand(PlayerCommandPreprocessEvent e) {
		Player player = e.getPlayer();
		if (!Duckhunt.isInGame(player)) return;
		if (e.getMessage().toLowerCase().contains("duckhunt")) return;
		Arena arena = Duckhunt.getArena(player);
		if (arena==null) return;
		boolean canceled = true;
		for (String command:Config.whitelistedCommands) {
			System.out.print(command + ", " + e.getMessage() + "," + (!e.getMessage().toLowerCase().contains(command.toLowerCase())));
			if (e.getMessage().toLowerCase().contains(command.toLowerCase())) {
				canceled = false;
				break;
			}
		}
		
		if (canceled) new Message(Config.playerUseCommand, arena, player).send();
		e.setCancelled(canceled);
	}
}
