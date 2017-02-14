package com.wowserman;

import org.bukkit.Bukkit;

import com.wowserman.arena.Arena;
import com.wowserman.arena.ArenaStatus;
import com.wowserman.arena.GamePlayer;
import com.wowserman.events.GamePlayerAttackedEvent;

public class DamageManager {
	
	public static void damage(GamePlayer target, GamePlayer attacker, double damage) {
		Arena arena = target.getArena();
		if (arena.getStatus()!=ArenaStatus.InGame) return;
		double health = target.getPlayer().getHealth();
		double newHealth = health - damage;
		boolean isFatal = newHealth <= 0;
		GamePlayerAttackedEvent event = new GamePlayerAttackedEvent(target, attacker, damage, isFatal);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		if (isFatal) {
			// Player will Die.
			arena.setLastDuckDeath(target.getPlayer().getName());
			arena.setLastDuckKiller(attacker.getPlayer().getName());
			arena.killDuck(target);
		} else {
			// Player will Live.
			target.getPlayer().damage(damage);
			target.getPlayer().setHealth(newHealth);
		}
	}
}
