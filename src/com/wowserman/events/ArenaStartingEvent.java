package com.wowserman.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.wowserman.arena.Arena;

public class ArenaStartingEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private Arena arena;
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public ArenaStartingEvent(Arena arena) {
		this.arena = arena;
	}
}
