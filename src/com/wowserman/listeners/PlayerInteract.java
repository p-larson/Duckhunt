package com.wowserman.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.wowserman.Config;
import com.wowserman.Duckhunt;
import com.wowserman.Message;
import com.wowserman.arena.Arena;
import com.wowserman.arena.ArenaStatus;
import com.wowserman.arena.Team;
import com.wowserman.bungee.BungeeCommunication;
import com.wowserman.signs.ArenaSign;
import com.wowserman.signs.BungeeArenaSign;

public class PlayerInteract implements Listener {

	@EventHandler
	public void interact(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		
		if (Duckhunt.isInGame(player) != true) return;
				
		Arena arena = Duckhunt.getArena(player);
		
		if (arena.getStatus() == ArenaStatus.Recruiting || arena.getStatus() == ArenaStatus.Starting) {
			
			if (e.getItem()==null || e.getItem().hasItemMeta()==false || e.getItem().getItemMeta().getDisplayName() == null) return;
						
			if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Config.getDuckSelector(arena, player).getItemMeta().getDisplayName())) {
				arena.changePreferedTeam(player, Team.Ducks);
			}
						
			if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Config.getNoPreferenceSelector(arena, player).getItemMeta().getDisplayName())) {
				// Interacting with No Preference Selector
				arena.changePreferedTeam(player, Team.None);
			}
			
			if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Config.getHunterSelector(arena, player).getItemMeta().getDisplayName())) {
				// Interacting with Hunter Selector
				arena.changePreferedTeam(player, Team.Hunters);
			}
			
			if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Config.getLeaveItem(arena, player).getItemMeta().getDisplayName())) {
				// Interacting with Leave Item
				arena.leave(player);
			}
		}
	}
	
	@EventHandler
	public void useSign(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();
		if (e.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
		if (Duckhunt.isArenaSign(block.getLocation())) {
			ArenaSign sign = Duckhunt.getArenaSign(block.getLocation());
			if (sign.isLeaveSign()) {
				if (Duckhunt.isInGame(player)) sign.getArena().leave(player);
				else {
					new Message(Config.playerCantLeaveNotInGame, null, player).send();
				}
			}
			else {
				sign.getArena().join(player);
			}
		}
		
		if (Duckhunt.isBungeeSign(block.getLocation())) {
			BungeeArenaSign sign = Duckhunt.getBungeeArenaSign(block.getLocation());
			player.sendMessage("Connecting to Server: " + sign.getArena().getServerName());
			BungeeCommunication.sendMessage("connect " + player.getName() + " " + sign.getArena().getServerName());
		}
	}
	
	@EventHandler
	public void useChest(PlayerInteractEvent e) {
		
		if (e.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
				
		if (Duckhunt.isInGame(e.getPlayer()) != true) return;
				
		Arena arena = Duckhunt.getArena(e.getPlayer());
		
		if (arena.getTeam(e.getPlayer()) != Team.Ducks && arena.getTeam(e.getPlayer()) != Team.Hunters) return;
		
		if (e.getClickedBlock().getType() != Material.CHEST) return;
		
		e.setCancelled(true);
		
		arena.openClassSelector(e.getPlayer());
	}
}
