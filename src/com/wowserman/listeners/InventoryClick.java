package com.wowserman.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.wowserman.Config;
import com.wowserman.Duckhunt;
import com.wowserman.Tools;
import com.wowserman.arena.Arena;
import com.wowserman.arena.GameClassType;

public class InventoryClick implements Listener {

	@EventHandler
	public void click(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if (!Duckhunt.isInGame(player)) return;
		
		Arena arena = Duckhunt.getArena(player);
		String name = e.getInventory().getName();
		ItemStack item = e.getCurrentItem();
		
		if (item==null || item.getType()==Material.AIR) return;
		
		if (name.equals(Config.getHunterClassSelector(arena, player).getName())) {
			if (Tools.isItem(item, Config.getCyborgIcon(arena, player))) {
				arena.changeClass(arena.getGamePlayer(player), GameClassType.Cyborg);
				player.closeInventory();
			}
			
			if (Tools.isItem(item, Config.getBarragerIcon(arena, player))) {
				arena.changeClass(arena.getGamePlayer(player), GameClassType.Barrager);
				player.closeInventory();
			}
			
			if (Tools.isItem(item, Config.getPyrotechIcon(arena, player))) {
				arena.changeClass(arena.getGamePlayer(player), GameClassType.Pyrotech);
				player.closeInventory();
			}
			
			if (Tools.isItem(item, Config.getFreezerIcon(arena, player))) {
				arena.changeClass(arena.getGamePlayer(player), GameClassType.Freezer);
				player.closeInventory();
			}
			e.setCancelled(true);
		}
		
		if (name.equals(Config.getDuckClassSelector(arena, player).getName())) {
			if (Tools.isItem(item, Config.getRunnerIcon(arena, player))) {
				arena.changeClass(arena.getGamePlayer(player), GameClassType.Runner);
				player.closeInventory();
			}
			
			if (Tools.isItem(item, Config.getHealerIcon(arena, player))) {
				arena.changeClass(arena.getGamePlayer(player), GameClassType.Healer);
				player.closeInventory();
			}
			
			if (Tools.isItem(item, Config.getSmokerIcon(arena, player))) {
				arena.changeClass(arena.getGamePlayer(player), GameClassType.Smoker);
				player.closeInventory();
			}
			
			if (Tools.isItem(item, Config.getCloakerIcon(arena, player))) {
				arena.changeClass(arena.getGamePlayer(player), GameClassType.Cloaker 	);
				player.closeInventory();
			}
			e.setCancelled(true);
		}
		
	}
}
