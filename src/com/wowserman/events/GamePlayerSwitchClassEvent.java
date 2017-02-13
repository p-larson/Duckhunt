package com.wowserman.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.wowserman.arena.GameClassType;
import com.wowserman.arena.GamePlayer;

public class GamePlayerSwitchClassEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private GamePlayer gp;
	private GameClassType oldType;
	private GameClassType newType;
	private boolean canceled = false;
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public GamePlayer getGamePlayer() {
		return gp;
	}
	
	public GameClassType getOldType() {
		return oldType;
	}
	
	public GameClassType getNewType() {
		return newType;
	}
	
	public GamePlayerSwitchClassEvent(GamePlayer gp, GameClassType oldType, GameClassType newType) {
		this.gp = gp;
		this.oldType = oldType;
		this.newType = newType;
	}

	@Override
	public boolean isCancelled() {
		return canceled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		canceled = arg0;
	}
}
