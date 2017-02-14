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
	
	/**
	 * @return True if the Arena is Enabled, False if the Arena is Disabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * @param value The New Value.
	 */
	public void setEnabled(boolean value) {
		enabled = value;
	}
	
	/**
	 * @return The Name of the Arena, with the First Letter Upper-Cased.
	 */
	public String getName() {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	/**
	 * @param player The Player we're getting the Lives of.
	 * @return The Lives of the Player.
	 */
	public int getLives(Player player) {
		if (this.lives.get(player.getName())!=null) return this.lives.get(player.getName());
		else return 0;
	}
	
	/**
	 * @param player The Player we're Changing the Lives of.
	 * @param value The New Value of Lives of the Player.
	 */
	public void setLives(Player player, int value) {
		this.lives.put(player.getName(), value);
	}
	
	/**
	 * @return The Status of the Arena.
	 */
	public ArenaStatus getStatus() {
		return status;
	}
	
	/**
	 * @param status The New Status of the Arena
	 */
	public void setStatus(ArenaStatus status) {
		this.status = status;
	}
	
	/**
	 * @return The Current Timer / Count-down.
	 */
	public int getTimer() {
		return timer;
	}
	
	/**
	 * @return The Current Player Count of the Arena.
	 */
	public int getPlayerCount() {
		return this.gamePlayers.size();
	}
	
	/**
	 * @param player The Player we're getting the GamePlayer of.
	 * @return The GamePlayer of the Player. Returns null if the Player isn't in the Arena.
	 */
	public GamePlayer getGamePlayer(Player player) {
		for (GamePlayer gp:this.getGamePlayers()) {
			if (gp.getPlayer().getName() == player.getName()) return gp;
		}
		return null;
	}
	
	/**
	 * @param player The Player we're removing from the Arena.
	 */
	private void removePlayer(Player player) {
		gamePlayers.remove(this.getGamePlayer(player));
	}
	
	/**
	 * @param player The Player we're adding to the Arena.
	 */
	private void addPlayer(Player player) {
		GamePlayer gp = new GamePlayer(this, player);
		if (gamePlayers.contains(gp)) return;
		gamePlayers.add(gp);
	}
	
	/**
	 * @return The GamePlayers of the Arena.
	 */
	public List<GamePlayer> getGamePlayers() {
		return gamePlayers;
	}
	
	/**
	 * @return The Players of the Arena.
	 */
	public List<Player> getPlayers() {
		List<Player> l = new ArrayList<Player>();
		for (GamePlayer p:this.getGamePlayers()) l.add(p.getPlayer());
		return l; 
	}
	
	/**
	 * @param player The GamePlayer we're Checking.
	 * @return True if the Arena contains the Player, False if it doesn't.
	 */
	public boolean hasPlayer(GamePlayer player) {
		return this.getGamePlayers().contains(player);
	}
	
	/**
	 * @return The Minimum Players needed for the Arena to Start.
	 */
	public int getMinPlayers() {
		return minPlayers;
	}
	
	/**
	 * @return The Maximum Players allowed in the Arena at one time.
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	/**
	 * @param player The Player we're Getting the GameClassType of.
	 * @return The GameClassType. Returns None if the GamePlayer isn't in the Arena.
	 */
	public GameClassType getGameClassType(Player player) {
		
		for (GamePlayer gm:this.getGamePlayers())
			if (gm.getPlayer().getName()==player.getName()) return gm.getType();
		return GameClassType.None;
	}
	
	/**
	 * @return The Duck Spawn.
	 */
	public Location getDuckSpawn() {
		return duckSpawn;
	}
	
	/**
	 * @return The Hunter Spawn.
	 */
	public Location getHunterSpawn() {
		return hunterSpawn;
	}
	
	/**
	 * @return The List of Checkpoints.  Not Used or Fully Implemented Yet.
	 */
	@Deprecated
	public List<Location> getCheckpoints() {
		return checkpoints;
	}
	
	/**
	 * @return The Current Size of the Duck Team.
	 */
	public int getDuckCount() {
		return ducks.size();
	}
	
	/**
	 * @return The Current Size of the Hunter Team.
	 */
	public int getHunterCount() {
		return hunters.size();
	}
	
	/**
	 * @return The Current Size of the Spectator Team.
	 */
	public int getSpectatorCount() {
		return spectators.size();
	}
	
	/**
	 * @return True if the Arena has enough Players to Start, False if there isn't enough Players.
	 */
	public boolean canStart() {
		return (this.getPlayerCount() >= this.minPlayers);
	}
	
	/**
	 * @return A GamePlayer who's Team Preference is set to None. Returns Null if there is no Game Player with no Preference.
	 */
	private GamePlayer getGamePlayerWithoutTeamPreference() {
		for (GamePlayer gp:this.getGamePlayers()) {
			if (gp.getTeamPreference()==Team.None) return gp;
		} return null;
	}
	
	/**
	 * Splits the GamePlayers into Teams.
	 * Takes into account the GamePlayer's Team Preference.
	 */
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
	
	/**
	 * Teleports the Player to their Team's Spawn.
	 * 
	 * @param player The Player we're Teleporting to Spawn.
	 */
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
	
	/**
	 * Changes a GamePlayer's GameClassType.
	 * 
	 * @param player The GamePlayer we're Changing the GameClassType to.
	 * @param type The New GameClassType of the GamePlayer
	 */
	public void changeClass(GamePlayer player, GameClassType type) {
		GamePlayerSwitchClassEvent e = new GamePlayerSwitchClassEvent(player, player.getType(), type);
		Bukkit.getPluginManager().callEvent(e);
		if (e.isCancelled()) return;
		player.changeType(type);
	}
	
	/**
	 * Change the Team Preference of a Player.
	 * 
	 * @param player The Player we're setting the Prefered Team of.
	 * @param team The New Team the Player Prefers.
	 */
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
	
	/**
	 * @param player The Player we're getting the Team Preference of.
	 * @return The Team Preference of the Player.
	 */
	public Team getPreferedTeam(Player player) {
		if (this.preferedteam.get(player.getName())!=null) return this.preferedteam.get(player.getName());
		else return Team.None;
	}
	
	/**
	 * Used for having Player's Join Teams.
	 * 
	 * @param team The Team the Player will Join.
	 * @param player The Player we're having Join a Team.
	 */
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
	
	/**
	 * Used for having Players Leave their Teams.
	 * 
	 * @param player The Player that we're having Leave their Team.
	 */
	public void leaveTeam(Player player) {
		if (this.getGamePlayer(player)==null) return;
		this.ducks.remove(this.getGamePlayer(player));
		this.hunters.remove(this.getGamePlayer(player));
		this.spectators.remove(this.getGamePlayer(player));
		this.changeClass(this.getGamePlayer(player), GameClassType.None);
		player.setGameMode(GameMode.SURVIVAL);
	}
	
	/**
	 * Used for having Players Join the Arena.
	 * 
	 * @param player The Player we're having Join the Arena.
	 */
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
	
	/**
	 * Used for having the Player Spectate the Arena.
	 * 
	 * @param player The Player we're having Spectate the Arena.
	 */
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
	
	/**
	 * Used when the Arena Ends.
	 * 
	 * @param player The Player we're having rejoin the Arena.
	 */
	public void rejoin(Player player) {
		this.preferedteam.remove(player.getName());
		this.leaveTeam(player);
		this.preferedteam.put(player.getName(), Team.None);
		this.getGamePlayer(player).heal();
		this.getGamePlayer(player).changeType(GameClassType.None);
		this.teleportToSpawn(player);
		new Message(Config.playerAutoRejoin, this, player).send();
	}
	
	/**
	 * Used for having Players Leave.
	 * 
	 * @param player The Player we're having leave the Arena.
	 */
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
	
	/**
	 * Go through all the PLayers and have them Leave the Arena.
	 */
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
	
	/**
	 * Go through all the Players and have them Rejoin the Arena.
	 */
	public void allPlayersRejoin() {
		for (GamePlayer gp:this.getGamePlayers()) {
			this.rejoin(gp.getPlayer());
			new Message(Config.playerLeaveAfterRejoin, this, gp.getPlayer()).send();
		}
	}
	
	/**
	 * Used for Having Players open their Class Selector based on their Team.
	 * 
	 * @param player The Player we're having Open their Class Selector.
	 */
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
	
	/**
	 * Start the Game.
	 */
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
	
	/**
	 * Start Recruiting.
	 */
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

	/**
	 * Start the Countdown.
	 */
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
	
	/**
	 * Start the In-Game Arena Clock.
	 */
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
	
	/**
	 *  Start Ending.
	 */
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
	
	/**
	 * End the Arena.
	 */
	public void endGame() {
		if (!Config.autoRejoin) this.allPlayersLeave();
		else this.allPlayersRejoin();
		this.winningteam = Team.None;
		this.startRecruiting();
		ArenaEndedEvent e = new ArenaEndedEvent(this);
		Bukkit.getPluginManager().callEvent(e);
		if (Config.bungeeEnabled && Config.bungeeRestartServerOnGameEnd) Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart"); 
	}
	
	/**
	 * Handles the Deaths of a Duck.
	 * 
	 * @param duck The GamePlayer that has been 'killed'.
	 */
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
	
	/**
	 * @param duck The GamePlayer who finished the Arena's Course.
	 */
	public void duckFinish(GamePlayer duck) {
		this.broadcast(Config.duckFinishedCourse, Team.None, duck.getPlayer());
		this.duckFinisher = duck.getPlayer().getName();
		this.winningteam = Team.Ducks;
		this.startEnding();
	}
	
	/**
	 * Update the Scoreboard for all of the Players.
	 */
	private void updateScoreboard() {
		for (Player player:getPlayers()) {
			player.setScoreboard(new Board(this, player).getBoard());
		}
	}
	
	/**
	 * Update the Actionbar for all of the Players.
	 */
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
	
	/**
	 * 
	 * @param message The Message to send to the Players.
	 * @param team The Team of to Broadcast to. Set to Team.None if you want to talk to Every Player in the Arena regardless of Team.
	 * @param player The Player the Message is About. Use null if you want to talk About the players we're broadcasting to instead of just one Specific Player.
	 */
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
	
	/**
	 * @param player The Player we're getting the Team of.
	 * @return The Team of the Player.
	 */
	public Team getTeam(Player player) {
		if (this.ducks.contains(this.getGamePlayer(player))) return Team.Ducks;
		if (this.hunters.contains(this.getGamePlayer(player))) return Team.Hunters;
		if (this.spectators.contains(this.getGamePlayer(player))) return Team.Spectators;
		return Team.None;
	}
	
	/**
	 * @return A List of GamePlayers that are Ducks.
	 */
	public List<GamePlayer> getDucks() {
		return ducks;
	}

	/**
	 * @return A List of GamePlayers that are Hunters.
	 */
	public List<GamePlayer> getHunters() {
		return hunters;
	}


	/**
	 * @return A List of GamePlayers that are Spectators.
	 */
	public List<GamePlayer> getSpectators() {
		return spectators;
	}

	/**
	 * @return The Last Deceased Duck's Name.
	 */
	public String getLastDuckDeath() {
		return lastDuckDeath;
	}

	/**
	 * @return The Last Deceased Duck's Killer's Name.
	 */
	public String getLastDuckKiller() {
		return lastDuckKiller;
	}
	
	/**
	 * @return The Latest Duck Finisher's Name.
	 */
	public String getDuckFinisher() {
		return duckFinisher;
	}
	
	/**
	 * @param playerName The Player Name of the New Last Duck Death.
	 */
	public void setLastDuckDeath(String playerName) {
		this.lastDuckDeath = playerName;
	}
	
	/**
	 * @param playerName The Player Name of the New Last Duck Killer.
	 */
	public void setLastDuckKiller(String playerName) {
		this.lastDuckKiller = playerName;
	}
	
	/**
	 * @return The Winning Team.
	 */
	public Team getWinningTeam() {
		return winningteam;
	}
	
	/**
	 * Re-Gets the Spawns from the Config.
	 */
	public void reloadConfig() {
		this.duckSpawn = ArenaStorage.getSpawnLocation(this, Team.Ducks);
		this.hunterSpawn = ArenaStorage.getSpawnLocation(this, Team.Hunters);
		this.spectatorSpawn = ArenaStorage.getSpawnLocation(this, Team.Spectators);
	}

	/**
	 * Initialize a New Arena.
	 * 
	 * @param name The Name of the Arena.
	 * @param status The Status of the Arena.
	 * @param minPlayers The Minimum Players needed for the Arena to Start.
	 * @param maxPlayers The Maximum Players allowed at once in the Arena.
	 * @param duckSpawn The Duck Spawn Location.
	 * @param hunterSpawn The Hunter Spawn Location.
	 * @param spectatorSpawn The Spectator Spawn Location. 
	 * @param checkpoints The Checkpoints of the Arena.
	 */
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
