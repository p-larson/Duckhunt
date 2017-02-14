package com.wowserman;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;

import com.wowserman.arena.Arena;

public class Tools {
	
	public static String getMinutesAndSeconds(int seconds) {
		int minutes = 0;
		int secondz = seconds;
		String extra = ":";
		while (secondz-60>=0) {
			minutes = minutes + 1;
			secondz = secondz - 60;	
		}
		
		String s = secondz + "";
		if (secondz<10) {
			s = "0" + secondz;
		}
		
		return minutes + extra + s;
	}
	
	public static ItemStack itemFromSection(ConfigurationSection section, Arena arena, Player player, String perm) {
		int id = section.getInt("item-id");
		int data = section.getInt("data-value");
		String displayname = section.getString("display-name");
		List<String> lores = section.getStringList("lores");
		if (perm!=null) {
			if (player.hasPermission(perm)) lores.add(new Message(Config.classUnlockedTag, arena, player).getText());
			else lores.add(new Message(Config.classLockedTag, arena, player).getText());
		}
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(id, 1, (byte) data);
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(new Message(displayname, arena, player).getText());
		m.setLore(new Message(lores, arena, player).getTextList());
		item.setItemMeta(m);
		return item;
	}
	
	public static Location locationFromSection(ConfigurationSection section) {
		return new Location(Bukkit.getWorld(section.getString("world")), section.getDouble("x"), section.getDouble("y"), section.getDouble("z"));
	}
	
	public static Inventory lobbyInventory(Arena arena, Player player) {
		Inventory i = Bukkit.createInventory(null, InventoryType.PLAYER);
		if (Config.bookEnabled) i.setItem(Config.leaveItemSlot-1, Config.getLeaveItem(arena, player));
		i.setItem(Config.duckSelectorSlot-1, Config.getDuckSelector(arena, player));
		i.setItem(Config.noPreferenceSlot, Config.getNoPreferenceSelector(arena, player));
		i.setItem(Config.hunterSelectorSlot-1, Config.getHunterSelector(arena, player));
		if (Config.bookEnabled) i.setItem(Config.bookSlot-1, Duckhunt.bookStorage.getBook());
		return i;
	}
	
	public static boolean isItem(ItemStack i1, ItemStack i2) {
		if (i1.hasItemMeta() && i2.hasItemMeta()) return i1.getItemMeta().getDisplayName().equalsIgnoreCase(i2.getItemMeta().getDisplayName());
		else return i1==i2;
	}
	
	public static boolean isInventoryEmpty(Player p){
		for(ItemStack item : p.getInventory().getContents()) {
			if (item != null) return false;
		  }
		return true;
	}
	
	public static Block getTargetBlock(Player player, int range, Material cantBe) {
		BlockIterator iter = new BlockIterator(player, range);
        Block block = iter.next();
        Block lastBlock = block;
        while (iter.hasNext()) {
        	lastBlock = block;
        	block = iter.next();
            if (block.getType() == Material.AIR) {
                continue;
            }
            if (block.getType() == cantBe || block.getRelative(0, -1, 0).getType() == cantBe) {
            	block = lastBlock.getRelative(0, -1, 0);
            	while (block.getType()==Material.AIR) {
            		block = block.getRelative(0, -1, 0);
            		if (block.getY() < 0) break;
            	}
            	break;
            }
            break;
        }
        return block;
	}
	
	public static boolean isSameLocation(Location l1, Location l2) {
		return (l1.getBlock().getX()==l2.getBlock().getX() && l1.getBlock().getY()==l2.getBlock().getY() && l1.getBlock().getZ()==l2.getBlock().getZ() && l1.getBlock().getWorld().getName()==l2.getBlock().getWorld().getName());
	}
}
