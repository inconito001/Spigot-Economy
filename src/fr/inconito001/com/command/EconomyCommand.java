package fr.inconito001.com.command;

import java.text.DecimalFormat;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.inconito001.com.Main;
import fr.inconito001.com.utils.BukkitUtils;
import fr.inconito001.com.utils.JavaUtils;
import fr.inconito001.com.utils.Utils;
import fr.inconito001.com.utils.command.Command;
import fr.inconito001.com.utils.command.CommandArgs;

public class EconomyCommand {
	
	private final Main plugin;
	
	public EconomyCommand(Main plugin) {
		this.plugin = plugin;
	}

	private static DecimalFormat formatter = new DecimalFormat("0.00");
	
	@Command(name = "pay", inGameOnly = true)
	public void payCmd(final CommandArgs args) {
		Player player = args.getPlayer();
		
		if (args.length() < 2) {
			player.sendMessage(Utils.color("&cUsage: /pay <player> <amount>"));
            return;
        }		

		Double amount = Double.parseDouble(args.getArgs(1));
		
		if (amount <= 0) {
			player.sendMessage(Utils.color("&cVous devez envoyer de l'argent en quantités positives."));
            return;
        }
		
		Double playerBalance = player != null ? plugin.getEconomyManager().getBalance(player.getUniqueId()) : 1024;
		
		if (playerBalance < amount) {
            player.sendMessage(Utils.color("&cVous n'avez pas assez d'argent !"));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args.getArgs(0));

        if (player.equals(target)) {
            player.sendMessage(Utils.color("&cVous ne pouvez pas vous envoyer de l'argent."));
            return;
        }

        Player targetPlayer = target.getPlayer();

        if (!target.hasPlayedBefore() && targetPlayer == null) {
            player.sendMessage(Utils.color("&cLe joueur " + args.getArgs(0) + " est introuvable."));
            return;
        }

		if (targetPlayer == null)
			return;

		if (player != null)
			plugin.getEconomyManager().subtractBalance(player.getUniqueId(), amount);
			plugin.getEconomyManager().addBalance(targetPlayer.getUniqueId(), amount);

		targetPlayer.sendMessage(Utils.color("&8» &6" + player.getName() + " Vous a envoyé &d" + formatter.format(amount).replace(',', '.') + "&e."));
        player.sendMessage(Utils.color("&8» &eVous avez envoyé " + formatter.format(amount).replace(',', '.') +  "$ à " + target.getName() + "&c."));
        return;
	}
	
	@Command(name= "economy", aliases = {"bal", "balance", "eco", "money"}, inGameOnly = true)	
	public void economyCommand(final CommandArgs args) {
		Player player = args.getPlayer();
		
		OfflinePlayer target;
		
		if (args.length() > 0 && player.hasPermission("economy.admin")) {
            target = BukkitUtils.offlinePlayerWithNameOrUUID(args.getArgs(0));
        } else if (player instanceof Player) {
            target = (Player) player;
        } else {
            player.sendMessage("Usage: /" + args.getLabel() + " <playerName>");
            return;
        }
		
		if (!target.hasPlayedBefore() && !target.isOnline()) {
            player.sendMessage(Utils.color("Joueur " + args.getArgs(0) +" introuvable"));
            return;
        }

        UUID uuid = target.getUniqueId();
        double balance = plugin.getEconomyManager().getBalance(uuid);
        
        if (args.length() < 2) {
            player.sendMessage(Utils.color((player.equals(target) ? "&8» &eVous avez actuellement &d" : "&8» &6" +  target.getName() + " &ea &d") + formatter.format(balance).replace(',', '.') + "$ &een banque&e."));
            return;
        }
        
        if (args.getArgs(1).equalsIgnoreCase("give") || args.getArgs(1).equalsIgnoreCase("add")) {
            if (args.length() < 3) {
                player.sendMessage(Utils.color("&cUsage: /" + args.getLabel() + ' ' + target.getName() + ' ' + args.getArgs(1) + " <amount>"));
                return;
            }

            Double amount = JavaUtils.tryParseDouble(args.getArgs(2));

            if (amount == null) {
            	player.sendMessage(Utils.color("&c" + args.getArgs(2) + " n'est pas un nombre valide."));
                return;
            }

            double newBalance = plugin.getEconomyManager().addBalance(uuid, amount);
            player.sendMessage(new String[]{ Utils.color("&8» &eVous avez ajouté " + formatter.format(amount).replace(',', '.') + "$ a &6" + target.getName() + "&e."),  Utils.color("&eLe solde de &6" + target.getName() + " &eest maintenant de &6" + formatter.format(newBalance).replace(',', '.') + "$&e.")});

            return;
        }
        
        if (args.getArgs(1).equalsIgnoreCase("take") || args.getArgs(1).equalsIgnoreCase("subtract")) {
            if (args.length() < 3) {
                player.sendMessage(Utils.color("&cUsage: /" + args.getLabel() + ' ' + target.getName() + ' ' + args.getArgs(1) + " <amount>"));
                return;
            }

            Double amount = JavaUtils.tryParseDouble(args.getArgs(2));

            if (amount == null) {
            	player.sendMessage(Utils.color("&c" + args.getArgs(2) + " n'est pas un nombre valide."));
                return;
            }

            double newBalance = plugin.getEconomyManager().subtractBalance(uuid, amount);

            player.sendMessage(new String[]{Utils.color("&8» &eVous venez de retirer &d" + formatter.format(amount).replace(',', '.') + "$ &e a &6" + target.getName() + "&e."), Utils.color("&eLe solde de &6" + target.getName() + " &eest maintenant de &d" + formatter.format(newBalance).replace(',', '.') + "$&e.")});
            return;
        }
        
        if (args.getArgs(1).equalsIgnoreCase("set")) {
            if (args.length() < 3) {
                player.sendMessage(Utils.color("&cUsage: /" + args.getLabel() + ' ' + target.getName() + ' ' + args.getArgs(1) + " <amount>"));
                return;
            }

            Double amount = JavaUtils.tryParseDouble(args.getArgs(2));

            if (amount == null) {
            	player.sendMessage(Utils.color("&c" + args.getArgs(2) + " n'est pas un nombre valide."));
                return;
            }

            double newBalance = plugin.getEconomyManager().setBalance(uuid, amount);
            player.sendMessage(Utils.color("&8» &eVous avez définie le solde de &6" + target.getName() + " &ea &d" + formatter.format(newBalance).replace(',', '.') + "$&e."));
            return;
        }

        player.sendMessage(Utils.color((player.equals(target) ? "&8» &eVotre solde" : "&8» &eLe solde de &6" + target.getName()) + " &eest de &d" + formatter.format(balance).replace(',', '.') + "$&e."));
        return;
        
	}
}
