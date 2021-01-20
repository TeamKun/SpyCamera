package team.kun.spycamera.listener

import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import team.kun.spycamera.ext.getMeta
import team.kun.spycamera.ext.getNbt
import team.kun.spycamera.item.HDSpyCamera
import team.kun.spycamera.item.HDSpyCameraArrow
import team.kun.spycamera.item.SpyCamera
import team.kun.spycamera.metadata.BasicNbtKey
import team.kun.spycamera.metadata.MetadataKey
import team.kun.spycamera.metadata.PlayerFlagMetadata

class ItemListener(private val plugin: JavaPlugin) : Listener {
    private val playerFlagMetadata = PlayerFlagMetadata(plugin)

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        val player = event.player
        if (playerFlagMetadata.getFlag(player)) {
            return
        }
        if (player.getMeta(MetadataKey.UsingCamera, false)) {
            playerFlagMetadata.avoidTwice(player)
            val camera = player.getMeta(MetadataKey.Camera)
            if (camera?.persistentDataContainer?.getNbt(plugin, BasicNbtKey.Name) == "HDSpyCameraArrow") {
                if (event.action == Action.LEFT_CLICK_BLOCK || event.action == Action.LEFT_CLICK_AIR) {
                    if (camera is LivingEntity) {
                        HDSpyCameraArrow().shot(camera)
                    }
                }else {
                    camera.let {
                        SpyCamera().end(player, it, plugin)
                    }
                }
            } else {
                camera?.let {
                    SpyCamera().end(player, it, plugin)
                }
            }
            event.isCancelled = true
            return
        }
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            if (SpyCamera().equal(event.player.inventory.itemInMainHand, plugin)) {
                playerFlagMetadata.avoidTwice(player)
                SpyCamera().execute(player, event.player.inventory.itemInMainHand, plugin)
                event.isCancelled = true
            } else if (HDSpyCamera().equal(event.player.inventory.itemInMainHand, plugin)) {
                playerFlagMetadata.avoidTwice(player)
                HDSpyCamera().execute(player, event.player.inventory.itemInMainHand, plugin)
                event.isCancelled = true
            }else if (HDSpyCameraArrow().equal(event.player.inventory.itemInMainHand, plugin)) {
                playerFlagMetadata.avoidTwice(player)
                HDSpyCameraArrow().execute(player, event.player.inventory.itemInMainHand, plugin)
                event.isCancelled = true
            }
        } else if (event.action == Action.LEFT_CLICK_AIR || event.action == Action.LEFT_CLICK_BLOCK) {
            if (SpyCamera().equal(event.player.inventory.itemInMainHand, plugin)) {
                playerFlagMetadata.avoidTwice(player)
                SpyCamera().cancel(player, event.player.inventory.itemInMainHand, plugin)
                event.isCancelled = true
            } else if (HDSpyCamera().equal(event.player.inventory.itemInMainHand, plugin)) {
                playerFlagMetadata.avoidTwice(player)
                HDSpyCamera().cancel(player, event.player.inventory.itemInMainHand, plugin)
                event.isCancelled = true
            }else if (HDSpyCameraArrow().equal(event.player.inventory.itemInMainHand, plugin)) {
                playerFlagMetadata.avoidTwice(player)
                HDSpyCameraArrow().cancel(player, event.player.inventory.itemInMainHand, plugin)
                event.isCancelled = true
            }
        }
    }
}