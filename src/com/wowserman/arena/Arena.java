package com.wowserman.arena;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.wowserman.Config;
import com.wowserman.Duckhunt;
import com.wowserman.Message;
import com.wowserman.Tools;
import com.wowserman.events.ArenaEndedEvent;
import com.wowserman.events.ArenaEndingEvent;
import com.wowserman.events.ArenaRecruitingEvent;
import com.wowserman.events.ArenaStartedEvent;
import com.wowserman.events.ArenaStartingEvent;
import com.wowserman.events.GamePlayerJoinedArenaEvent;
import com.wowserman.events.GamePlayerLeftArenaEvent;
import com.wowserman.events.GamePlayerSwitchClassEvent;
import com.wowserman.tools.ActionbarReflection;
import com.wowserman.tools.Board;
import com.wowserman.tools.Fireworks;
import com.wowserman.tools.TitlesReflection;

public class Arena {

	private String name;
	private boolean enabled;
	private ArenaStatus status;
	private int minPlayers;
	private int maxPlayers;	
	private Location duckSpawn;	
	private Location hunterSpawn;	
	private Location spectatorSpawn;
	private List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
	private List<Location> checkpoints = new ArrayList<Location>();
	private Hashtable<String, Integer> lives = new Hashtable<String, Integer>();
	private Hashtable<String, Team> preferedteam = new Hashtable<String, Team>();
	private List<GamePlayer> ducks = new ArrayList<GamePlayer>();
	private List<GamePlayer> hunters = new ArrayList<GamePlayer>();
	private List<GamePlayer> spectators = new ArrayList<GamePlayer>();
	private int timer = 0;
	private Team winningteam = Team.None;
	private String lastDuckDeath = "?";
	private String lastDuckKiller = "?";
	private String duckFinisher = "?";
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean value) {
		enabled = value;
	}
	
	public String getName() {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	public int getLives(Player player) {
		if (this.lives.get(player.getName())!=null) return this.lives.get(player.getName());
		else return 0;
	}
	
	public void setLives(Player player, int value) {
		this.lives.put(player.getName(), value);
	}
	
	public ArenaStatus getStatus() {
		return status;
	}
	
	public void setStatus(ArenaStatus status) {
		this.status = status;
	}
	
	public int getTimer() {
		return timer;
	}
	
	public int getPlayerCount() {
		return this.gamePlayers.size();
	}
	
	public GamePlayer getGamePlayer(Player player) {
		GamePlayer gamePlayer = null;
		for (GamePlayer gp:this.getGamePlayers()) {
			if (gp.getPlayer().getName() == player.getName()) gamePlayer = gp;
		}
		return gamePlayer;
	}
	
	private void removePlayer(Player player) {
		gamePlayers.remove(this.getGamePlayer(player));
	}
	
	private void addPlayer(Player player) {
		GamePlayer gp = new GamePlayer(this, player);
		if (gamePlayers.contains(gp)) return;
		gamePlayers.add(gp);
	}
	
	public List<GamePlayer> getGamePlayers() {
		return gamePlayers;
	}
	
	public List<Player> getPlayers() {
		List<Player> l = new ArrayList<Player>();
		for (GamePlayer p:this.getGamePlayers()) l.add(p.getPlayer());
		return l; 
	}
	
	public boolean hasPlayer(GamePlayer player) {
		return this.getGamePlayers().contains(player);
	}
	
	public int getMinPlayers() {
		return minPlayers;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public GameClassType getGameClassType(Player player) {
		
		for (GamePlayer gm:this.getGamePlayers())
			if (gm.getPlayer().getName()==player.getName()) return gm.getType();
		return GameClassType.None;
	}
	
	public Location getDuckSpawn() {
		return duckSpawn;
	}
	
	public Location getHunterSpawn() {
		return hunterSpawn;
	}
	
	public List<Location> getCheckpoints() {
		return checkpoints;
	}
	
	public int getDuckCount() {
		return ducks.size();
	}
	
	public int getHunterCount() {
		return hunters.size();
	}
	
	public int getSpectatorCount() {
		return spectators.size();
	}
	
	public boolean canStart() {
		return (this.getPlayerCount() >= this.minPlayers);
	}
	
	private GamePlayer getGamePlayerWithoutTeamPreference() {
		for (GamePlayer gp:this.getGamePlayers()) {
			if (gp.getTeamPreference()==Team.None) return gp;
		} return null;
	}
	
	public void splitIntoTeams() {
		List<GamePlayer> p = new ArrayList<GamePlayer>(this.getGamePlayers()); // All Game Players reference
		List<GamePlayer> d = new ArrayList<GamePlayer>(); // ducks
		List<GamePlayer> h = new ArrayList<GamePlayer>(); // hunters
		
		Integer neededhunters = 1;
		for (int i = p.size(); i >= 4;i+=-4) {
			neededhunters += 1;
		}
		// Add every player that prefers hunters to the hunters team if there is room for them.
		for (GamePlayer gp:this.getGamePlayers()) {
			if (this.getPreferedTeam(gp.getPlayer())==Team.Hunters) {
				if (h.size() < neededhunters) {
					h.add(gp);
					p.remove(gp);
				}
			}
		}
		
		// Get more hunters while there isn't enough needed hunters
		while (h.size() < neededhunters) {
			if (getGamePlayerWithoutTeamPreference()==null) break;
			else {
				GamePlayer gp = getGamePlayerWithoutTeamPreference();
				p.remove(gp);
				h.add(gp);
			}
		}
		
		// Has the closest amount of hunters we can get.
		d = p;
		for (GamePlayer duck:d) {
			this.joinTeam(Team.Ducks, duck.getPlayer());
		}
		for (GamePlayer hunter:h) {
			this.joinTeam(Team.Hunters, hunter.getPlayer());
		}
	}
	
	public void teleportToSpawn(Player player) {
		switch (this.getTeam(player)) {
		case Ducks:
			player.teleport(this.duckSpawn);
			break;
		case Hunters:
			player.teleport(this.hunterSpawn);
			break;
		case Spectators:
			player.teleport(this.spectatorSpawn);
			break;
		case None:
			player.teleport(this.spectatorSpawn);
		}
	}
	
	public void changeClass(GamePlayer gp, GameClassType type) {
		GamePlayerSwitchClassEvent e = new GamePlayerSwitchClassEvent(gp, gp.getType(), type);
		Bukkit.getPluginManager().callEvent(e);
		if (e.isCancelled()) return;
		gp.changeType(type);
	}
	
	public void changePreferedTeam(Player player, Team team) {
		if (this.getPreferedTeam(player)==team) return;
		this.preferedteam.put(player.getName(), team);
		switch (team) {
		case Ducks:
			new Message(Config.preferDucks, this, player).send();
			break;
		case Hunters:
			new Message(Config.preferHunters, this, player).send();
			break;
		case None:
			new Message(Config.preferNone, this, player).send();
			break;
		case Spectators:
			break;
		}
		this.updateScoreboard();
	}
	
	public Team getPreferedTeam(Player player) {
		if (this.preferedteam.get(player.getName())!=null) return this.preferedteam.get(player.getName());
		else return Team.None;
	}
	
	public void joinTeam(Team team, Player player) {
		GamePlayer gp = this.getGamePlayer(player);
		this.leaveTeam(player); // Leave their past team 
		switch (team) {
		case Ducks:
			this.ducks.add(gp);
			player.setGameMode(GameMode.SURVIVAL);
			player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_AMBIENT, 1f, 1f);
			break;
		case Hunters:
			this.hunters.add(gp);
			player.setGameMode(GameMode.SURVIVAL);
			player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1f, 1f);
			break;
		case Spectators:
			this.spectators.add(gp);
			player.setGameMode(GameMode.SPECTATOR);
			new Message(Config.playerEnterSpectatorMode, this, player).send();
			break;
		case None:
			player.setGameMode(GameMode.SURVIVAL);
			break;
		}
		this.teleportToSpawn(player);
	}
	
	public void leaveTeam(Player player) {
		if (this.getGamePlayer(player)==null) return;
		this.ducks.remove(this.getGamePlayer(player));
		this.hunters.remove(this.getGamePlayer(player));
		this.spectators.remove(this.getGamePlayer(player));
		this.changeClass(this.getGamePlayer(player), GameClassType.None);
		player.setGameMode(GameMode.SURVIVAL);
	}
	
	public void join(Player player) {
		if (Duckhunt.isInGame(player)) {
			new Message(Config.playerInGameCantJoin, Duckhunt.getArena(player), player).send();
			return;
		}
		if (status == ArenaStatus.Disabled) {
			new Message(Config.arenaNotEnabled, this, player).send();
			return;
		}
		if (status == ArenaStatus.InGame) {
			new Message(Config.playerCantJoinButCanSpectate, this, player).send();
			return;
		}
		if (Config.ignoreInventories==false && Tools.isInventoryEmpty(player)==false) {
			new Message(Config.playerCantJoinHasContentsInInventory, this, player).send();
			return;
		}
		this.addPlayer(player);
		this.preferedteam.put(player.getName(), Team.None);
		this.teleportToSpawn(player);
		
		if (status == ArenaStatus.Recruiting || status == ArenaStatus.Starting) {
			player.getInventory().setContents(Tools.lobbyInventory(this, player).getContents());
			player.updateInventory();
		}
		this.getGamePlayer(player).heal();
		this.broadcast(Config.joinMessage, Team.None, player);
		this.updateScoreboard();
		GamePlayerJoinedArenaEvent e = new GamePlayerJoinedArenaEvent(this.getGamePlayer(player), this);
		Bukkit.getPluginManager().callEvent(e);
	}
	
	public void spectate(Player player) {
		if (status == ArenaStatus.Disabled) {
			new Message(Config.arenaNotEnabled, this, player).send();
			return;
		}
		if (Config.ignoreInventories==false && Tools.isInventoryEmpty(player)==false) {
			new Message(Config.playerCantJoinHasContentsInInventory, this, player).send();
			return;
		}
		if (Duckhunt.isInGame(player)) {
			new Message(Config.playerIngameCantSpectate, Duckhunt.getArena(player), player).send();
			return;
		}
		if (status != ArenaStatus.InGame) {
			new Message(Config.arenaIsntInGameCantSpectate, this, player).send();
			return;
		}
		this.addPlayer(player);
		this.preferedteam.put(player.getName(), Team.None);
		this.joinTeam(Team.Spectators, player);
	}
	
	public void rejoin(Player player) {
		this.preferedteam.remove(player.getName());
		this.leaveTeam(player);
		this.preferedteam.put(player.getName(), Team.None);
		this.getGamePlayer(player).heal();
		this.getGamePlayer(player).changeType(GameClassType.None);
		this.teleportToSpawn(player);
		new Message(Config.playerAutoRejoin, this, player).send();
	}
	
	public void leave(Player player) {
		this.preferedteam.remove(player.getName());
		this.leaveTeam(player);
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		this.broadcast(Config.quitMessage, Team.None, player);
		this.updateScoreboard();
		GamePlayerLeftArenaEvent e = new GamePlayerLeftArenaEvent(this.getGamePlayer(player), this);
		Bukkit.getPluginManager().callEvent(e);
		this.removePlayer(player);
		player.teleport(Config.fallbackLocation);
		if (this.getHunterCount() <= 0 && this.getDuckCount() > 1) {
			GamePlayer selectedDuck = this.getDucks().get(new Random().nextInt(this.getDucks().size()));
			this.joinTeam(Team.Hunters, selectedDuck.getPlayer());
			this.broadcast(Config.duckSwitchToHunter, Team.None, selectedDuck.getPlayer());
		} else {
			this.broadcast(Config.arenaHasNoEligibleHunter, Team.None, null);
			this.winningteam = Team.Ducks;
		}
	}
	
	public void allPlayersLeave() {
		for (GamePlayer gp:this.getGamePlayers()) {
			Player player = gp.getPlayer();
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			player.closeInventory();
			player.getInventory().clear();
			gp.heal();
		}
		this.gamePlayers = new ArrayList<GamePlayer>();
	}
	
	public void allPlayersRejoin() {
		for (GamePlayer gp:this.getGamePlayers()) {
			this.rejoin(gp.getPlayer());
			new Message(Config.playerLeaveAfterRejoin, this, gp.getPlayer()).send();
		}
	}
	
	public void openClassSelector(Player player) {
		switch (this.getTeam(player)) {
		case Ducks:
			player.openInventory(Config.getDuckClassSelector(this, player));
			break;
		case Hunters:
			player.openInventory(Config.getHunterClassSelector(this, player));
			break;
		default:
			break;
		}
		
	}
	
	public void startGame() {
		this.splitIntoTeams();
		for (Player player:this.getPlayers()) {
			switch (this.getTeam(player)) {
			case Ducks:
				new Message(Config.duckStartMessages, this, player).send();
				break;
			case Hunters:
				new Message(Config.hunterStartMessages, this, player).send();
				break;
			default:
				break;
			}
			this.setLives(player, Config.lives);
			this.teleportToSpawn(player);
			this.openClassSelector(player);
		}
		this.startArenaClock();
	}
	
	public void startRecruiting() {
		if (status == ArenaStatus.Recruiting) return;
		status = ArenaStatus.Recruiting;
		for (GamePlayer gp:this.getGamePlayers()) {
			this.joinTeam(Team.None, gp.getPlayer());
			gp.changeType(GameClassType.None);
			this.changePreferedTeam(gp.getPlayer(), Team.None);
			gp.getPlayer().getInventory().setContents(Tools.lobbyInventory(this, gp.getPlayer()).getContents());
			gp.getPlayer().updateInventory();
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				updateScoreboard();
				updateActionbar();
				if (canStart()) {
					startCountdown();
					this.cancel();
				}
				
				if (Arena.this.getStatus()!=ArenaStatus.Recruiting) {
					this.cancel();
				}
			}
		}.runTaskTimer(Duckhunt.plugin, 0, 20);
		
		ArenaRecruitingEvent e = new ArenaRecruitingEvent(this);
		Bukkit.getPluginManager().callEvent(e);
	}

	public void startCountdown() {
		if (status == ArenaStatus.Starting) return;
		status = ArenaStatus.Starting;
		for (Player player:this.getPlayers())
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
		this.timer = 60; // 60 seconds = 1 minute;
		new BukkitRunnable() {
			@Override
			public void run() {
				updateScoreboard();
				updateActionbar();
				if (status != ArenaStatus.Starting) {
					this.cancel();
					return;
				} else if (canStart() == false) {
					// Not enough Players
					this.cancel();
					startRecruiting();
					for (Player player:getPlayers()) {
						TitlesReflection.sendTitle(player, new Message(Config.countdownCanceled, Arena.this, player).getText());
						TitlesReflection.sendSubTitle(player, new Message(Config.countdownCanceledSub, Arena.this, player).getText());
					}
				} else if (timer <= 0) {
					// Countdown is over, Game can begin.
					startGame();
					this.cancel();
				} else {
					// Countdown is still going.
					if (timer <= 10) {
						for (Player player:getPlayers()) {
							TitlesReflection.clearTitle(player);
							TitlesReflection.sendTitle(player, timer + "s");
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);
						}
					}
					timer += -1;
				}
			}
			
		}.runTaskTimer(Duckhunt.plugin, 0, 20);
		
		ArenaStartingEvent e = new ArenaStartingEvent(this);
		Bukkit.getPluginManager().callEvent(e);
	}
	
	public void startArenaClock() {
		if (status == ArenaStatus.InGame) return;
		status = ArenaStatus.InGame;
		this.timer = Config.timer;
		for (Player player:this.getPlayers())
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1f, 1f);
		new BukkitRunnable() {
			@Override
			public void run() {
				updateScoreboard();
				updateActionbar();
				if (status != ArenaStatus.InGame) {
					this.cancel();
				} else if (timer <= 0 || getWinningTeam() != Team.None) {
					// Countdown is over or a Team won.
					startEnding();
					this.cancel();
				} else {
					// Countdown is still going.
					timer += -1;
				}
			}
		}.runTaskTimer(Duckhunt.plugin, 0, 20);
		
		ArenaStartedEvent e = new ArenaStartedEvent(this);
		Bukkit.getPluginManager().callEvent(e);
	}
	
	public void startEnding() {
		if (status == ArenaStatus.Ending) return;
		status = ArenaStatus.Ending;
		if (Config.bungeeSkipEndingCelebration) {
			this.endGame();
			return;
		}
		for (Player player:this.getPlayers()) {
			new Message(Config.endMessages, this, player).send();
			player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_DEATH, 3f, 3f);
		}		
		this.timer = 15;
		new BukkitRunnable() {
			@Override
			public void run() {
				updateScoreboard();
				updateActionbar();
				if (status != ArenaStatus.Ending) {
					this.cancel();
				} else if (timer <= 0) {
					// Countdown is over, Game Ends.
					endGame();
					for (Player player:Arena.this.getPlayers()) {
						if (Arena.this.getTeam(player)==Team.None || Arena.this.getTeam(player)==Team.Spectators) continue;
						if (Arena.this.getTeam(player)==Arena.this.winningteam) {
							for (String command:Config.winCommands) 
								Bukkit.dispatchCommand(player, new Message(command, Arena.this, player).getText());
							for (String command:Config.winConsoleCommands) 
								Bukkit.dispatchCommand(player, new Message(command, Arena.this, player).getText());
							for (String message:Config.winMessages)
								new Message(message, Arena.this, player).send();
						} else {
							for (String command:Config.looseCommands) 
								Bukkit.dispatchCommand(player, new Message(command, Arena.this, player).getText());
							for (String command:Config.looseConsoleCommands) 
								Bukkit.dispatchCommand(player, new Message(command, Arena.this, player).getText());
							for (String message:Config.looseMessages)
								new Message(message, Arena.this, player).send();
						}
					}
					this.cancel();
				} else {
					// Countdown is still going.
					timer += -1;
					for (Player player:getPlayers())
						Fireworks.spawnFirework(player);
				}
			}
		}.runTaskTimer(Duckhunt.plugin, 0, 20);
		
		ArenaEndingEvent e = new ArenaEndingEvent(this);
		Bukkit.getPluginManager().callEvent(e);
	}
	
	public void endGame() {
		if (!Config.autoRejoin) this.allPlayersLeave();
		else this.allPlayersRejoin();
		this.winningteam = Team.None;
		this.startRecruiting();
		ArenaEndedEvent e = new ArenaEndedEvent(this);
		Bukkit.getPluginManager().callEvent(e);
		if (Config.bungeeEnabled && Config.bungeeRestartServerOnGameEnd) Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart"); 
	}
	
	public void killDuck(GamePlayer duck) {
		if (this.getTeam(duck.getPlayer())!=Team.Ducks) return;
		int lives = this.getLives(duck.getPlayer());
		int newLives = lives - 1;
		if (newLives > 0) {
			// Duck has enough lives to continue.
			duck.restoreInventory();
			this.teleportToSpawn(duck.getPlayer());
			this.openClassSelector(duck.getPlayer());
			this.broadcast(Config.duckDeath, Team.None, duck.getPlayer());
			this.setLives(duck.getPlayer(), newLives);
			if (newLives == 1) new Message(Config.duckLastLife, this, duck.getPlayer()).send();
			else new Message(Config.duckHasLives, this, duck.getPlayer()).send();
		} else {
			// Duck is eliminated.
			this.broadcast(Config.duckEliminated, Team.None, duck.getPlayer());
			if (this.getDuckCount()-1 <= 0) {
				// Last duck Eliminated. 
				// Hunters win.
				this.winningteam = Team.Hunters;
			} else {
				// There is more ducks.
				this.joinTeam(Team.Spectators, duck.getPlayer());
			}
		}
	}
	
	public void duckFinish(GamePlayer duck) {
		this.broadcast(Config.duckFinishedCourse, Team.None, duck.getPlayer());
		this.duckFinisher = duck.getPlayer().getName();
		this.winningteam = Team.Ducks;
		this.startEnding();
	}
	
	private void updateScoreboard() {
		for (Player player:getPlayers()) {
			player.setScoreboard(new Board(this, player).getBoard());
		}
	}
	
	private void updateActionbar() {
		switch (status) {
		case Recruiting:
			for (Player player:getPlayers())
				ActionbarReflection.sendActionbar(player, new Message(Config.playersNeeded, this, player).getText());
			break;
		case Starting:
			for (Player player:getPlayers())
				ActionbarReflection.sendActionbar(player, new Message(Config.gameStartingSoon, this, player).getText());
			break;
		default:
			break;
		}
	}
	
	// Have player be null if you want to talk about the players we're broadcasting to.
	// Have player be someone else if your talking about someone specific to everyone.
	public void broadcast(String message, Team team, Player player) {
		for (Player p : getPlayers()) {
			if (this.getTeam(p)==team || team == Team.None) {
				if (player==null) {
					new Message(message, this, p).send();
				} else {
					new Message(message, this, p, player).send();
				}
			}
		}
	}
	
	public Team getTeam(Player player) {
		if (this.ducks.contains(this.getGamePlayer(player))) return Team.Ducks;
		if (this.hunters.contains(this.getGamePlayer(player))) return Team.Hunters;
		if (this.spectators.contains(this.getGamePlayer(player))) return Team.Spectators;
		return Team.None;
	}
	
	public List<GamePlayer> getDucks() {
		return ducks;
	}

	public List<GamePlayer> getHunters() {
		return hunters;
	}


	public List<GamePlayer> getSpectators() {
		return spectators;
	}

	public String getLastDuckDeath() {
		return lastDuckDeath;
	}

	public String getLastDuckKiller() {
		return lastDuckKiller;
	}
	
	public String getDuckFinisher() {
		return duckFinisher;
	}
	
	public void setLastDuckDeath(String playerName) {
		this.lastDuckDeath = playerName;
	}
	
	public void setLastDuckKiller(String playerName) {
		this.lastDuckKiller = playerName;
	}
	
	public Team getWinningTeam() {
		return winningteam;
	}
	
	public void reloadConfig() {
		this.duckSpawn = ArenaStorage.getSpawnLocation(this, Team.Ducks);
		this.hunterSpawn = ArenaStorage.getSpawnLocation(this, Team.Hunters);
		this.spectatorSpawn = ArenaStorage.getSpawnLocation(this, Team.Spectators);
	}

	public Arena(String name, ArenaStatus status, int minPlayers, int maxPlayers, Location duckSpawn, Location hunterSpawn, Location spectatorSpawn, List<Location> checkpoints) {
		this.name = name;
		this.status = status;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.duckSpawn = duckSpawn;
		this.hunterSpawn = hunterSpawn;
		this.spectatorSpawn = spectatorSpawn;
		this.checkpoints = checkpoints;
	}
}
