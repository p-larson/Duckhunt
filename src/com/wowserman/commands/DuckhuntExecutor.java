package com.wowserman.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wowserman.Config;
import com.wowserman.Duckhunt;
import com.wowserman.Message;
import com.wowserman.Tools;
import com.wowserman.arena.Arena;
import com.wowserman.arena.ArenaEditor;
import com.wowserman.arena.ArenaStatus;
import com.wowserman.bungee.BungeeArena;
import com.wowserman.bungee.BungeeCommunication;

public class DuckhuntExecutor implements CommandExecutor {
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
		
		if (sender instanceof Player == false) {
			sender.sendMessage("You must be a Player to Perform any Duckhunt related Commands.");
			return false;
		}
		
		Player player = (Player) sender;
		
		if (args.length <= 0) { 
			// Send Help Message
			Message message = player.isOp() ? new Message(Config.opHelp, null, player) : new Message(Config.nonopHelp, null, player);
			message.send();
			return true;
		}
		
		if (args[0].equalsIgnoreCase("list")) {
			// Do something
			String arenalist = "";
			for (Arena arena:Duckhunt.arenas) arenalist = arenalist + arena.getName() + ", ";
			if (arenalist.endsWith(", ")) arenalist = arenalist.substring(0, arenalist.length()-2);
			if (arenalist.length() == 0) arenalist = "none";
			new Message(arenalist, null, player).send();
			return true;
		}
		
