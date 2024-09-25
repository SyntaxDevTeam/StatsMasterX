package pl.syntaxdevteam

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.plugin.java.JavaPlugin
import pl.syntaxdevteam.helpers.*

class StatMasterX : JavaPlugin(), Listener {
    lateinit var logger: Logger
    private var config = getConfig()
    private var debugMode = config.getBoolean("debug")
    private val language = config.getString("language") ?: "EN"
    private lateinit var pluginManager: PluginManager
    private lateinit var statsCollector: StatsCollector
    lateinit var messageHandler: MessageHandler
    lateinit var timeHandler: TimeHandler
    private lateinit var updateChecker: UpdateChecker
    private var econ: Economy? = null
    private var perms: Permission? = null

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
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            setupPAPI()
        } else {
            logger.warning("Cannot find PlaceholderAPI! Functionality may be limited.")
        }
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            setupVault()
        } else {
            logger.warning("Cannot find Vault! Functionality may be limited.")
        }

        statsCollector = StatsCollector(this)
        updateChecker = UpdateChecker(this, config)
        updateChecker.checkForUpdates()

        // Register the PluginEnableEvent listener
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler
    fun onPluginEnable(event: PluginEnableEvent) {
        if (event.plugin.description.name == "Vault") {
            setupVault()
        }
        if (event.plugin.description.name == "PlaceholderAPI") {
            setupPAPI()
        }
    }

    private fun setupPAPI() {
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    private fun setupVault() {
        if (!setupService(Economy::class.java)) {
            logger.warning("Cannot configure Economy service! Is the Economy plugin installed?.")
            return
        }
        if (!setupService(Permission::class.java)) {
            logger.warning("Cannot set up Permission service! Functionality may be limited.")
        }
    }

    private fun setupService(serviceClass: Class<*>): Boolean {
        return try {
            val rsp = server.servicesManager.getRegistration(serviceClass) ?: return false
            if (serviceClass == Economy::class.java) {
                econ = rsp.provider as Economy
            } else if (serviceClass == Permission::class.java) {
                perms = rsp.provider as Permission
            }
            rsp.provider != null
        } catch (e: Exception) {
            logger.severe("Error setting up service: ${e.message}")
            false
        }
    }

    fun getEconomy(): Economy? {
        return econ
    }

    fun getPermissions(): Permission? {
        return perms
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
