package com.wowserman.signs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

import com.wowserman.Duckhunt;
import com.wowserman.bungee.BungeeArena;

public class BungeeArenaSign {

	private BungeeArena arena;
	private Location location;
	private boolean deleted;
	
	public BungeeArena getArena() {
		return arena;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Block getBlock() {
		return location.getBlock();
	}
	
	public boolean isSign() {
		return (this.getBlock().getType()==Material.SIGN || this.getBlock().getType()==Material.SIGN_POST || this.getBlock().getType()==Material.WALL_SIGN);
	}
	
	public String getName() {
		return this.getLocation().getBlockX() + "-" + this.getLocation().getBlockY() + "-" + this.getLocation().getBlockZ() + "-" + this.getLocation().getWorld().getName();
	}
	
	public boolean update() {
		if (!isSign()) {
			this.delete();
			return false;
		}
		Sign sign = (Sign) this.getBlock().getState();
		if (!arena.isValid()) {
			sign.setLine(0, "§2Duckhunt");
			sign.setLine(1, arena.getArenaName());
			sign.setLine(2, "? / ?");
			sign.setLine(3, "§c§l" + arena.getStatus().toString());
			return true;
		}
		sign.setLine(0, "§2Duckhunt");
		sign.setLine(1, arena.getArenaName());
		sign.setLine(2, arena.getPlayerCount() + " / " + arena.getMaxPlayers());
		switch (arena.getStatus()) {
		case Disabled:
			sign.setLine(3, "§c§l" + arena.getStatus().toString());
			break;
		case Ending:
			sign.setLine(3, "§6§l" + arena.getStatus().toString());
			break;
		case InGame:
			sign.setLine(3, "§d§l" + arena.getStatus().toString());
			break;
		case Offline:
			sign.setLine(3, "§c§l" + arena.getStatus().toString());
			break;
		case Recruiting:
			sign.setLine(3, "§a§l" + arena.getStatus().toString());
			break;
		case Starting:
			sign.setLine(3, "§e§l" + arena.getStatus().toString());
			break;
		case Waiting:
			sign.setLine(3, "§6§l" + arena.getStatus().toString());
			break;
		case Pinging:
			sign.setLine(1, "Unknown");
			sign.setLine(3, "" + arena.getStatus().toString());
			break;
		default:
			break;
		}
		sign.update();
		return true;
	}
	
	public void delete() {
		Duckhunt.signStorage.delete(this.getName());
		this.deleted = true;
	}
	
	public BungeeArenaSign(BungeeArena arena, Location location) {
		
		this.arena = arena;
		this.location = location;
		Duckhunt.bungeeSigns.add(this);
		new BukkitRunnable() {
			@Override
			public void run() {
				if (update()==false || deleted) {
					this.cancel();
				}
			}
		}.runTaskTimer(Duckhunt.plugin, 0, 20);
		
	}
}
