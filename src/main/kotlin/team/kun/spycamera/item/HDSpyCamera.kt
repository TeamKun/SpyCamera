package team.kun.spycamera.item

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import team.kun.spycamera.ext.setNbt
import team.kun.spycamera.ext.spawn
import team.kun.spycamera.metadata.BasicNbtKey

open class HDSpyCamera : SpyCamera() {
    override val name = "HDスパイカメラ"
    override val description = listOf(
            "右クリックでスパイカメラを設置・使用",
            "左クリックでスパイカメラを解除",
            "カメラ起動中に左クリックで矢を発射"
    )
    override val itemStack = ItemStack(Material.GOLDEN_PICKAXE)

    override fun getRecipe(plugin: JavaPlugin): ShapedRecipe? {
        val key = getKey(plugin) ?: return null
        return ShapedRecipe(key, toItemStack(plugin)).apply {
            shape("GGG", "GIG", "GGG")
            setIngredient('G', Material.GLASS)
            setIngredient('I', Material.GOLD_INGOT)
        }
    }

    override fun createCamera(location: Location, player: Player, plugin: JavaPlugin): Entity {
        return location.spawn<Villager> {
            it.setAI(false)
            it.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 10000, 2))
            it.isCollidable = false
            it.persistentDataContainer.setNbt(plugin, BasicNbtKey.Name, this::class.simpleName)
        } as Villager
    }
}