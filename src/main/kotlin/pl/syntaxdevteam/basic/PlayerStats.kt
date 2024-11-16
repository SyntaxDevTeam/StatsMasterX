package pl.syntaxdevteam.basic

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.util.UUID

data class PlayerStats(
    val stats: Map<String, Map<String, Int>>
) {

    fun loadPlayerStats(playerUUID: UUID): PlayerStats? {
        val statsFile = File("world/stats/$playerUUID.json")
        if (!statsFile.exists()) return null

        val json = statsFile.readText()
        val parser = JSONParser()
        val jsonObject = parser.parse(json) as JSONObject

        val stats = jsonObject["stats"]
        if (stats is Map<*, *>) {
            @Suppress("UNCHECKED_CAST")
            return PlayerStats(stats as Map<String, Map<String, Int>>)
        }
        return null
    }
}
