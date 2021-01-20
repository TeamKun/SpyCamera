package team.kun.spycamera.item

import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin
import team.kun.spycamera.ext.spawn

class HDSpyCameraArrow : HDSpyCamera() {
    override val name = "HDスパイカメラ（矢）"
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
            setIngredient('G', Material.ARROW)
            setIngredient('I', RecipeChoice.ExactChoice(HDSpyCamera().toItemStack(plugin)))
        }
    }

    fun shot(camera: LivingEntity) {
        camera.eyeLocation.add(camera.eyeLocation.direction.multiply(0.2)).spawn<Arrow> {
            it.shooter = camera
            it.velocity = camera.eyeLocation.direction.multiply(2.0)
        }
    }
}