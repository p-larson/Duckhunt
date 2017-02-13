package com.wowserman.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.wowserman.arena.GamePlayer;

public class GamePlayerAttackedEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
	private GamePlayer target;
	private GamePlayer attacker;
	private double damage;
	private boolean isFatal;
	private boolean canceled;
	
	public GamePlayerAttackedEvent(GamePlayer target, GamePlayer attacker, double damage, boolean isFatal) {
		this.target = target;
		this.attacker = attacker;
		this.damage = damage;
		this.isFatal = isFatal;
	}
	
	public GamePlayer getTarget() {
		return target;
	}

	public GamePlayer getAttacker() {
		return attacker;
	}

	public double getDamage() {
		return damage;
	}

	public boolean willDie() {
		return isFatal;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
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
