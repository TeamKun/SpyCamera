package team.kun.spycamera

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.annotation.dependency.Dependency
import org.bukkit.plugin.java.annotation.dependency.DependsOn
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.author.Author
import team.kun.spycamera.ext.registerListener
import team.kun.spycamera.item.RecipeService
import team.kun.spycamera.listener.ItemListener
import team.kun.spycamera.listener.PlayerListener

@Plugin(name = "SpyCamera", version = "1.0-SNAPSHOT")
@Author("ReyADayer")
@DependsOn(
        Dependency("ProtocolLib"),
)
@ApiVersion(ApiVersion.Target.v1_15)
class SpyCamera : JavaPlugin() {
    override fun onEnable() {
        registerListener(ItemListener(this))
        registerListener(PlayerListener(this))

        RecipeService.add(this)
    }

    override fun onDisable() {
        RecipeService.remove(this)
    }
}