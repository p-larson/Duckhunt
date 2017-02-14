package com.wowserman.signs;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.wowserman.Duckhunt;
import com.wowserman.arena.Arena;
import com.wowserman.bungee.BungeeArena;

import net.md_5.bungee.api.ChatColor;

public class SignStorage {
	
	public FileConfiguration configfile;
	private File fileobject;

	private void attemptToCreateFile(Plugin p) {

		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}

		fileobject = new File(p.getDataFolder(), "signs.yml");

		if (!fileobject.exists()) {
			try {
				fileobject.createNewFile();
			}
			catch (IOException e) {
				Bukkit.getServer().getLogger().severe(ChatColor.RED  + "Could not create signs.yml!");
			}
		}

		configfile = YamlConfiguration.loadConfiguration(fileobject);
	}

	public void saveFile() {
		try {
			configfile.save(fileobject);
		}
		catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED  + "Could not save signs.yml!");
		}
	}

	public void reloadData() {
		configfile = YamlConfiguration.loadConfiguration(fileobject);
	}
	
	public void createArenaSign(Location location, Arena arena, boolean leaveSign) {
		final int x = location.getBlockX();
		final int y = location.getBlockY();
		final int z = location.getBlockZ();
		final String world = location.getWorld().getName();
		ConfigurationSection section = this.configfile.createSection(x + "-" + y + "-" + z + "-" + world);
		section.set("x", x);
		section.set("y", y);
		section.set("z", z);
		section.set("world", world);
		section.set("bungee", false);
		section.set("leave", leaveSign);
		section.set("arena", arena.getName());
		this.saveFile();
		new ArenaSign(arena, location, leaveSign);
	}
	
	public void createBungeeArenaSign(Location location, BungeeArena arena) {
		final int x = location.getBlockX();
		final int y = location.getBlockY();
		final int z = location.getBlockZ();
		final String world = location.getWorld().getName();
		ConfigurationSection section = this.configfile.createSection(x + "-" + y + "-" + z + "-" + world);
		section.set("x", x);
		section.set("y", y);
		section.set("z", z);
		section.set("world", world);
		section.set("bungee", true);
		section.set("server", arena.getServerName());
		this.saveFile();
		new BungeeArenaSign(arena, location);
	}
	
	public void delete(String name) {
		this.configfile.set(name, null);
		this.saveFile();
	}
	
	public void loadSign(ConfigurationSection section) {
		boolean bungee = section.getBoolean("bungee");
		if (bungee) {
			final int x = section.getInt("x");
			final int y = section.getInt("y");
			final int z = section.getInt("z");
			final String world = section.getString("world");
			System.out.print("Duckhunt Loaded Sign at " + x + ", " + y + ", " + z + ", " + world);
			Location location = new Location(Bukkit.getWorld(world), x, y, z);
			BungeeArena arena = Duckhunt.getBungeeArenaOfServerName(section.getString("server"));
			new BungeeArenaSign(arena, location);
		} else {
			final int x = section.getInt("x");
			final int y = section.getInt("y");
			final int z = section.getInt("z");
			final String world = section.getString("world");
			System.out.print("Duckhunt Loaded Sign at " + x + ", " + y + ", " + z + ", " + world);
			final boolean leave = section.getBoolean("leave");
			Location location = new Location(Bukkit.getWorld(world), x, y, z);
			Arena arena = Duckhunt.getArenaOfName(section.getString("arena"));
			new ArenaSign(arena, location, leave);
		}
	}
 
	public SignStorage() {
		this.attemptToCreateFile(Duckhunt.plugin);
	}
}
