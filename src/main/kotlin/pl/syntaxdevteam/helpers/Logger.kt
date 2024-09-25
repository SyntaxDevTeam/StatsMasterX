package pl.syntaxdevteam.helpers

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import pl.syntaxdevteam.StatMasterX

class Logger(plugin: StatMasterX, private val debugMode: Boolean) {
    private val plName = plugin.description.name
    private val plVer = plugin.description.version

    fun clear(s: String) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', s))
    }

    fun success(s: String) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[$plName] &a&l$s&r"))
    }

    fun info(s: String) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[$plName] $s"))
    }

    fun warning(s: String) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[$plName] &6$s&r"))
    }

    fun err(s: String) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[$plName] &c$s&r"))
    }

    fun severe(s: String) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[$plName] &c&l$s&r"))
    }

    fun log(s: String) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[$plName] $s"))
    }

    fun debug(s: String) {
        if (debugMode) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[$plName] [DEBUG] &e&l$s"))
        }
    }

    fun pluginStart(pluginsByPriority: List<Pair<String, String>>) {
        clear("")
        clear("&9    __                   __        ___            ")
        clear("&9   (_      _  |_  _     |  \\  _     |   _  _   _  ")
        clear("&9   __) \\/ | ) |_ (_| )( |__/ (- \\/  |  (- (_| ||| ")
        clear("&9       /                                          ")
        clear("&9           ... is proud to present and enable:")
        clear("&9                     &f * &f&l${plName} v${plVer}")
        for ((plName, pluginVersion) in pluginsByPriority) {
            clear("&9                     &f * $plName v$pluginVersion")
        }
        clear("&9                 utilizing all the optimizations of your server engine!         ")
        clear("")
        clear("&a    Join our Discord! &9&lhttps://discord.gg/Zk6mxv7eMh")
        clear("")
        clear("")
    }
}