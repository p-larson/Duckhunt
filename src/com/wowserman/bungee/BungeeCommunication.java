package com.wowserman.bungee;

import java.io.ByteArrayInputStream;	
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import com.wowserman.Config;
import com.wowserman.Duckhunt;
import com.wowserman.arena.ArenaStatus;

public class BungeeCommunication implements PluginMessageListener {
	
	public static String serverAddress = "localhost";
	public static int serverPort = 25565;
	public static String serverName = null;
	public static String communicationChannel = "duckhunt";
	
	public static String getServerStatusMessage() {
		return serverName + " " + Duckhunt.getMainArena().getName() + " " + Duckhunt.getMainArena().getStatus().toString() 
				+ " " + Duckhunt.getMainArena().getMaxPlayers() + " " + Duckhunt.getMainArena().getPlayerCount() + " "
				+ Duckhunt.getMainArena().getName();
	}
	
	public static String getChannel() {
		return communicationChannel;
	}
	
	public static void sendMessage(String message) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(stream);
		try {
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().sendPluginMessage(Duckhunt.plugin, communicationChannel, stream.toByteArray());
	}
	
	@Override 
	public void onPluginMessageReceived(String channel, Player p, byte[] bytes) {
		if (!channel.equals(BungeeCommunication.communicationChannel)) return;
		ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
		DataInputStream in = new DataInputStream(stream);
		try {
			final String[] message = in.readUTF().split(" ");
			if (message.length>0 != true) return;
			// Check type of Request
			if (message[0].equalsIgnoreCase("status")) {
				// Bungeecord is requesting the Server's Status.
				// Send them: Name of Server, Status of Server, # of Online Players, # of Max Players, Name of Arena
				if (serverName!=null && Duckhunt.getMainArena()!=null) 
					BungeeCommunication.sendMessage("statusupdate " + BungeeCommunication.getServerStatusMessage());
			}
			
			if (message[0].equalsIgnoreCase("name")) {
				// Bungeecord is sending us the name of our server.
				serverName = message[1];
				BungeeCommunication.sendMessage("servers " + serverName);
			}
			
			if (message[0].equalsIgnoreCase("server")) {
				/*
				 * The ProxyServer is giving an Update on a Server's Status, Players, Max Players, and Arena
				 * [1] is the Server Name (Which Server the Information is about)
				 * [2] is the Status (The Status of the Server)
				 * [3] is the Player Count (Amount of Players on the Server)
				 * [4] is the Max Players Amount (Max Players game can hold)
				 * [5] is the Arena Name (Name of Arena the Server is playing on)
				 */
				
				if (!(message.length>5)) return; // Message doesn't cover all of the 5 needed Arguments: exit
				
				String serverName = message[1];
				String status = message[2];
				int playerCount = 0; try { playerCount = Integer.parseInt(message[3]); } catch (Exception e) {}
				int maxPlayers = 0; try { maxPlayers = Integer.parseInt(message[4]); } catch (Exception e) {}
				String arenaName = message[5];

				if (Duckhunt.getBungeeArenaOfServerName(serverName)==null) new BungeeArena(serverName);
				BungeeArena arena = Duckhunt.getBungeeArenaOfServerName(serverName);
				arena.setStatus(ArenaStatus.valueOf(status));
				arena.setPlayerCount(playerCount);
				arena.setMaxPlayers(maxPlayers);
				arena.setArenaName(arenaName);
				arena.validate();
			}
			
		} catch (IOException e) { /* Ah! We caught a bug! Let's Do Nothing about it :P */ }
	}

	private void bungeeStartup() {
		
		if (Config.bungeeEnabled == false) return;
		
		/*
		 * Bungee is enabled but hasn't been able to set-up properly.
		 * 
		 * Called on Server Start-Up and when a Player Joins
		 * 
		 * Going to try and set it up by:
		 * - Estabilishing Connection with ProxyServer
		 * - Asking for Name of Server
		 * - Declaring the Arena of the Server
		 */
		
		new BukkitRunnable() {
			@Override
			public void run() {
				
				if (Bukkit.getOnlinePlayers().size()==0) return;
				
				if (serverName!=null) {
					System.out.print("Successfully Estabilished Connection with Proxy Server with " + serverName);
					/*
					 * Ask for an update on every server and give them an update on ourselves.
					 */
					
					new BukkitRunnable() {

						@Override
						public void run() {
							if (Duckhunt.getMainArena()!=null) {
								BungeeCommunication.sendMessage("statusupdate " + getServerStatusMessage());
							}
							BungeeCommunication.sendMessage("get-server-statuses " + serverName);
						}
						
					}.runTaskTimer(Duckhunt.plugin, 0, 20);
					this.cancel();
				} else {
					System.out.print("Duckhunt is Attempting to Establish Connection with Bungeecord's Proxy Server...");
					// Set the Arena of the Server
					
					if (Duckhunt.getMainArena()==null) {
						// Could not get a Valid Arena. Cancel this.
						System.out.print("Duckhunt has no Valid Main Arena Set. Ignoring it. \nIf this Server is a Lobby Server sort of thing, this is fine. Ignore this Message.");
					} else System.out.print("Duckhunt has a Valid Main Arena.");
					
					sendMessage("name");
				}
				
			}
			
		}.runTaskTimer(Duckhunt.plugin, 0, 20);
	}

	public BungeeCommunication() {
		
		System.out.print("Started BungeeCommunication.");
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(Duckhunt.plugin, BungeeCommunication.getChannel());
        Bukkit.getMessenger().registerIncomingPluginChannel(Duckhunt.plugin, BungeeCommunication.getChannel(), this);
		
		this.bungeeStartup();
	}
}
