package com.skilfully.ethero.utils.messenger;

import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messenger {

    public Messenger(@NonNull String pluginName) {
        this.consolePrefix = "&7| &b" + pluginName + " &7|";
    }

    @Setter
    @Getter
    private String prefix;

    @Getter
    private final String consolePrefix;

    private final static String logInfo = "&7| &fINFO  &7| &r";
    private final static String logWarn = "&7| &eWARN  &7| &e";
    private final static String logError = "&7| &cERROR &7| &c";
    private final static String logDebug = "&7| &5DEBUG &7| &5";

    public void sendMessage(@NonNull CommandSender sender, String message, Object... args) {
        sendMessage(sender, formatMessageColor(formatMessageArgs(message, args)));
    }

    public void sendMessage(@NonNull CommandSender sender, String message) {
        if (message == null || message.isEmpty()) {
            sender.sendMessage("");
            return;
        }
        sender.sendMessage(formatMessageColor(message));
    }

    public void consoleInfo( String message, Object... args) {
        sendMessage(Bukkit.getConsoleSender(), consolePrefix + logInfo + message, args);
    }

    public void consoleWarn( String message, Object... args) {
        sendMessage(Bukkit.getConsoleSender(), consolePrefix + logWarn + message, args);
    }

    public void consoleError( String message, Object... args) {
        sendMessage(Bukkit.getConsoleSender(), consolePrefix + logError + message, args);
    }

    public void consoleDebug( String message, Object... args) {
        sendMessage(Bukkit.getConsoleSender(), consolePrefix + logDebug + message, args);
    }

    public void sendMessageToPlayer(@NonNull Player player, String message) {
        sendMessage(player, prefix + formatMessageColor(message));
    }

    public void sendMessageToPlayer(@NonNull Player player, String message, Object... args) {
        sendMessageToPlayer(player, prefix + formatMessageColor(formatMessageArgs(message, args)));
    }

    public void sendNoPrefixMessageToPlayer(@NonNull Player player, String message) {
        sendMessage(player, formatMessageColor(message));
    }

    public void sendNoPrefixMessageToPlayer(@NonNull Player player, String message, Object... args) {
        sendMessageToPlayer(player, formatMessageColor(formatMessageArgs(message, args)));
    }

    private String formatMessageColor(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private String formatMessageArgs(String message, Object... args) {
        if (args.length == 0) {
            return message;
        }

        StringBuilder result = new StringBuilder();
        int argIndex = 0;
        int lastIndex = 0;
        int placeholderIndex;

        while (argIndex < args.length && (placeholderIndex = message.indexOf("{}", lastIndex)) != -1) {
            result.append(message, lastIndex, placeholderIndex);
            result.append(args[argIndex] == null ? "null" : args[argIndex].toString());
            lastIndex = placeholderIndex + 2;
            argIndex++;
        }

        if (lastIndex < message.length()) {
            result.append(message.substring(lastIndex));
        }

        return result.toString();
    }

}
