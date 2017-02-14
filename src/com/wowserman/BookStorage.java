package com.wowserman;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class BookStorage {


	public FileConfiguration configfile;
	private File fileobject;

	private void attemptToCreateFile(Plugin p) {

		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}

		fileobject = new File(p.getDataFolder(), "book.yml");

		if (!fileobject.exists()) {
			try {
				fileobject.createNewFile();
			}
			catch (IOException e) {
				Bukkit.getServer().getLogger().severe(ChatColor.RED  + "Could not create book.yml!");
			}
		}

		configfile = YamlConfiguration.loadConfiguration(fileobject);
	}

	public void saveFile() {
		try {
			configfile.save(fileobject);
		}
		catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED  + "Could not save book.yml!");
		}
	}

	public void reloadData() {
		configfile = YamlConfiguration.loadConfiguration(fileobject);
	}

	public BookStorage() {
		this.attemptToCreateFile(Duckhunt.plugin);
	}

	public FileConfiguration getConfig() {
		return configfile;
	}

	public void write(ItemStack book) {
		this.getConfig().addDefault("book", book);
		this.getConfig().set("book", book);
		this.saveFile();
	}

	public ItemStack getBook() {
		if (this.getConfig().get("book")!=null) {
			ItemStack book = new ItemStack(Material.BOOK, 1);
			book = (ItemStack) this.getConfig().get("book");
			book.setAmount(1);
			return book;
		} else {
			ItemStack unknown = new ItemStack(Material.BOOK, 1);
			ItemMeta m = unknown.getItemMeta();
			m.setDisplayName("?");
			List<String> lores = new ArrayList<String>();
			lores.add("The Book is Enabled, but");
			lores.add("no Book Exists.");
			lores.add("");
			lores.add("Add a Book with /duckhunt savebook");
			m.setLore(lores);
			unknown.setItemMeta(m);
			return unknown;
		}
	}
}
