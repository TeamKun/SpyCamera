package team.kun.spycamera.item

import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import team.kun.spycamera.ext.getNbt
import team.kun.spycamera.ext.setNbt
import team.kun.spycamera.metadata.BasicNbtKey

abstract class Item {
    abstract val name: String
    abstract val description: List<String>
    abstract val itemStack: ItemStack

    fun toItemStack(plugin: JavaPlugin): ItemStack {
        val resultItemStack = itemStack
        val itemMeta = resultItemStack.itemMeta
        itemMeta?.setDisplayName(name)
        itemMeta?.lore = description
        itemMeta?.persistentDataContainer?.setNbt(plugin, BasicNbtKey.Name, this::class.simpleName)
        resultItemStack.itemMeta = itemMeta
        return resultItemStack
    }

    fun toItemStack(amount: Int, plugin: JavaPlugin): ItemStack {
        val itemStack = toItemStack(plugin)
        itemStack.amount = amount
        return itemStack
    }

    fun equal(targetItemStack: ItemStack, plugin: JavaPlugin): Boolean {
        return this::class.simpleName == targetItemStack.itemMeta?.persistentDataContainer?.getNbt(plugin, BasicNbtKey.Name)
    }
}