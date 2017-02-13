package com.wowserman.tools;

import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.wowserman.Duckhunt;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;

public class Fireworks {

	public static void spawnFirework(Player player) {
		
		Builder fb = FireworkEffect.builder();
		Random r = new Random();
		FireworkEffect f = null;
		fb.withColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
		fb.with(Type.BALL_LARGE);
		if (r.nextBoolean()==true) fb.withFlicker();
		f = fb.build();
		
		Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
 		FireworkMeta fwm = firework.getFireworkMeta();
 		fwm.clearEffects();
 		fwm.addEffect(f);
 		fwm.setPower(r.nextInt(3) + 2);
 		firework.setFireworkMeta(fwm);
 		
	}
	
	public static void createFirework(Location location, int r, int g, int b, Type type, boolean explodeOnSpawn, boolean randomColor, int power, Vector vector) {
		Builder fb = FireworkEffect.builder();
		Random random = new Random();
		FireworkEffect f = null;
		if (randomColor == true) fb.withColor(Color.fromRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
		else fb.withColor(Color.fromRGB(r, g, b));
		fb.with(type);
		if (random.nextBoolean()==true) fb.withFlicker();
		f = fb.build();
		
		Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
 		FireworkMeta fwm = firework.getFireworkMeta();
 		fwm.clearEffects();
 		fwm.addEffect(f);
 		fwm.setPower(power);
 		firework.setFireworkMeta(fwm);
 		if (vector != null) firework.setVelocity(vector);
 		if (explodeOnSpawn==true) {
 			new BukkitRunnable() {
				@Override
				public void run() {
					firework.detonate();
				}
 			}.runTaskLater(Duckhunt.plugin, 3);
 		}
	}
}
