package fr.inconito001.com.api;

import java.util.UUID;

import fr.inconito001.com.Main;

public class EconomyAPI {
	
	private final Main plugin;
	
	public EconomyAPI(Main plugin) {
		this.plugin = plugin;
	}
	
	public double getPlayerBalance(UUID uuid) {
        return plugin.getEconomyManager().getBalance(uuid);
    }
	
	public double setPlayerBalance(UUID uuid, double amount) {
        return plugin.getEconomyManager().setBalance(uuid, amount);
    }
	
	public double subtractPlayerBalance(UUID uuid, double amount) {
        return plugin.getEconomyManager().subtractBalance(uuid, amount);
    }
}
