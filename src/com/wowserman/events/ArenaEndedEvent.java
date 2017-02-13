package com.wowserman.events;

import com.wowserman.arena.Arena;
import com.wowserman.arena.Team;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaEndedEvent extends Event {

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
	
	public ArenaEndedEvent(Arena arena) {
		this.arena = arena;
	}
}
