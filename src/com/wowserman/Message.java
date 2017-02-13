package com.wowserman;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;

import com.wowserman.arena.Arena;

public class Message {

	private String basictext = null;
	private List<String> basictextlist = null;
	private Arena arena;
	private Player playerReference;
	private Player target;
	
	public String getText() {
		return this.decode(this.basictext);
	}
	
	public List<String> getTextList() {
		return this.decodeList(this.basictextlist);
	}
	
	private String extras = "";
	
	private String getExtras() {
		extras = extras + " ";
		return extras;
	}
	
	private String decode(String message) {
		String m = message;
		if (message.contains("@Arena") && this.arena != null) m = m.replace("@Arena", this.arena.getName());
		if (message.contains("@GameStatus") && this.arena != null) m = m.replace("@GameStatus", this.arena.getStatus().toString());
		if (message.contains("@PlayerCount") && this.arena != null) m = m.replace("@PlayerCount", this.arena.getPlayerCount() + "");
		if (message.contains("@MinPlayers") && this.arena != null) m = m.replace("@MinPlayers", this.arena.getMinPlayers() + "");
		if (message.contains("@MaxPlayers") && this.arena != null) m = m.replace("@MaxPlayers", this.arena.getMaxPlayers() + "");
		if (message.contains("@NeededPlayers") && this.arena != null) m = m.replace("@NeededPlayers", this.arena.getMinPlayers() - this.arena.getPlayerCount() + "");
		if (message.contains("@Player") && this.playerReference != null) m = m.replace("@Player", this.playerReference.getName());
		if (message.contains("@Lives") && this.arena != null) m = m.replace("@Lives", this.arena.getLives(this.playerReference) + "");
		if (message.contains("@DuckCount") && this.arena != null) m = m.replace("@DuckCount", this.arena.getDuckCount() + "");
		if (message.contains("@HunterCount") && this.arena != null) m = m.replace("@HunterCount", this.arena.getHunterCount() + "");
		if (message.contains("@LastDuckDeath") && this.arena != null) m = m.replace("@LastDuckDeath", this.arena.getLastDuckDeath());
		if (message.contains("@LastDuckKiller") && this.arena != null) m = m.replace("@LastDuckKiller", this.arena.getLastDuckKiller());
		if (message.contains("@DuckFinisher") && this.arena != null) m = m.replace("@DuckFinisher", this.arena.getDuckFinisher());
		if (message.contains("@WinningTeam") && this.arena != null) m = m.replace("@WinningTeam", this.arena.getWinningTeam().toString());
		if (message.contains("@StartingCountdownTimer") && this.arena != null) m = m.replace("@StartingCountdownTimer", this.arena.getTimer() + "");
		if (message.contains("@TimeLeft") && this.arena != null) m = m.replace("@TimeLeft", Tools.getMinutesAndSeconds(this.arena.getTimer()));
		if (message.contains("@PreferedTeam") && this.playerReference != null) m = m.replace("@PreferedTeam", this.arena.getPreferedTeam(playerReference).toString());
		if (message.contains("@RandomColor")) m = m.replace("@RandomColor", "§" + Message.randomColor());
		if (message.contains("@EmptyLine")) m = this.getExtras();
		if (message.contains("&")) m = m.replace("&", "§");
		return m;
	}
	
	public static String randomColor() {
		String[] colors = new String[]{"a","b","c","d","e","f","0","1","2","3","4","5","6","7","8","9"};
		return colors[new Random().nextInt(colors.length)];
	}
	
	private List<String> decodeList(List<String> messages) {
		List<String> ms = new ArrayList<String>();
		for (String m:messages) ms.add(this.decode(m));
		return ms;
	}
	
	public Message(String text, Arena arena, Player player) {
		this.basictext = text;
		this.arena = arena;
		this.playerReference = player;
		this.target = player;
	}
	
	public Message(List<String> textlist, Arena arena, Player player) {
		this.basictextlist = textlist;
		this.arena = arena;
		this.playerReference = player;
		this.target = player;
	}
	
	public Message(String text, Arena arena, Player target, Player playerReference) {
		this.basictext = text;
		this.arena = arena;
		this.target = target;
		this.playerReference = playerReference;
	}
	
	public Message(List<String> textlist, Arena arena, Player target, Player playerReference) {
		this.basictextlist = textlist;
		this.arena = arena;
		this.target = target;
		this.playerReference = playerReference;
	}
	
	public void send() {
		if (basictext != null) {
			target.sendMessage(this.decode(Config.prefix) + this.getText());
			return;
		}
		
		if (basictextlist != null) {
			for (String line:this.getTextList()) {
				target.sendMessage(this.decode(Config.prefix) + line);
			}
			return;
		}
	}
}

