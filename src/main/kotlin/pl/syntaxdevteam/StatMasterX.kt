package pl.syntaxdevteam

import org.bukkit.plugin.java.JavaPlugin
import pl.syntaxdevteam.helpers.*

class StatMasterX : JavaPlugin() {
    lateinit var logger: Logger
    private var config = getConfig()
    private var debugMode = config.getBoolean("debug")
    private val language = config.getString("language") ?: "EN"
    private lateinit var pluginManager: PluginManager
    private lateinit var statsCollector: StatsCollector
    lateinit var messageHandler: MessageHandler
    lateinit var timeHandler: TimeHandler
    private lateinit var updateChecker: UpdateChecker

    override fun onLoad() {
        logger = Logger(this, debugMode)
    }

    override fun onEnable() {
        saveDefaultConfig()

        messageHandler = MessageHandler(this)
        timeHandler = TimeHandler(this)
        val author = when (language.lowercase()) {
            "pl" -> "WieszczY"
            "en" -> "Syntaxerr"
            "fr" -> "OpenAI Chat GPT-3.5"
            "es" -> "OpenAI Chat GPT-3.5"
            "de" -> "OpenAI Chat GPT-3.5"
            else -> "SyntaxDevTeam"
        }
        logger.log("<gray>Loaded \"$language\" language file by: <white><b>$author</b></white>")
        pluginManager = PluginManager(this)
        val externalPlugins = pluginManager.fetchPluginsFromExternalSource("https://raw.githubusercontent.com/SyntaxDevTeam/plugins-list/main/plugins.json")
        val loadedPlugins = pluginManager.fetchLoadedPlugins()
        val highestPriorityPlugin = pluginManager.getHighestPriorityPlugin(externalPlugins, loadedPlugins)
        if (highestPriorityPlugin == description.name) {
            val syntaxDevTeamPlugins = loadedPlugins.filter { it.first != description.name }
            logger.pluginStart(syntaxDevTeamPlugins)
        }

        statsCollector = StatsCollector(this)
        updateChecker = UpdateChecker(this, config)
        updateChecker.checkForUpdates()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
