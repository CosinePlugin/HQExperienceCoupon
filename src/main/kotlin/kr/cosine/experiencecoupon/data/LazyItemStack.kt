package kr.cosine.experiencecoupon.data

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class LazyItemStack(
    private val material: Material,
    private val displayName: String,
    private val lore: List<String>,
    private val customModelData: Int
) {
    fun isSimilar(itemStack: ItemStack): Boolean {
        return toItemStack().isSimilar(itemStack)
    }

    fun toItemStack(replace: (String) -> String = { it }): ItemStack {
        return ItemStack(material).apply {
            editMeta {
                val newDisplayName = replace(this@LazyItemStack.displayName)
                if (newDisplayName.isNotEmpty()) {
                    it.setDisplayName(newDisplayName)
                }
                val newLore = this@LazyItemStack.lore.map(replace)
                if (newLore.isNotEmpty()) {
                    lore = newLore
                }
                val newCustomModelData = this@LazyItemStack.customModelData
                if (newCustomModelData != 0) {
                    it.setCustomModelData(newCustomModelData)
                }
            }
        }
    }
}