package team.kun.spycamera.listener

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.plugin.java.JavaPlugin
import team.kun.spycamera.ext.getMeta
import team.kun.spycamera.item.SpyCamera
import team.kun.spycamera.metadata.MetadataKey

class PlayerListener(private val plugin: JavaPlugin) : Listener {
    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity is Player && entity.getMeta(MetadataKey.UsingCamera, false)) {
            entity.getMeta(MetadataKey.Camera)?.let {
                SpyCamera().end(entity, it, plugin)
            }
        }
    }

    @EventHandler
    fun onDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val cameraPlayers = entity.getMeta(MetadataKey.CameraPlayers)
        cameraPlayers?.forEach {
            SpyCamera().end(it, entity, plugin)
        }
    }
}