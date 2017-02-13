package com.wowserman.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.wowserman.Config;
import com.wowserman.Message;
import com.wowserman.arena.Arena;
import com.wowserman.arena.ArenaStatus;
import com.wowserman.arena.Team;

import net.md_5.bungee.api.ChatColor;

public class Board {

	private Arena arena;
	private Player player;
	
	private String scoreboardTitle() {
		if (this.arena.getStatus() == ArenaStatus.Starting || this.arena.getStatus() == ArenaStatus.InGame) return "§2§lDuckhunt";
		else if (this.arena.getStatus() == ArenaStatus.Ending) return "§" + Message.randomColor() + "§lDuckhunt";
		else return ChatColor.DARK_GREEN + "Duckhunt";
	}
	
	private List<String> getLines() {
		switch (arena.getStatus()) {
		case Recruiting:
			return Config.recruitingScoreboard;
		case Starting: 
			return Config.startingScoreboard;
		case InGame:
			if (this.arena.getTeam(player)==Team.Ducks) return Config.ingameDuckScoreboard;
			else return Config.ingameHunterScoreboard;
		case Ending:
			if (this.arena.getWinningTeam()==Team.Ducks) return Config.endgameDucksWinScoreboard;
			else return Config.endgameHuntersWinScoreboard;
		default:
			return new ArrayList<String>();
		}
	}
	
	public Scoreboard getBoard() {
		Scoreboard board = player.getScoreboard();
		if (player.getScoreboard()==null) board = Bukkit.getScoreboardManager().getNewScoreboard();
		for (Objective o:board.getObjectives()) o.unregister();
		Objective scores = board.registerNewObjective(scoreboardTitle(), "dummy");
		scores.setDisplaySlot(DisplaySlot.SIDEBAR);
		int i = 0;
		List<String> lines = new Message(this.getLines(), arena, player).getTextList();
		Collections.reverse(lines);
		while (i < lines.size()) {
			scores.getScore(lines.get(i)).setScore(i); i += 1;
		}
		return board;
	}
	
	public Board(Arena arena, Player player) {
		this.arena = arena;
		this.player = player;
	}
} 
