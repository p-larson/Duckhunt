package com.wowserman.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.wowserman.Config;
import com.wowserman.DamageManager;
import com.wowserman.Duckhunt;
import com.wowserman.Tools;
import com.wowserman.arena.Arena;
import com.wowserman.arena.GameClassType;
import com.wowserman.arena.GamePlayer;
import com.wowserman.arena.Team;

public class Freezer implements Listener {

	private static List<Location> icepillars = new ArrayList<Location>(); 	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void throwSnowball(PlayerInteractEvent e) { 
		if (!Duckhunt.isInGame(e.getPlayer())) return;
		Arena arena = Duckhunt.getArena(e.getPlayer());
		GamePlayer player = arena.getGamePlayer(e.getPlayer());
		if (player==null) return;
		if (player.getType()!=GameClassType.Freezer) return;
		// Player is a Freezer
		if (e.getAction()!=Action.RIGHT_CLICK_AIR && e.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
		if (e.getPlayer().getItemInHand().getType()!=Config.getSnowball(arena, e.getPlayer()).getType()) return;
		Snowball snowball = (Snowball) e.getPlayer().getWorld().spawnEntity(e.getPlayer().getEyeLocation().add(e.getPlayer().getLocation().getDirection().multiply(2.5)), EntityType.SNOWBALL);
		snowball.setShooter(e.getPlayer());
		snowball.setVelocity(e.getPlayer().getLocation().getDirection().multiply(2.5));
		e.setCancelled(true);
	}
	
	public void snowballHitPlayer(GamePlayer target, GamePlayer shooter) {
		DamageManager.damage(target, shooter, 2);
	}
	
	@EventHandler
	public void hitSnowball(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Snowball == false) return;
		Snowball snowball = (Snowball) e.getEntity();
		if (snowball.getShooter() instanceof Player == false) {
			System.out.print("Snowball was not shot by a Player");
			return;
		}
		Player sh = (Player) snowball.getShooter();
		for (Entity entity:snowball.getNearbyEntities(1.5, 3, 1.5)) {
			if (entity instanceof Player == false) continue;
			Player tg = (Player) entity;
			if (!Duckhunt.isInGame(tg)) continue;
			Arena arena = Duckhunt.getArena(tg);
			GamePlayer target = arena.getGamePlayer(tg);
			GamePlayer shooter = arena.getGamePlayer(sh);
			if (arena.getTeam(tg)!=Team.Ducks) {
				System.out.print("Target was not a Duck");
				continue;
			}
			snowballHitPlayer(target, shooter);
		}
	}
	
	public static void freezerPillarPointHighlighter(GamePlayer gp) {
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (gp.getType() != GameClassType.Freezer) {
					this.cancel();
					return;
				}
				
				if (Tools.isItem(gp.getPlayer().getItemInHand(), Config.getIcePillar(gp.getArena(), gp.getPlayer()))) {
					Block block = gp.getTargetBlock(50, Material.ICE);
					if (block.getRelative(0, 1, 0).getType()!=Material.AIR) return;
					Location pos = block.getLocation().add(0.5, 1, 0.5);
					
					block.getRelative(0, 1, 0).getWorld().spawnParticle(Particle.SNOWBALL, pos, 5, 0, 0, 0, 0);	
				}
			}
			
		}.runTaskTimer(Duckhunt.plugin, 5, 5);
	}
	
	private void growPillar(Block block) {
		icepillars.add(block.getLocation());
		if (block.getType()!=Material.AIR) return;
		block.setType(Material.ICE);
	}
	
	private static void clearPillar(Block block) {
		if (block.getType()!=Material.ICE) return;
		block.setType(Material.AIR);
		icepillars.remove(block.getLocation());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void createPillar(PlayerInteractEvent e) {
		if (!Duckhunt.isInGame(e.getPlayer())) return;
		Arena arena = Duckhunt.getArena(e.getPlayer());
		GamePlayer player = arena.getGamePlayer(e.getPlayer());
		if (player==null) return;
		if (player.getType()!=GameClassType.Freezer) return;
		if (Tools.isItem(e.getPlayer().getItemInHand(), Config.getIcePillar(arena, e.getPlayer())) == false) return;
		if (e.getAction()!=Action.LEFT_CLICK_BLOCK && e.getAction()!=Action.LEFT_CLICK_AIR) return;
		if (player.getItemCount(Config.getIcePillar(arena, e.getPlayer())) <= 0) return;
		final Block block = player.getTargetBlock(50, Material.ICE);
		player.removeItem(Config.getIcePillar(arena, e.getPlayer()));
		new BukkitRunnable() { 
			int countdown = 40;
			@Override
			public void run() {
				if (countdown==40) growPillar(block.getRelative(0, 1, 0));
				if (countdown==39) growPillar(block.getRelative(0, 2, 0));
				if (countdown==38) growPillar(block.getRelative(0, 3, 0));
				if (countdown==3) clearPillar(block.getRelative(0, 3, 0));
				if (countdown==2) clearPillar(block.getRelative(0, 2, 0));
				if (countdown==1) clearPillar(block.getRelative(0, 1, 0));
				if (countdown<1) this.cancel();
				countdown+=-1;
			}
		}.runTaskTimer(Duckhunt.plugin, 0, 5);
	}
	
	public static void removeAllIcePillars() {
		for (Location location:icepillars) {
			location.getBlock().setType(Material.AIR);
		}
	}
	
}
