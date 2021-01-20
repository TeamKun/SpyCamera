package team.kun.spycamera.item

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Creeper
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import team.kun.spycamera.ext.getMeta
import team.kun.spycamera.ext.getNbt
import team.kun.spycamera.ext.playSound
import team.kun.spycamera.ext.removeMeta
import team.kun.spycamera.ext.removeNbt
import team.kun.spycamera.ext.setMeta
import team.kun.spycamera.ext.setNbt
import team.kun.spycamera.ext.spawn
import team.kun.spycamera.metadata.BasicNbtKey
import team.kun.spycamera.metadata.MetadataKey
import team.kun.spycamera.packet.CameraPacket
import java.util.*

open class SpyCamera : Item(), Craftable {
    override val name = "スパイカメラ"
    override val description = listOf(
            "右クリックでスパイカメラを設置・使用",
            "左クリックでスパイカメラを解除"
    )
    override val itemStack = ItemStack(Material.STONE_PICKAXE)

    override fun getRecipe(plugin: JavaPlugin): ShapedRecipe? {
        val key = getKey(plugin) ?: return null
        return ShapedRecipe(key, toItemStack(plugin)).apply {
            shape("GGG", "GIG", "GGG")
            setIngredient('G', Material.GLASS)
            setIngredient('I', Material.IRON_INGOT)
        }
    }

    fun execute(player: Player, itemStack: ItemStack, plugin: JavaPlugin) {
        val uuid = itemStack.itemMeta?.persistentDataContainer?.getNbt(plugin, BasicNbtKey.EntityUUID)
        if (uuid != null) {
            val camera = plugin.server.getEntity(UUID.fromString(uuid))
            if (camera == null || camera.isDead) {
                val itemMeta = itemStack.itemMeta ?: return
                itemMeta.persistentDataContainer.removeNbt(plugin, BasicNbtKey.EntityUUID)
                itemStack.itemMeta = itemMeta
                player.sendMessage("カメラは破壊されました")
            } else {
                val players = camera.getMeta(MetadataKey.CameraPlayers, listOf()).toMutableList()
                players.add(player)
                players.distinct()
                camera.setMeta(plugin, MetadataKey.CameraPlayers, players)
                player.playSound(player.location, Sound.BLOCK_SHULKER_BOX_OPEN, 1.0f, 1.0f)
                CameraPacket(camera).send(player)
                player.setMeta(plugin, MetadataKey.Camera, camera)
                player.setMeta(plugin, MetadataKey.UsingCamera, true)
            }
        } else {
            val itemMeta = itemStack.itemMeta ?: return
            val camera = createCamera(player.location, player, plugin)
            player.location.playSound(Sound.BLOCK_METAL_PLACE, 1.0f, 1.0f)
            itemMeta.persistentDataContainer.setNbt(plugin, BasicNbtKey.EntityUUID, camera.uniqueId.toString())
            itemStack.itemMeta = itemMeta
            player.sendMessage("カメラを設置しました")
        }
    }

    fun cancel(player: Player, itemStack: ItemStack, plugin: JavaPlugin) {
        val uuid = itemStack.itemMeta?.persistentDataContainer?.getNbt(plugin, BasicNbtKey.EntityUUID)
        if (uuid != null) {
            val camera = plugin.server.getEntity(UUID.fromString(uuid))
            camera?.location?.playSound(Sound.BLOCK_METAL_BREAK, 1.0f, 1.0f)
            val itemMeta = itemStack.itemMeta ?: return
            itemMeta.persistentDataContainer.removeNbt(plugin, BasicNbtKey.EntityUUID)
            itemStack.itemMeta = itemMeta
            camera?.remove()
            player.sendMessage("カメラを解除しました")
        }
    }

    fun end(player: Player, camera: Entity, plugin: JavaPlugin) {
        val players = camera.getMeta(MetadataKey.CameraPlayers, listOf()).toMutableList()
        players.remove(player)
        camera.setMeta(plugin, MetadataKey.CameraPlayers, players)
        CameraPacket(player).send(player)
        player.removeMeta(plugin, MetadataKey.Camera)
        player.setMeta(plugin, MetadataKey.UsingCamera, false)
    }

    open fun createCamera(location: Location, player: Player, plugin: JavaPlugin): Entity {
        return location.spawn<Creeper> {
            it.setAI(false)
            it.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 10000, 2))
            it.isCollidable = false
            it.persistentDataContainer.setNbt(plugin, BasicNbtKey.Name, this::class.simpleName)
        } as Creeper
    }
}