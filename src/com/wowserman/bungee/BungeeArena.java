package com.wowserman.bungee;

import com.wowserman.Duckhunt;
import com.wowserman.arena.ArenaStatus;

public class BungeeArena {

	private String serverName;
	private String arenaName = "Unknown";
	private ArenaStatus status = ArenaStatus.Offline;
	private int playerCount = 1;
	private int maxPlayers = -1;
	private boolean valid = false;
	
	public String getServerName() {
		return serverName;
	}
	
	public String getArenaName() {
		return arenaName.substring(0, 1).toUpperCase() + arenaName.substring(1);
	}
	
	public void setArenaName(String arenaName) {
		this.arenaName = arenaName;
	}
	
	public ArenaStatus getStatus() {
		return status;
	}
	
	public void setStatus(ArenaStatus status) {
		this.status = status;
	}
	
	public int getPlayerCount() {
		return playerCount;
	}
	
	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public void validate() {
		valid = true;
	}

	public BungeeArena(String serverName) {
		this.serverName = serverName;
		Duckhunt.bungeeArenas.add(this);
	}
	
}