package com.wowserman.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wowserman.Duckhunt;
import com.wowserman.Message;

import net.md_5.bungee.api.ChatColor;

public class ArenaEditor implements Listener {

	public static Arena arenaEditted;

	
	public static Inventory edit() {	
		Inventory edit = Bukkit.createInventory(null, 9, "" + arenaEditted.getName());
		
		ItemStack title = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta titleMeta = title.getItemMeta();
		titleMeta.setDisplayName(ChatColor.AQUA + arenaEditted.getName());
        title.setItemMeta(titleMeta);
        edit.setItem(0, title);
        
        ItemStack duck = new ItemStack(Material.FEATHER, 1);
		ItemMeta duckMeta = duck.getItemMeta();
		duckMeta.setDisplayName(ChatColor.AQUA + "Set Duck's Spawn");
		duck.setItemMeta(duckMeta);
        edit.setItem(3, duck);
        
        ItemStack hunter = new ItemStack(Material.LEATHER, 1);
		ItemMeta hunterMeta = hunter.getItemMeta();
		hunterMeta.setDisplayName(ChatColor.AQUA + "Set Hunter's Spawn");
		hunter.setItemMeta(hunterMeta);
        edit.setItem(2, hunter);
        
        ItemStack spectator = new ItemStack(Material.EYE_OF_ENDER, 1);
		ItemMeta spectatorMeta = spectator.getItemMeta();
		spectatorMeta.setDisplayName(ChatColor.AQUA + "Set Spectator's Spawn");
		spectator.setItemMeta(spectatorMeta);
        edit.setItem(4, spectator);
        
        ItemStack world = new ItemStack(Material.EMPTY_MAP, 1);
		ItemMeta worldMeta = world.getItemMeta();
		worldMeta.setDisplayName(ChatColor.AQUA + "Set the World");
		world.setItemMeta(worldMeta);
        edit.setItem(5, world);
        
        ItemStack enabled = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
		ItemMeta enabledMeta = enabled.getItemMeta();
		enabledMeta.setDisplayName(ChatColor.AQUA + "Toggle Enabled");
		enabled.setItemMeta(enabledMeta);
        edit.setItem(6, enabled);
        
		return edit;
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (arenaEditted == null) return;
		Player player = (Player) e.getWhoClicked();
		ItemStack clickedItem = e.getCurrentItem();
		Inventory inventory = e.getInventory(); 
		if (inventory.getName().equals(arenaEditted.getName()) && clickedItem!=null) {
			Material clicked = e.getCurrentItem().getType();
			if (clicked==Material.FEATHER) {
				Duckhunt.arenaStorage.setDuckSpawn(arenaEditted, player);
				new Message("&aSet the Duck's Spawn.", arenaEditted, player).send();
			}
			if (clicked==Material.LEATHER) {
				Duckhunt.arenaStorage.setHunterSpawn(arenaEditted, player);
				new Message("&aSet the Hunter's Spawn.", arenaEditted, player).send();
			}
			if (clicked==Material.EYE_OF_ENDER) {
				Duckhunt.arenaStorage.setSpectatorSpawn(arenaEditted, player);
				new Message("&aSet the Spectator's Spawn.", arenaEditted, player).send();
			}
			if (clicked==Material.EMPTY_MAP) {
				Duckhunt.arenaStorage.setWorld(arenaEditted, player);
				new Message("&aSet the World of the Arena.", arenaEditted, player).send();
			}
			if (clicked==Material.REDSTONE_TORCH_ON) {
				ArenaStorage.toggle(arenaEditted);
				new Message("&aToggled Enabled to &d" + arenaEditted.isEnabled(), arenaEditted, player).send();
			}
			e.setCancelled(true);
		}
	}
	
	Location getLocation(Player player) {
		Location location = player.getLocation();
		location.setX(player.getLocation().getBlockX()+0.5);
		location.setY(player.getLocation().getBlockY()+0.5);
		location.setZ(player.getLocation().getBlockZ()+0.5);

		
		return location;
		
	}
	
}
