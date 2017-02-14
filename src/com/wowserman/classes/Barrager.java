package com.wowserman.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.util.Vector;

import com.wowserman.Config;
import com.wowserman.DamageManager;
import com.wowserman.Duckhunt;
import com.wowserman.Tools;
import com.wowserman.arena.Arena;
import com.wowserman.arena.GameClassType;
import com.wowserman.arena.GamePlayer;
import com.wowserman.arena.Team;

public class Barrager implements Listener {

	private List<Arrow> arrows = new ArrayList<Arrow>();
	private static List<String> drawing = new ArrayList<String>();

	public static boolean isShooting(Player player) {
		return drawing.contains(player.getName());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void drawBow(PlayerInteractEvent e) {
		if (!Duckhunt.isInGame(e.getPlayer())) return;
		Arena arena = Duckhunt.getArena(e.getPlayer());
		GamePlayer player = arena.getGamePlayer(e.getPlayer());
		if (player==null) return;
		if (player.getType()!=GameClassType.Barrager) return;
		if (e.getAction()!=Action.RIGHT_CLICK_AIR && e.getAction()!=Action.RIGHT_CLICK_BLOCK) return;
		if (!Tools.isItem(player.getPlayer().getItemInHand(), Config.getRustyBow(arena, player.getPlayer()))) return;
		if (player.getItemCount(Config.getRustyArrow(arena, player.getPlayer()))<=0) return;
		if (!drawing.contains(player.getPlayer().getName())) drawing.add(player.getPlayer().getName());
	}
	
	@EventHandler
	public void shoot(EntityShootBowEvent e) {
		if (e.getEntity() instanceof Player != true) return;
		if (e.getProjectile() instanceof Arrow != true) return;
		Player player = (Player) e.getEntity();
		if (!Duckhunt.isInGame(player)) return;
		Arena arena = Duckhunt.getArena(player);
		if (arena.getGameClassType(player)!=GameClassType.Barrager) return;
		Arrow arrow = (Arrow) e.getProjectile();
		arrow.setVelocity(arrow.getVelocity().multiply(2.5));
		GamePlayer gp = arena.getGamePlayer(player);
		drawing.remove(player.getName());
		int arrowcount = gp.getItemCount(Config.getRustyArrow(arena, player)) * 3;
		Random r = new Random();
		while (arrowcount > 1) {
			Vector v = e.getProjectile().getVelocity();
			v.add(new Vector(r.nextInt(50)/100-0.25, 0, r.nextInt(50)/100-0.25));
			Arrow a = player.getWorld().spawnArrow(e.getProjectile().getLocation().add(v), player.getLocation().getDirection().multiply(1.5), 3, 6);
			a.setShooter(player);
			arrows.add(a);
			arrowcount += -1;
		}
		gp.removeAll(Config.getRustyArrow(arena, player));
		arrow.remove();
		player.playSound(player.getLocation(), Sound.ENTITY_SKELETON_SHOOT, 3f, 3f);
	}
	
	@EventHandler
	public void switchSlot(PlayerItemHeldEvent e) {
		Player player = e.getPlayer();
		if (!Duckhunt.isInGame(player)) return;
		drawing.remove(player.getName());
	}
	
	@EventHandler
	public void hit(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Arrow == false) return;
		Arrow arrow = (Arrow) e.getEntity();
		if (!arrows.contains(arrow)) return;
		arrow.remove();
		arrows.remove(arrow);
		if (arrow.getShooter() instanceof Player == false) return;
		Player sh = (Player) arrow.getShooter();
		if (!Duckhunt.isInGame(sh)) return;
		if (e.getHitEntity() instanceof Player == false) return;
		Player tg = (Player) e.getHitEntity();
		if (!Duckhunt.isInGame(tg)) return;
		Arena arena = Duckhunt.getArena(tg);
		GamePlayer target = arena.getGamePlayer(tg);
		GamePlayer shooter = arena.getGamePlayer(sh);
		if (arena.getTeam(tg)!=Team.Ducks) return;
		arrowHit(target, shooter);
	}
	
	public void arrowHit(GamePlayer target, GamePlayer shooter) {
		DamageManager.damage(target, shooter, 2);
		target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.BLOCK_ANVIL_PLACE, 1f, 1f);
		shooter.getPlayer().playSound(shooter.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f);
	}
}
