package fr.inconito001.com.manager;

import java.util.UUID;

import fr.inconito001.com.utils.ConfigCreator;
import net.minecraft.util.gnu.trove.map.TObjectDoubleMap;

public interface EconomyManager {

	TObjectDoubleMap<UUID> getBalanceMap();

	ConfigCreator getBalanceConfig();
	
	double getBalance(UUID uuid);

	double setBalance(UUID uuid, double amount);

	double addBalance(UUID uuid, double amount);

	double subtractBalance(UUID uuid, double amount);

	void reloadEconomyData();

	void saveEconomyData();
}
