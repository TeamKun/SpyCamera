package team.kun.spycamera.item

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

object RecipeService {
    val items: List<Craftable> = listOf(
            SpyCamera(),
            HDSpyCamera(),
            HDSpyCameraArrow()
    )

    fun add(plugin: JavaPlugin) {
        items.forEach {
            Bukkit.addRecipe(it.getRecipe(plugin))
        }
    }

    fun remove(plugin: JavaPlugin) {
        items.forEach { item ->
            item.getKey(plugin)?.let {
                Bukkit.removeRecipe(it)
            }
        }
    }
}