package com.wowserman;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.wowserman.arena.Arena;

public class Config {

	private static FileConfiguration configfile = Duckhunt.plugin.getConfig();

	
	// Test 
	
	public static Boolean infoMessage;
	public static Boolean autoRejoin;
	public static Integer lives;
	public static Integer timer;
	public static String createdArena;
	public static String arenaAlreadyExists;
	public static String prefix;
	public static String arenaDoesntExist;
	public static String arenaIsntInGameCantSpectate;
	public static String arenaNotEnabled;
	public static String playerCantSpectatePlayerInGame;
	public static String playerCantJoinButCanSpectate;
	public static String playerInGameCantJoin;
	public static String playerCantJoinHasContentsInInventory;
	public static String playerCantLeaveNotInGame;
	public static String joinMessage;
	public static String quitMessage;
	public static String duckFinishedCourse;
	public static String duckDeath;
	public static String duckEliminated;
	public static String duckLastLife;
	public static String duckHasLives;
	public static String duckSwitchToHunter;
	public static String gameStartingSoon;
	public static String arenaHasNoEligibleHunter;
	public static String playerAutoRejoin;
	public static String playerLeaveAfterRejoin;
	public static String playerEnterSpectatorMode;
	public static String playersNeeded;
//	public static String hasNeededPlayers;
//	public static String notAllowedToMakeDuckhuntSign;
//	public static String duckDidntChooseKit;
//	public static String hunterDidntChooseKit;
//	public static String playerLacksPermissionForKit;
//	public static String countdownCanceled;
//	public static String countdownCanceledSub;
	public static String playerUseCommand;
//	public static String playerCantStartArena;
	public static String noGoodArenaAvalible;
	public static String noGoodServerAvalible;
	public static List<String> nonopHelp;
	public static List<String> opHelp;
	public static String joinHelp;
	public static String spectateHelp;
	public static String preferDucks;
	public static String preferNone;
	public static String preferHunters;
	public static Boolean ignoreInventories;
//	public static List<String> endMessages;
//	public static List<String> winCommands;
//	public static List<String> winConsoleCommands;
	public static List<String> winMessages;
//	public static List<String> looseCommands;
//	public static List<String> looseConsoleCommands;
//	public static List<String> looseMessages;
	public static List<String> duckStartMessages;
	public static List<String> hunterStartMessages;
	public static List<String> equipBarrager;
	public static List<String> equipPyrotech;
	public static List<String> equipFreezer;
	public static List<String> equipCyborg;
	public static List<String> equipRunner;
	public static List<String> equipHealer;
	public static List<String> equipSmoker;
	public static List<String> equipCloaker;
	public static List<String> whitelistedCommands;
	public static List<String> ingameDuckScoreboard;
	public static List<String> ingameHunterScoreboard;
	public static List<String> endgameDucksWinScoreboard;
	public static List<String> endgameHuntersWinScoreboard;
	public static List<String> startingScoreboard;
	public static List<String> recruitingScoreboard;
	public static Location fallbackLocation;
	public static Integer leaveItemSlot;
	public static Integer duckSelectorSlot;
	public static Integer noPreferenceSlot;
	public static Integer hunterSelectorSlot;
	public static Integer bookSlot;
	public static Boolean bookEnabled;
	public static Boolean leaveItemEnabled;
	public static String mainArenaName;
	public static boolean bungeeEnabled;
	public static boolean bungeeAutoJoinGameOnConnect;
	public static String bungeeLobbyServerName;	
//	public static boolean bungeeSkipEndingCelebration;
//	public static boolean bungeeRestartServerOnGameEnd;

