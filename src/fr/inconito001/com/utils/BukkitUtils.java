package fr.inconito001.com.utils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.util.com.google.common.base.Preconditions;

public class BukkitUtils {

	public static List<String> getCompletions(final String[] args, final List<String> input) {
		return getCompletions(args, input, 80);
	}

	public static List<String> getCompletions(final String[] args, final List<String> input, final int limit) {
		Preconditions.checkNotNull((Object) args);
		Preconditions.checkArgument(args.length != 0);
		String argument = args[args.length - 1];
		return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length()))
				.limit(limit).collect(Collectors.toList());
	}

	public static String getDisplayName(final CommandSender sender) {
		Preconditions.checkNotNull((Object) sender);
		return (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();
	}

	public static long getIdleTime(final Player player) {
		Preconditions.checkNotNull((Object) player);
		final long idleTime = ((CraftPlayer) player).getHandle().x();
		return (idleTime > 0L) ? (MinecraftServer.ar() - idleTime) : 0L;
	}

	public static Player playerWithNameOrUUID(final String string) {
		if (string == null) {
			return null;
		}
		return JavaUtils.isUUID(string) ? Bukkit.getPlayer(UUID.fromString(string)) : Bukkit.getPlayer(string);
	}

	
	public static OfflinePlayer offlinePlayerWithNameOrUUID(final String string) {
		if (string == null) {
			return null;
		}
		return JavaUtils.isUUID(string) ? Bukkit.getOfflinePlayer(UUID.fromString(string)) : Bukkit.getOfflinePlayer(string);
	}

	public static boolean isWithinX(final Location location, final Location other, final double distance) {
		return location.getWorld().equals(other.getWorld()) && Math.abs(other.getX() - location.getX()) <= distance
				&& Math.abs(other.getZ() - location.getZ()) <= distance;
	}

	public static Location getHighestLocation(final Location origin) {
		return getHighestLocation(origin, null);
	}

	public static Location getHighestLocation(final Location origin, final Location def) {
		Preconditions.checkNotNull((Object) origin, "The location cannot be null");
		final Location cloned = origin.clone();
		final World world = cloned.getWorld();
		final int x = cloned.getBlockX();
		int y = world.getMaxHeight();
		final int z = cloned.getBlockZ();
		while (y > origin.getBlockY()) {
			final Block block = world.getBlockAt(x, --y, z);
			if (!block.isEmpty()) {
				final Location next = block.getLocation();
				next.setPitch(origin.getPitch());
				next.setYaw(origin.getYaw());
				return next;
			}
		}
		return def;
	}
}
