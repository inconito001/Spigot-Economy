package fr.inconito001.com.utils;

import org.apache.commons.lang.StringUtils;

import net.md_5.bungee.api.ChatColor;

public class Utils {

	public static final String LINE = "§7" + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", (int) 53);

	public static String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}
