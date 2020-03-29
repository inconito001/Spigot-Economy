package fr.inconito001.com;

import org.bukkit.plugin.java.JavaPlugin;

import fr.inconito001.com.manager.EconomyData;
import fr.inconito001.com.manager.EconomyManager;
import fr.inconito001.com.utils.command.CommandFramework;
import lombok.Getter;

public class Main extends JavaPlugin {
	
	private static Main plugin;
	
	@Getter private CommandFramework framework;
	@Getter private EconomyManager economyManager;
	
	@Override
	public final void onEnable() {
		plugin = this;
		
		this.registerManager();
		this.registerCommand();
		this.registerData();
		
		super.onEnable();
	}
	
	@Override
	public final void onDisable() {
		this.saveData();
		super.onDisable();
	}
	
	private final void saveData() {
		this.economyManager.saveEconomyData();
	}
	
	private final void registerData() {
		this.economyManager.reloadEconomyData();
	}
	
	private final void registerCommand() {
		this.framework = new CommandFramework(this);
	}
	
	private final void registerManager() {
		economyManager = new EconomyData(this);
	}
	
	public static final Main getPlugin() {
		return plugin;
	}
}
