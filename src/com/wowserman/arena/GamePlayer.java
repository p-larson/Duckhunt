package com.wowserman.arena;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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
	
	/**
	 * @return The Player assosiated with the GamePlayer instnace.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * @return The GameClassType of the GamePlayer.
	 */
	public GameClassType getType() {
		return type;
	}
	
	/**
	 * @return The Arena the GamePlayer is assosiated with.
	 */
	public Arena getArena() {
		return arena;
	}
	
	/**
	 * @return The rate of which the GamePlayer will be Refilled in Minecraft Ticks.
	 */
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
	
	/**
	 * @param item The ItemStack we're getting the Count of.
	 * @return the Count of Items the Player's Inventory has.
	 */
	public int getItemCount(ItemStack item) {
		Material material = item.getType();
		int amount = 0;
		
		for (ItemStack is:player.getInventory().getContents()) {
			if (is!=null && is.getType()==material) amount = amount + is.getAmount(); 
		}
		return amount;
	}
	
	/**
	 * @param item The ItemStack we're removing from the Player's Inventory.
	 */
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
	
	/**
	 * @param item The ItemStack we're removing all of in the Player's Inventory.
	 */
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
	
	/**
	 * @param item The ItemStack we're adding to the Player's Inventory.
	 */
	public void spawnItem(ItemStack item) {
		ItemStack[] items = player.getInventory().getContents();
		int i = 0;
		for (ItemStack is:player.getInventory().getContents()) {
			if (is==null || is.getType()==Material.AIR) {
				items[i]=item;
				player.getInventory().setContents(items);
				break;
			}
			i=i+1;
		}
	}
	
	/**
	 *  Remove the Item in the Player's Hand.
	 */
	public void removeItemInHand() { 
		ItemStack[] items = player.getInventory().getContents();
		int slot = player.getInventory().getHeldItemSlot();
		items[slot].setType(Material.AIR);
		player.getInventory().setContents(items);
	}
	
	/**
	 * @param itemstack The ItemStack we're adding to the Player's Inventory.
	 * @param slot The Slot we're Adding the ItemStack to.
	 */
	public void spawnItem(ItemStack itemstack, int slot) {
		ItemStack[] items = player.getInventory().getContents();
		items[slot]=itemstack;
		player.getInventory().setContents(items);
		player.updateInventory();
	}
	
	/**
	 * @param item The ItemStack we're adding to the Player's Inventory.
	 */
	public void addItem(ItemStack item) {
		player.getInventory().addItem(item);
	}
	
	/**
	 * @param range The Range of Blocks we're going to Look through.
	 * @return The Block the Player is Looking at.
	 */
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
	
	/**
	 * @param range The Range of Blocks we're going to Look through.
	 * @param cantBe The Material we're going to ignore while iterating through the Blocks in the Player's Direction.
	 * @return The Block the Player is Looking at.
	 */
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
	
	/**
	 * @return The Team Preference of the Player.
	 */
	public Team getTeamPreference() {
		if (arena.getPreferedTeam(player)!=null) return arena.getPreferedTeam(player);
		else return Team.None;
	}
	
	/**
	 * Reset the Player's Inventory.
	 */
	public void restoreInventory() {
		player.getInventory().setContents(this.getInventory().getContents());
		player.updateInventory();
		player.setHealth(20);
	}
	
	/**
	 * @param type The GameClassType we're changing to.
	 */
	public void changeType(GameClassType type) {
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
	
	/**
	 * @return The Inventory based on the GameClassType of the Player.
	 */
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
	
	/**
	 * Reset's Health.
	 */
	public void heal() {
		player.setHealth(20);
		player.setFoodLevel(20);
	}
	
	private boolean isShooting() {
		return Barrager.isShooting(player);
	}
	
	/**
	 * Restocks the GamePlayer of items they may have used.
	 */
	public void refill() {
		switch (type) {
		case Runner:
			if (this.getItemCount(Config.getSpeedFeather(arena, player)) < 3) {
				this.spawnItem(Config.getSpeedFeather(arena, player));
				this.player.playSound(player.getLocation(), Sound.ENTITY_HORSE_SADDLE, 0.5f, 0.5f);
			}
			break;
		case Healer:
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 2));
			break;
		case Smoker:
			if (this.getItemCount(Config.getSmokeGrenade(arena, player)) < 1) {
				this.spawnItem(Config.getSmokeGrenade(arena, player));
				this.player.playSound(player.getLocation(), Sound.ENTITY_HORSE_SADDLE, 0.5f, 0.5f);
			}
			break;
		case Cloaker:
			if (this.getItemCount(Config.getInvisibilityCloak(arena, player)) < 1) {
				this.spawnItem(Config.getInvisibilityCloak(arena, player));
				this.player.playSound(player.getLocation(), Sound.ENTITY_HORSE_SADDLE, 0.5f, 0.5f);
			}
			break;
		case Cyborg:
			if (this.getItemCount(Config.getPlasma(arena, player)) < 5) {
				this.spawnItem(Config.getPlasma(arena, player));
				this.player.playSound(player.getLocation(), Sound.ENTITY_HORSE_SADDLE, 0.5f, 0.5f);
			}
			break;
		case Pyrotech:
			if (this.getItemCount(Config.getFireball(arena, player)) < 3) {
				this.spawnItem(Config.getFireball(arena, player));
				this.player.playSound(player.getLocation(), Sound.ENTITY_HORSE_SADDLE, 0.5f, 0.5f);
			}
			break;
		case Barrager:
			if (!this.isShooting() && this.getItemCount(Config.getRustyArrow(arena, player)) < 8) {
				this.spawnItem(Config.getRustyArrow(arena, player));
				this.player.playSound(player.getLocation(), Sound.ENTITY_HORSE_SADDLE, 0.5f, 0.5f);
			}
			break;
		case Freezer:
			if (this.getItemCount(Config.getIcePillar(arena, player)) < 5) {
				this.addItem(Config.getIcePillar(arena, player));
				this.player.playSound(player.getLocation(), Sound.ENTITY_HORSE_SADDLE, 0.5f, 0.5f);
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
					this.cancel(); 
					return;
				}
				if (selectedGameClass != getType()) {
					refillclock();
					this.cancel();
					return;
				}
				refill();
			}
		}.runTaskTimer(Duckhunt.plugin, (long) (this.getRefillRate() * 20), (long) (this.getRefillRate() * 20));
	}
	
	/**
	 * Initialize a new GamePlayer.
	 * 
	 * @param arena The Arena the Player's in.
	 * @param player The Player the GamePlayer is assosiated with.
	 */
	public GamePlayer(Arena arena, Player player) {
		this.arena = arena;
		this.player = player;
		this.type = GameClassType.None;
		this.refillclock();
	}
}

