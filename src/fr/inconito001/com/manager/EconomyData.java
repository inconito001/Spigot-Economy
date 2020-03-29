package fr.inconito001.com.manager;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.MemorySection;
import org.bukkit.plugin.java.JavaPlugin;

import fr.inconito001.com.utils.ConfigCreator;
import net.minecraft.util.gnu.trove.map.TObjectDoubleMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectDoubleHashMap;

public class EconomyData implements EconomyManager {

	private final JavaPlugin plugin;

    private TObjectDoubleMap<UUID> balanceMap = new TObjectDoubleHashMap<>();
    public static ConfigCreator balanceConfig;

    public EconomyData(JavaPlugin plugin) {
        this.plugin = plugin;
        reloadEconomyData();
    }
    
    @Override
    public ConfigCreator getBalanceConfig() {
    	return balanceConfig;
    }

	@Override
    public TObjectDoubleMap<UUID> getBalanceMap() {
        return balanceMap;
    }

    @Override
    public double getBalance(UUID uuid) {
        return balanceMap.get(uuid);
    }

    @Override
    public double setBalance(UUID uuid, double amount) {
        balanceMap.put(uuid, amount);
        return amount;
    }

    @Override
    public double addBalance(UUID uuid, double amount) {
        return setBalance(uuid, getBalance(uuid) + amount);
    }

    @Override
    public double subtractBalance(UUID uuid, double amount) {
        return setBalance(uuid, getBalance(uuid) - amount);
    }
    
    @Override
    public void reloadEconomyData() {
        balanceConfig = new ConfigCreator(plugin, "balances");
        Object object = balanceConfig.get("balances");
        if (object instanceof MemorySection) {
            MemorySection section = (MemorySection) object;
            Set<String> keys = section.getKeys(false);
            for (String id : keys) {
                balanceMap.put(UUID.fromString(id), balanceConfig.getDouble("balances." + id));
			}
		}
	}

	@Override
	public void saveEconomyData() {
		LinkedHashMap<String, Double> saveMap = new LinkedHashMap<String, Double>(this.balanceMap.size());
		this.balanceMap.forEachEntry((uuid, i) -> {
			saveMap.put(uuid.toString(), i);
			return true;
		});
		balanceConfig.set("balances", saveMap);
		balanceConfig.save();
	}

}