	public static ItemStack getLeaveItem(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getConfigurationSection("leave-item"), arena, player);
	}

	public static ItemStack getDuckSelector(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getConfigurationSection("duck-selector"), arena, player);
	}

	public static ItemStack getNoPreferenceSelector(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getConfigurationSection("nopreference-selector"), arena, player);
	}

	public static ItemStack getHunterSelector(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getConfigurationSection("hunter-selector"), arena, player);
	}

	public static ItemStack getSpeedFeather(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("game-items").getConfigurationSection("runner-items").getConfigurationSection("speed-feather"), arena, player);
	}

	public static ItemStack getHealWand(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("game-items").getConfigurationSection("healer-items").getConfigurationSection("healing-wand"), arena, player);
	}

	public static ItemStack getSmokeGrenade(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("game-items").getConfigurationSection("smoker-items").getConfigurationSection("smoke-grenade"), arena, player);
	}

	public static ItemStack getInvisibilityCloak(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("game-items").getConfigurationSection("cloaker-items").getConfigurationSection("invisibility-cloak"), arena, player);
	}

	public static ItemStack getPlasma(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("game-items").getConfigurationSection("cyborg-items").getConfigurationSection("plasma"), arena, player);
	}

	public static ItemStack getLaserGun(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("game-items").getConfigurationSection("cyborg-items").getConfigurationSection("laser-gun"), arena, player);

	}

	public static ItemStack getFireball(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("game-items").getConfigurationSection("pyrotech-items").getConfigurationSection("fireball"), arena, player);
	}

	public static ItemStack getRustyBow(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("game-items").getConfigurationSection("barrager-items").getConfigurationSection("rusty-bow"), arena, player);
	}

	public static ItemStack getRustyArrow(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("game-items").getConfigurationSection("barrager-items").getConfigurationSection("rusty-arrow"), arena, player);
	}

	public static ItemStack getSnowball(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("game-items").getConfigurationSection("freezer-items").getConfigurationSection("snowball"), arena, player);
	}

	public static ItemStack getIcePillar(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("game-items").getConfigurationSection("freezer-items").getConfigurationSection("ice-pillar"), arena, player);
	}

	public static ItemStack getCyborgIcon(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("hunter-class-selector").getConfigurationSection("cyborg"), arena, player);
	}

	public static ItemStack getBarragerIcon(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("hunter-class-selector").getConfigurationSection("barrager"), arena, player);
	}

	public static ItemStack getPyrotechIcon(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("hunter-class-selector").getConfigurationSection("pyrotech"), arena, player);
	}

	public static ItemStack getFreezerIcon(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("hunter-class-selector").getConfigurationSection("freezer"), arena, player);
	}

	public static Inventory getHunterClassSelector(Arena arena, Player player) {
		Inventory i = Bukkit.createInventory(null, InventoryType.HOPPER, new Message(Config.configfile.getConfigurationSection("settings").getConfigurationSection("hunter-class-selector").getString("menu-name"), arena, player).getText());
		i.setItem(0, Config.getCyborgIcon(arena, player));
		i.setItem(1, Config.getBarragerIcon(arena, player));
		i.setItem(2, Config.getPyrotechIcon(arena, player));
		i.setItem(3, Config.getFreezerIcon(arena, player));
		return i;
	}

	public static ItemStack getRunnerIcon(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("duck-class-selector").getConfigurationSection("runner"), arena, player);
	}

	public static ItemStack getHealerIcon(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("duck-class-selector").getConfigurationSection("healer"), arena, player);
	}

	public static ItemStack getSmokerIcon(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("duck-class-selector").getConfigurationSection("smoker"), arena, player);
	}

	public static ItemStack getCloakerIcon(Arena arena, Player player) {
		return Tools.itemFromSection(Config.configfile.getConfigurationSection("settings").getConfigurationSection("duck-class-selector").getConfigurationSection("cloaker"), arena, player);
	}

	public static Inventory getDuckClassSelector(Arena arena, Player player) {
		Inventory i = Bukkit.createInventory(null, InventoryType.HOPPER, new Message(Config.configfile.getConfigurationSection("settings").getConfigurationSection("duck-class-selector").getString("menu-name"), arena, player).getText());
		i.setItem(0, Config.getRunnerIcon(arena, player));
		i.setItem(1, Config.getHealerIcon(arena, player));
		i.setItem(2, Config.getSmokerIcon(arena, player));
		i.setItem(3, Config.getCloakerIcon(arena, player));
		return i;
	}

	public static void setLobby(Location location) {
		configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getConfigurationSection("fallback-location").set("x", location.getBlockX()+0.5);
		configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getConfigurationSection("fallback-location").set("y", location.getBlockY()+0.5);
		configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getConfigurationSection("fallback-location").set("z", location.getBlockZ()+0.5);
		configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getConfigurationSection("fallback-location").set("world", location.getWorld().getName());
		Duckhunt.plugin.saveDefaultConfig();
		Config.fallbackLocation = location;
	}

	public void saveFile() {
		Duckhunt.plugin.saveDefaultConfig();
	}

	public void reloadData() {
		configfile = Duckhunt.plugin.getConfig();
	}

	public Config() {
		configfile.options().copyDefaults(true);
		Duckhunt.plugin.saveDefaultConfig();

		Config.createdArena = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("arena-created");
		Config.arenaAlreadyExists = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("arena-already-exists");
		Config.infoMessage = configfile.getConfigurationSection("settings").getBoolean("plugin-info-message");
		Config.autoRejoin = configfile.getConfigurationSection("settings").getBoolean("rejoin-after-game");
		Config.lives = configfile.getConfigurationSection("settings").getInt("lives");
		Config.prefix = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("prefix") + " ";
		Config.arenaDoesntExist = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("arena-doesnt-exist-message");
		Config.arenaIsntInGameCantSpectate = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("arena-isnt-in-game-message");
		Config.arenaNotEnabled = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("arena-not-enabled-message");
		Config.playerCantJoinButCanSpectate = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-can-spectate-message");
		Config.playerCantSpectatePlayerInGame = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-cant-spectate-message");
		Config.joinMessage = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("join-message");
		Config.quitMessage = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("quit-message");
		Config.duckDeath = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("duck-death-message");
		Config.duckEliminated = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("duck-eliminated-message");
		Config.duckLastLife = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("duck-last-life-message");
		Config.duckHasLives = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("duck-has-lives-message");
		Config.duckSwitchToHunter = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("duck-switch-to-hunter-message");
		Config.hunterDeath = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("hunter-death-message");
		Config.playerCantSpectate = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-cant-spectate-message");
		Config.playerCanSpectate = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-can-spectate-message");
		Config.playerInGameCantJoin = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-is-in-game-cant-join-message");
		Config.playerCantJoinHasContentsInInventory = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-cant-join-has-contents-in-inventory-message");
		Config.playerCantLeaveNotInGame = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-cant-leave-not-in-game-message");
		Config.duckSwitchToHunter = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("duck-switch-to-hunter-message");
		Config.duckFinishedCourse = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("duck-finishes-course-message");
		Config.arenaHasNoEligibleHunter = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("arena-has-no-eligible-hunters-message");
		Config.gameStartingSoon = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("game-starting-soon-message");
		Config.playerAutoRejoin = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-auto-rejoin-message");
		Config.playerLeaveAfterRejoin = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-leave-after-rejoin-message");
		Config.playerEnterSpectatorMode = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-enter-spectator-mode-message");
		Config.playersNeeded = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("players-needed-actionbar");
		Config.hasNeededPlayers = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("has-needed-players");
		Config.notAllowedToMakeDuckhuntSign = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("not-allowed-to-make-duckhunt-sign-message");
		Config.duckDidntChooseKit = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("duck-didnt-choose-class-message");
		Config.hunterDidntChooseKit = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("hunter-didnt-choose-class-message");
		Config.playerLacksPermissionForKit = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-lacks-permission-for-kit-message");
		Config.countdownCanceled = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("countdown-canceled-title");
		Config.countdownCanceledSub = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("countdown-canceled-subtitle");
		Config.playerUseCommand = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-ingame-uses-non-duckhunt-command-message");
		Config.winMessages = configfile.getConfigurationSection("settings").getConfigurationSection("rewards").getConfigurationSection("win-rewards").getStringList("messages");
		Config.winCommands = configfile.getConfigurationSection("settings").getConfigurationSection("rewards").getConfigurationSection("win-rewards").getStringList("commands");
		Config.winConsoleCommands = configfile.getConfigurationSection("settings").getConfigurationSection("rewards").getConfigurationSection("win-rewards").getStringList("console-commands");	
		Config.looseMessages = configfile.getConfigurationSection("settings").getConfigurationSection("rewards").getConfigurationSection("loose-rewards").getStringList("messages");
		Config.looseCommands = configfile.getConfigurationSection("settings").getConfigurationSection("rewards").getConfigurationSection("loose-rewards").getStringList("commands");
		Config.looseConsoleCommands = configfile.getConfigurationSection("settings").getConfigurationSection("rewards").getConfigurationSection("loose-rewards").getStringList("console-commands");
		Config.endMessages = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("end-messages");
		Config.duckStartMessages = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("duck-start-messages");
		Config.hunterStartMessages = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("hunter-start-messages");
		Config.equipBarrager = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("equip-barrager-messages");
		Config.equipPyrotech = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("equip-pyrotech-messages");
		Config.equipFreezer = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("equip-freezer-messages");
		Config.equipCyborg = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("equip-cyborg-messages");
		Config.equipRunner = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("equip-runner-messages");
		Config.equipHealer = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("equip-healer-messages");
		Config.equipSmoker = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("equip-smoker-messages");
		Config.equipCloaker = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("equip-cloaker-messages");
		Config.duckStartMessages = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("duck-start-messages");
		Config.hunterStartMessages = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("hunter-start-messages");
		Config.endMessages = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getStringList("end-messages");
		Config.timer = configfile.getConfigurationSection("settings").getInt("timer");
		Config.whitelistedCommands = configfile.getConfigurationSection("settings").getStringList("whitelisted-commands");
		Config.playerCantStartArena = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-cant-start-arena");
		Config.nonopHelp = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getConfigurationSection("help").getStringList("default-messages");
		Config.opHelp = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getConfigurationSection("help").getStringList("op-messages");
		Config.joinHelp = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getConfigurationSection("help").getString("join");
		Config.spectateHelp = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getConfigurationSection("help").getString("spectate");
		Config.ignoreInventories = configfile.getConfigurationSection("settings").getBoolean("ignore-inventories");
		Config.ingameDuckScoreboard = configfile.getConfigurationSection("settings").getConfigurationSection("scoreboards").getStringList("ingame-ducks");
		Config.ingameHunterScoreboard = configfile.getConfigurationSection("settings").getConfigurationSection("scoreboards").getStringList("ingame-hunters");
		Config.endgameDucksWinScoreboard = configfile.getConfigurationSection("settings").getConfigurationSection("scoreboards").getStringList("ending-ducks-win");
		Config.endgameHuntersWinScoreboard = configfile.getConfigurationSection("settings").getConfigurationSection("scoreboards").getStringList("ending-hunters-win");
		Config.startingScoreboard = configfile.getConfigurationSection("settings").getConfigurationSection("scoreboards").getStringList("starting");
		Config.recruitingScoreboard = configfile.getConfigurationSection("settings").getConfigurationSection("scoreboards").getStringList("recruiting");
		Config.fallbackLocation = Tools.locationFromSection(configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getConfigurationSection("fallback-location"));
		Config.leaveItemSlot = configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getConfigurationSection("leave-item").getInt("item-slot");
		Config.duckSelectorSlot = configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getConfigurationSection("duck-selector").getInt("item-slot");
		Config.noPreferenceSlot = configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getConfigurationSection("nopreference-selector").getInt("item-slot");
		Config.hunterSelectorSlot = configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getConfigurationSection("hunter-selector").getInt("item-slot");
		Config.bookSlot = configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getInt("book-slot");
		Config.bookEnabled = configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getBoolean("book-enabled");
		Config.leaveItemEnabled = configfile.getConfigurationSection("settings").getConfigurationSection("lobby").getBoolean("leave-item-enabled");
		Config.preferDucks = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-prefer-ducks");
		Config.preferNone = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-prefer-nopreference");
		Config.preferHunters = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("player-prefer-hunters");
		Config.mainArenaName = configfile.getConfigurationSection("settings").getConfigurationSection("bungeemode").getString("main-arena");
		Config.bungeeEnabled = configfile.getConfigurationSection("settings").getConfigurationSection("bungeemode").getBoolean("enabled");
		Config.bungeeAutoJoinGameOnConnect = configfile.getConfigurationSection("settings").getConfigurationSection("bungeemode").getBoolean("enabled");
		Config.bungeeLobbyServerName = configfile.getConfigurationSection("settings").getConfigurationSection("bungeemode").getString("lobby-server");
		Config.bungeeSkipEndingCelebration = configfile.getConfigurationSection("settings").getConfigurationSection("bungeemode").getBoolean("skip-end-celebration");
		Config.bungeeRestartServerOnGameEnd = configfile.getConfigurationSection("settings").getConfigurationSection("bungeemode").getBoolean("restart-after-game-ends");
		Config.noGoodArenaAvalible = configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("no-good-arena-message");
		Config.noGoodServerAvalible =  configfile.getConfigurationSection("settings").getConfigurationSection("messages").getString("no-good-server-message");

	}
}
