package com.wowserman.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.wowserman.arena.Arena;
import com.wowserman.arena.GamePlayer;

public class GamePlayerLeftArenaEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private GamePlayer gp;
	private Arena arena;
	
	public GamePlayer getGamePlayer() {
		return gp;
	}
	public Arena getArena() {
		return arena;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public GamePlayerLeftArenaEvent(GamePlayer gp, Arena arena) {
		this.gp = gp;
		this.arena = arena;
	}
}
