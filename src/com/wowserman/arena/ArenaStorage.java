package com.wowserman.arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.wowserman.Config;
import com.wowserman.Duckhunt;

import net.md_5.bungee.api.ChatColor;

public class ArenaStorage {

	public FileConfiguration configfile;
	private File fileobject;

	private void attemptToCreateFile(Plugin p) {

		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}

		fileobject = new File(p.getDataFolder(), "arenas.yml");

		if (!fileobject.exists()) {
			try {
				fileobject.createNewFile();
			}
			catch (IOException e) {
				Bukkit.getServer().getLogger().severe(ChatColor.RED  + "Could not create arenas.yml!");
			}
		}

		configfile = YamlConfiguration.loadConfiguration(fileobject);
	}

	public void saveFile() {
		try {
			configfile.save(fileobject);
		}
		catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED  + "Could not save arenas.yml!");
		}
	}

	public void reloadData() {
		configfile = YamlConfiguration.loadConfiguration(fileobject);
	}

	public ArenaStorage() {
		this.attemptToCreateFile(Duckhunt.plugin);
	}


	public void createArena(String name) {
		ConfigurationSection section = configfile.createSection(name.toLowerCase());

		section.set("enabled", true);
		section.set("auto-join-after-completion", true);
		section.set("minimum-players", 4);
		section.set("maximum-players", 24);
		section.set("duck-spawn-x", 0.0);
		section.set("duck-spawn-y", 0.0);
		section.set("duck-spawn-z", 0.0);
		section.set("hunter-spawn-x", 0.0);
		section.set("hunter-spawn-y", 0.0);
		section.set("hunter-spawn-z", 0.0);
		section.set("spectator-spawn-x", 0.0);
		section.set("spectator-spawn-y", 0.0);
		section.set("spectator-spawn-z", 0.0);
		section.addDefault("world", "world");
		section.set("world", "world");

		this.saveFile();
		this.reloadData();
		Duckhunt.arenas.add(getArena(name));
	}

	public void setDuckSpawn(Arena arena, Player player) {
		ConfigurationSection section = configfile.getConfigurationSection(arena.getName().toLowerCase());
		section.set("duck-spawn-x", player.getLocation().getBlockX()+0.5);
		section.set("duck-spawn-y", player.getLocation().getBlockY()+0.5);
		section.set("duck-spawn-z", player.getLocation().getBlockZ()+0.5);
		this.saveFile();
		this.reloadData();
	}

	public void setHunterSpawn(Arena arena, Player player) {
		ConfigurationSection section = configfile.getConfigurationSection(arena.getName().toLowerCase());
		section.set("hunter-spawn-x", player.getLocation().getBlockX()+0.5);
		section.set("hunter-spawn-y", player.getLocation().getBlockY()+0.5);
		section.set("hunter-spawn-z", player.getLocation().getBlockZ()+0.5);
		this.saveFile();
		this.reloadData();
	}

	public void setSpectatorSpawn(Arena arena, Player player) {
		ConfigurationSection section = configfile.getConfigurationSection(arena.getName().toLowerCase());
		section.set("spectator-spawn-x", player.getLocation().getBlockX()+0.5);
		section.set("spectator-spawn-y", player.getLocation().getBlockY()+0.5);
		section.set("spectator-spawn-z", player.getLocation().getBlockZ()+0.5);
		this.saveFile();
		this.reloadData();
	}

	public void setWorld(Arena arena, Player player) {
		ConfigurationSection section = configfile.getConfigurationSection(arena.getName().toLowerCase());
		section.set("world", player.getLocation().getWorld().getName());
		this.saveFile();
		this.reloadData();
		arena.reloadConfig();
	}

	public static Location getSpawnLocation(Arena arena, Team team) {
		String prefix = "";
		if (team==Team.Ducks) prefix = "duck";
		if (team==Team.Hunters) prefix = "hunter";
		if (team==Team.Spectators) prefix = "spectator";
		if (prefix == "") {
			Bukkit.getConsoleSender().sendMessage("Problem getting the location for " + arena.getName() + " of team: " + team.toString());
			return Config.fallbackLocation;
		}
		double x, y, z;
		x = Duckhunt.arenaStorage.configfile.getConfigurationSection(arena.getName().toLowerCase()).getDouble(prefix+"-spawn-x");
		y = Duckhunt.arenaStorage.configfile.getConfigurationSection(arena.getName().toLowerCase()).getDouble(prefix+"-spawn-y");
		z = Duckhunt.arenaStorage.configfile.getConfigurationSection(arena.getName().toLowerCase()).getDouble(prefix+"-spawn-z");
		Location location = new Location(Bukkit.getWorld(Duckhunt.arenaStorage.configfile.getConfigurationSection(arena.getName().toLowerCase()).getString("world")), x, y, z);
		return location;
	}

	public static void toggle(Arena arena) {
		Duckhunt.arenaStorage.configfile.getConfigurationSection(arena.getName().toLowerCase()).set("enabled", !arena.isEnabled());
		arena.setEnabled(!arena.isEnabled());
	}

	public Arena getArena(String name) {
		if (this.configfile.getConfigurationSection(name.toLowerCase())==null) return null;
		else {
			// Arena(String name, ArenaStatus status, int playerCount, int maxPlayers, Location duckSpawn, Location hunterSpawn, Location spectatorSpawn, List<Location> checkpoints)
			ConfigurationSection section = this.configfile.getConfigurationSection(name.toLowerCase());

			ArenaStatus status = ArenaStatus.Disabled;
			try { 
				if (section.getBoolean("enabled")==true) 
					status = ArenaStatus.Waiting; // Arena Not Disabled.
			} catch (Exception e) { /* Caught Error*/ }

			Integer minPlayers = 0;
			try { 
				minPlayers = section.getInt("minimum-players");
			} catch (Exception e) { /* Caught Error*/ }

			Integer maxPlayers = 0;
			try { 
				maxPlayers = section.getInt("maximum-players");
			} catch (Exception e) { /* Caught Error*/ }

			Location duckSpawn = null;
			try { 
				duckSpawn = new Location(Bukkit.getWorld(section.getString("world")), section.getDouble("duck-spawn-x"), section.getDouble("duck-spawn-y"), section.getDouble("duck-spawn-z"));
			} catch (Exception e) { /* Caught Error*/ }

			Location hunterSpawn = null;
			try { 
				hunterSpawn = new Location(Bukkit.getWorld(section.getString("world")), section.getDouble("hunter-spawn-x"), section.getDouble("hunter-spawn-y"), section.getDouble("hunter-spawn-z"));
			} catch (Exception e) { /* Caught Error*/ }

			Location spectatorSpawn = null;
			try { 
				spectatorSpawn = new Location(Bukkit.getWorld(section.getString("world")), section.getDouble("spectator-spawn-x"), section.getDouble("spectator-spawn-y"), section.getDouble("spectator-spawn-z"));
			} catch (Exception e) { /* Caught Error*/ }

			List<Location> checkpoints = new ArrayList<Location>();
			return new Arena(name, status, minPlayers, maxPlayers, duckSpawn, hunterSpawn, spectatorSpawn, checkpoints);
		}
	}
}
