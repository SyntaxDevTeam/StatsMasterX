package pl.syntaxdevteam.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.syntaxdevteam.basic.loadPlayerStats

class StatsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        val player = sender
        val playerUUID = player.uniqueId
        val stats = loadPlayerStats(playerUUID)

        if (stats != null) {
            player.sendMessage("Twoje statystyki:")
            stats.stats["minecraft:mined"]?.forEach { (block, count) ->
                player.sendMessage("Wykopane $block: $count")
            }
            stats.stats["minecraft:killed"]?.forEach { (mob, count) ->
                player.sendMessage("Zabite $mob: $count")
            }
            stats.stats["minecraft:custom"]?.forEach { (stat, count) ->
                player.sendMessage("$stat: $count")
            }
        } else {
            player.sendMessage("Nie znaleziono statystyk.")
        }

        return true
    }
}
