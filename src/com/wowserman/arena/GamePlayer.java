package com.wowserman.arena;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import com.wowserman.Config;
import com.wowserman.Duckhunt;
import com.wowserman.Message;
import com.wowserman.classes.Barrager;
import com.wowserman.classes.Freezer;

public class GamePlayer {
	
	private GameClassType type;
	private Arena arena;
	private Player player;
	
	public Player getPlayer() {
		return player;
	}
	
	public GameClassType getType() {
		return type;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	private Double getRefillRate() {
		switch (type) {
		case Runner:
			return 15d;
		case Healer:
			return 1d;
		case Smoker:
			return 20d;
		case Cloaker:
			return 30d;
		case Cyborg:
			return 5d;
		case Pyrotech:
			return 4d;
		case Barrager:
			return 0.5d;
		case Freezer:
			return 5d;
		case None:
			return 1d;
		}
		return 1d;
	}
	
	public int getItemCount(ItemStack item) {
		Material material = item.getType();
		int amount = 0;
		
		for (ItemStack is:player.getInventory().getContents()) {
			if (is!=null && is.getType()==material) amount = amount + is.getAmount(); 
		}
		return amount;
	}
	
	public void removeItem(ItemStack item) {
		ItemStack[] items = player.getInventory().getContents();
		int i = 0;
		for (ItemStack is:player.getInventory().getContents()) {
			if (is!=null && is.getType()==item.getType()) {
				if (items[i].getAmount()==1) items[i]=null;
				else {
					ItemStack i2 = is;
					i2.setAmount(is.getAmount()-1);
					items[i]=i2;
				}
				player.getInventory().setContents(items);
				break;
			}
			i=i+1;
		}
	}
	
	public void removeAll(ItemStack item) {
		Material material = item.getType();
		ItemStack[] items = player.getInventory().getContents();
		int i = 0;
		for (ItemStack is:player.getInventory().getContents()) {
			if (is!=null && is.getType()==material) {
				items[i] = null;
			}
			i=i+1;
		}
		player.getInventory().setContents(items);
	}
	
	public void spawnItem(ItemStack itemstack) {
		ItemStack[] items = player.getInventory().getContents();
		int i = 0;
		for (ItemStack is:player.getInventory().getContents()) {
			if (is==null || is.getType()==Material.AIR) {
				items[i]=itemstack;
				player.getInventory().setContents(items);
				break;
			}
			i=i+1;
		}
	}
	
	public void removeItemInHand() { 
		ItemStack[] items = player.getInventory().getContents();
		int slot = player.getInventory().getHeldItemSlot();
		items[slot].setType(Material.AIR);
		player.getInventory().setContents(items);
	}
	
	public void spawnItem(ItemStack itemstack, int slot) {
		ItemStack[] items = player.getInventory().getContents();
		items[slot]=itemstack;
		player.getInventory().setContents(items);
		player.updateInventory();
	}
	
	public void addItem(ItemStack itemstack) {
		player.getInventory().addItem(itemstack);
	}
	
	public final Block getTargetBlock(int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }
	
	public final Block getTargetBlock(int range, Material cantBe) {
		BlockIterator iter = new BlockIterator(player, range);
        Block block = iter.next();
        Block lastBlock = block;
        while (iter.hasNext()) {
        	lastBlock = block;
        	block = iter.next();
            if (block.getType() == Material.AIR) {
                continue;
            }
            if (block.getType() == cantBe || block.getRelative(0, -1, 0).getType() == cantBe) {
            	block = lastBlock.getRelative(0, -1, 0);
            	while (block.getType()==Material.AIR) {
            		block = block.getRelative(0, -1, 0);
            		if (block.getY() < 0) break;
            	}
            	break;
            }
            break;
        }
        return block;
	}
	
	public Team getTeamPreference() {
		if (arena.getPreferedTeam(player)!=null) return arena.getPreferedTeam(player);
		else return Team.None;
	}
	
	public void restoreInventory() {
		player.getInventory().setContents(this.getInventory().getContents());
		player.updateInventory();
		player.setHealth(20);
	}
	
	public void changeType(GameClassType type) {
		System.out.print(player.getName() + " changing class to " + type.toString());
		switch (type) {
		case Runner:
			new Message(Config.equipRunner, arena, player).send();
			break;
		case Healer:
			new Message(Config.equipHealer, arena, player).send();
			break;
		case Smoker:
			new Message(Config.equipSmoker, arena, player).send();
			break;
		case Cloaker:
			new Message(Config.equipCloaker, arena, player).send();
			break;
		case Cyborg:
			new Message(Config.equipCyborg, arena, player).send();
			break;
		case Pyrotech:
			new Message(Config.equipPyrotech, arena, player).send();
			break;
		case Barrager:
			new Message(Config.equipBarrager, arena, player).send();
			break;
		case Freezer:
			new Message(Config.equipFreezer, arena, player).send();
			Freezer.freezerPillarPointHighlighter(this);
			break;
		case None:
			break;
		}
		this.type = type;
		this.restoreInventory();
	}
	
	public Inventory getInventory() {
		Inventory i = Bukkit.createInventory(null, InventoryType.PLAYER);
		switch (type) {
		case Runner:
			Inventory runner = Bukkit.createInventory(null, InventoryType.PLAYER);
			runner.setItem(0, Config.getSpeedFeather(arena, player));
			i = runner;
			break;
		case Healer:
			Inventory healer = Bukkit.createInventory(null, InventoryType.PLAYER);
			healer.setItem(0, Config.getHealWand(arena, player));
			i = healer;
			break;
		case Smoker:
			Inventory smoker = Bukkit.createInventory(null, InventoryType.PLAYER);
			smoker.setItem(0, Config.getSmokeGrenade(arena, player));
			i = smoker;
			break;
		case Cloaker:
			Inventory cloaker = Bukkit.createInventory(null, InventoryType.PLAYER);
			cloaker.setItem(0, Config.getInvisibilityCloak(arena, player));
			i = cloaker;
			break;
		case Cyborg:
			Inventory cyborg = Bukkit.createInventory(null, InventoryType.PLAYER);
			cyborg.setItem(0, Config.getLaserGun(arena, player));
			cyborg.setItem(1, Config.getPlasma(arena, player));
			i = cyborg;
			break;		
		case Pyrotech:
			Inventory pyrotech = Bukkit.createInventory(null, InventoryType.PLAYER);
			pyrotech.setItem(0, Config.getFireball(arena, player));
			i = pyrotech;
			break;
		case Barrager:
			Inventory barrager = Bukkit.createInventory(null, InventoryType.PLAYER);
			barrager.setItem(0, Config.getRustyBow(arena, player));
			barrager.setItem(1, Config.getRustyArrow(arena, player));
			i = barrager;
			break;
		case Freezer:
			Inventory freezer = Bukkit.createInventory(null, InventoryType.PLAYER);
			freezer.setItem(0, Config.getSnowball(arena, player));
			freezer.setItem(1, Config.getIcePillar(arena, player));
			i = freezer;
			break;
		case None:
			break;
		}
		return i;
	}
	
	public void heal() {
		player.setHealth(20);
		player.setFoodLevel(20);
	}
	
	// Only for Barragers
	private boolean isShooting() {
		return Barrager.isShooting(player);
	}
	
	public void refill() {
		System.out.print(player.getName() + " refilled.");
		switch (type) {
		case Runner:
			if (this.getItemCount(Config.getSpeedFeather(arena, player)) < 3) {
				this.spawnItem(Config.getSpeedFeather(arena, player));
			}
			break;
		case Healer:
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 2));
			break;
		case Smoker:
			if (this.getItemCount(Config.getSmokeGrenade(arena, player)) < 1) {
				this.spawnItem(Config.getSmokeGrenade(arena, player));
			}
			break;
		case Cloaker:
			if (this.getItemCount(Config.getInvisibilityCloak(arena, player)) < 1) {
				this.spawnItem(Config.getInvisibilityCloak(arena, player));
			}
			break;
		case Cyborg:
			if (this.getItemCount(Config.getPlasma(arena, player)) < 5) {
				this.spawnItem(Config.getPlasma(arena, player));
			}
			break;
		case Pyrotech:
			if (this.getItemCount(Config.getFireball(arena, player)) < 3) {
				this.spawnItem(Config.getFireball(arena, player));
			}
			break;
		case Barrager:
			if (!this.isShooting() && this.getItemCount(Config.getRustyArrow(arena, player)) < 8) {
				this.spawnItem(Config.getRustyArrow(arena, player));
			}
			break;
		case Freezer:
			if (this.getItemCount(Config.getIcePillar(arena, player)) < 5) {
				this.addItem(Config.getIcePillar(arena, player));
			}
			break;
		case None:
			break;
		}
	}
	
	private void refillclock() {
		final GameClassType selectedGameClass = this.getType();
		final GamePlayer self = this;
		new BukkitRunnable() {
			@Override
			public void run() {
				if (arena.getStatus() != ArenaStatus.InGame) return;
				if (!arena.hasPlayer(self)) {
					System.out.print("Player left Arena. Refilling Canceled for " + player.getName());
					this.cancel(); 
					return;
				}
				if (selectedGameClass != getType()) {
					System.out.print(player.getName() + " switched classes, refilling is stopping and starting back up again.");
					refillclock();
					this.cancel();
					return;
				}
				refill();
			}
		}.runTaskTimer(Duckhunt.plugin, (long) (this.getRefillRate() * 20), (long) (this.getRefillRate() * 20));
	}
	
	public GamePlayer(Arena arena, Player player) {
		this.arena = arena;
		this.player = player;
		this.type = GameClassType.None;
		this.refillclock();
	}
}

