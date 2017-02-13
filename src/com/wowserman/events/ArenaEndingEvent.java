package com.wowserman.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.wowserman.arena.Arena;
import com.wowserman.arena.Team;

public class ArenaEndingEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private Arena arena;
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public Team getWinningTeam() {
		return arena.getWinningTeam();
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public ArenaEndingEvent(Arena arena) {
		this.arena = arena;
	}
}