		if (args[0].equalsIgnoreCase("create")) {
			if (!player.isOp()) {
				new Message(Config.playerLackingPermission, null, player).send();
				return false;
			}
			if (args.length < 2) {
				new Message("/duckhunt create (arenaname)", null, player).send();
				return false;
			}
			
			String key = args[1].toLowerCase();
			if (Duckhunt.getArenaOfName(key)==null) {
				Duckhunt.arenaStorage.createArena(key);
				new Message(Config.createdArena, Duckhunt.getArenaOfName(key), player).send();
				return true;
			} else {
				new Message(Config.arenaAlreadyExists, Duckhunt.getArenaOfName(key), player).send();
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("delete")) {
			if (!player.isOp()) {
				new Message(Config.playerLackingPermission, null, player).send();
				return false;
			}
			if (args.length < 2) {
				new Message("/duckhunt delete (arena)", null, player).send(); 
				return true;
			} else {
				String key = args[1];
				Arena arena = Duckhunt.getArenaOfName(key);
				if (arena != null) {
					Duckhunt.arenaStorage.configfile.set(key, null);
					new Message("&a" + key + " Deleted Successfully.", null, player).send();
					return true;
				} else {
					new Message(Config.arenaDoesntExist, new Arena(args[1], null, 0, 0, null, null, null, null), player).send();
					return true;
				}
			}
		}
		
		if (args[0].equalsIgnoreCase("join")) {
			if (args.length < 2) {
				new Message(Config.joinHelp, null, player).send();
				return true;
			}
			
			if (args.length >= 2) {
    			//Join Arena
    			Arena arena = Duckhunt.getArenaOfName(args[1]);
    			if (arena != null) {
    				arena.join(player);
    				return true;
    			} else {
    				new Message(Config.arenaDoesntExist, new Arena(args[1], null, 0, 0, null, null, null, null), player).send();
    				return true;
    			}
    		}
		}
		
		if (args[0].equalsIgnoreCase("leave")) {
			// Do something
			if (Duckhunt.isPlayerInGame(player)) {
				Duckhunt.getArena(player).leave(player);
				return true;
			} else {
				new Message(Config.playerCantLeaveNotInGame, Duckhunt.getArena(player), player).send();
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("setlobby")) {
			if (!player.isOp()) {
				new Message(Config.playerLackingPermission, null, player).send();
				return false;
			}
			Config.setLobby(player.getLocation());
			new Message("§aSaved Lobby Location", null, player).send();
			return true;
		}
		
		if (args[0].equalsIgnoreCase("savebook")) {
			if (!player.isOp()) {
				new Message(Config.playerLackingPermission, null, player).send();
				return false;
			}
			if (player.getItemInHand().getType()==Material.WRITTEN_BOOK) {
    			ItemStack book = player.getItemInHand();
    			Duckhunt.bookStorage.write(book);
    			new Message("&aSaved Book Successfully.", null, player).send();
    			return true;
    		} else {
    			new Message("&cCannot Save this Item! It isn't a Book!", null, player).send();
    			return true;
    		}
		}
		
		if (args[0].equalsIgnoreCase("forcestart")) {
			if (!player.isOp()) {
				new Message(Config.playerLackingPermission, null, player).send();
				return false;
			}
			// Do something
			if (args.length < 2) {
				new Message("/duckhunt forcestart (arena)", null, player).send();
				return true;
			}
			Arena arena = Duckhunt.getArenaOfName(args[1]);
			if (arena != null) {
				arena.startGame();
				return true;
			} else {
				new Message(Config.arenaDoesntExist, new Arena(args[1], null, 0, 0, null, null, null, null), player).send();
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("forcestop")) {
			if (!player.isOp()) {
				new Message(Config.playerLackingPermission, null, player).send();
				return false;
			}
			// Do something
			if (args.length < 2) {
				// Force stop Help
				new Message("/duckhunt forcestop (arena)", null, player).send();
				return true;
			}
			Arena arena = Duckhunt.getArenaOfName(args[1]);
			if (arena != null) {
				arena.startEnding();
				return true;
			} else {
				new Message(Config.arenaDoesntExist, new Arena(args[1], null, 0, 0, null, null, null, null), player).send();
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("edit")) {
			if (!player.isOp()) {
				new Message(Config.playerLackingPermission, null, player).send();
				return false;
			}
			if (args.length < 3) {
				new Message("/duckhunt edit (arena)", null, player).send();
				return true;
			}
			Arena arena = Duckhunt.getArenaOfName(args[1]);
			if (arena != null) {
				ArenaEditor.arenaEditted = arena;
				player.openInventory(ArenaEditor.edit());
				new Message("Opening Editor...", arena, player).send();
				return true;
			} else {
				new Message(Config.arenaDoesntExist, new Arena(args[1], null, 0, 0, null, null, null, null), player).send();
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("joinsign")) {
			if (!player.isOp()) {
				new Message(Config.playerLackingPermission, null, player).send();
				return false;
			}
			if (args.length < 2) {
				new Message("/duckhunt joinsign (arena)", null, player).send();
				return true;
			}
			Arena arena = Duckhunt.getArenaOfName(args[1]);
			if (arena != null) {
				Location targetLocation = Tools.getTargetBlock(player, 10, Material.AIR).getLocation();
				Duckhunt.signStorage.createArenaSign(targetLocation, arena, false);
				new Message("Created Arena Sign.", arena, player).send();
				return true;
			} else {
				new Message(Config.arenaDoesntExist, new Arena(args[1], null, 0, 0, null, null, null, null), player).send();
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("leavesign")) {
			if (!player.isOp()) {
				new Message(Config.playerLackingPermission, null, player).send();
				return false;
			}
			if (args.length < 2) {
				new Message("/duckhunt leavesign (arena)", null, player).send();
				return true;
			}
			Arena arena = Duckhunt.getArenaOfName(args[1]);
			if (arena != null) {
				Location targetLocation = Tools.getTargetBlock(player, 10, Material.AIR).getLocation();
				Duckhunt.signStorage.createArenaSign(targetLocation, arena, true);
				new Message("Created Arena Sign.", arena, player).send();
				return true;
			} else {
				new Message(Config.arenaDoesntExist, new Arena(args[1], null, 0, 0, null, null, null, null), player).send();
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("autojoin")) {
			// Do something
			
			Arena bestArena = null;
			for (Arena arena:Duckhunt.getArenas()) {
				if (arena.getStatus()!=ArenaStatus.Recruiting) continue;
				if (bestArena==null) bestArena = arena;
				
				if ((arena.getPlayerCount() / arena.getMaxPlayers()) > (bestArena.getPlayerCount() / bestArena.getMaxPlayers())) {
					bestArena = arena;
					continue;
				}
			}
			
			if (bestArena==null) new Message(Config.noGoodArenaAvalible, null, player).send();
			else bestArena.join(player);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("spectate")) {
			if (args.length < 2) {
				new Message(Config.spectateHelp, null, player).send();
				return true;
			}
			
			if (args.length >= 2) {
    			//Join Arena
    			Arena arena = Duckhunt.getArenaOfName(args[1]);
    			if (arena != null) {
    				arena.spectate(player);
    				return true;
    			} else {
    				new Message(Config.arenaDoesntExist, new Arena(args[1], null, 0, 0, null, null, null, null), player).send();
    				return true;
    			}
    		}
		}
		
		if (args[0].equalsIgnoreCase("connect")) {
			// Do something
			if (!Config.bungeeEnabled) {
				new Message("&cSorry but Bungee-Mode isn't Enabled.", null, player).send();
				return true;
			}
			if (args.length < 1) {
				new Message("/duckhunt connect ", null, player).send();
				return true;
			}
			
			BungeeCommunication.sendMessage("connect " + player.getName() + " " + args[1]);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("autoconnect")) {
			if (!Config.bungeeEnabled) {
				new Message("&cSorry but Bungee-Mode isn't Enabled.", null, player).send();
				return true;
			}
			// Do something
			BungeeArena bestArena = null;
			for (BungeeArena arena:Duckhunt.getBungeeArenas()) {
				if (arena.getStatus()!=ArenaStatus.Recruiting) continue;
				if (bestArena==null) bestArena = arena;
				
				if ((arena.getPlayerCount() / arena.getMaxPlayers()) > (bestArena.getPlayerCount() / bestArena.getMaxPlayers())) {
					bestArena = arena;
					continue;
				}
			}
			
			if (bestArena==null) new Message(Config.noGoodServerAvalible, null, player).send();
			else BungeeCommunication.sendMessage("connect " + player.getName() + " " + bestArena.getServerName());
			
		}
		
		if (args[0].equalsIgnoreCase("bungeesign")) {
			if (!player.isOp()) {
				new Message(Config.playerLackingPermission, null, player).send();
				return false;
			}
			// Do something
			if (!Config.bungeeEnabled) {
				new Message("&cSorry but Bungee-Mode isn't Enabled.", null, player).send();
				return true;
			}
			if (args.length < 2) {
				new Message("/duckhunt bungeesign (servername)", null, player).send();
				return true;
			}
			BungeeArena arena = Duckhunt.getBungeeArenaOfServerName(args[1]);
			if (arena != null) {
				Location targetLocation = Tools.getTargetBlock(player, 10, Material.AIR).getLocation();
				Duckhunt.signStorage.createBungeeArenaSign(targetLocation, arena);
				new Message("Created Bungee Sign.", null, player).send();
				return true;
			} else {
				new Message(Config.arenaDoesntExist, new Arena(args[1], null, 0, 0, null, null, null, null), player).send();
				return true;
			}
		}

		if (player.isOp()) new Message(Config.opHelp, null, player).send();
		else new Message(Config.nonopHelp, null, player).send();
		
		return true;
	}

}
