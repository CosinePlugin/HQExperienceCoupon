package kr.cosine.experiencecoupon.config

import kr.cosine.experiencecoupon.data.LazyItemStack
import kr.cosine.experiencecoupon.data.Sound
import kr.cosine.experiencecoupon.enums.Notice
import kr.cosine.experiencecoupon.registry.SettingRegistry
import kr.hqservice.framework.bukkit.core.extension.colorize
import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.framework.yaml.config.HQYamlConfiguration
import kr.hqservice.framework.yaml.config.HQYamlConfigurationSection
import org.bukkit.Material

@Bean
class SettingConfig(
    private val config: HQYamlConfiguration,
    private val settingRegistry: SettingRegistry
) {
    fun load() {
        loadExperienceExtractCoupon()
        loadExperienceCoupon()
        loadMessage()
        loadSound()
    }

    private fun loadExperienceExtractCoupon() {
        val experienceExtractCoupon = config.findLazyItemStack("experience-extract-coupon") ?: return
        settingRegistry.setExperienceExtractCoupon(experienceExtractCoupon)
    }

    private fun loadExperienceCoupon() {
        val experienceCoupon = config.findLazyItemStack("experience-coupon") ?: return
        settingRegistry.setExperienceCoupon(experienceCoupon)
    }

    private fun HQYamlConfigurationSection.findLazyItemStack(path: String): LazyItemStack? {
        return getSection(path)?.let {
            val material = it.getString("material").uppercase().run(Material::getMaterial) ?: return null
            val displayName = it.getString("display-name").colorize()
            val lore = it.getStringList("lore").map(String::colorize)
            val customModelData = it.getInt("custom-model-data")
            LazyItemStack(material, displayName, lore, customModelData)
        }
    }

    private fun loadMessage() {
        config.getSection("message")?.apply {
            getKeys().forEach { noticeText ->
                val notice = Notice.of(noticeText) ?: return@forEach
                val message = getString(noticeText).colorize()
                notice.setMessage(message)
            }
        }
    }

    private fun loadSound() {
        config.getSection("sound")?.apply {
            getKeys().forEach { noticeText ->
                val notice = Notice.of(noticeText) ?: return@forEach
                getSection(noticeText)?.apply {
                    val isEnabled = getBoolean("enabled")
                    val name = getString("name")
                    val volume = getDouble("volume").toFloat()
                    val pitch = getDouble("pitch").toFloat()
                    val sound = Sound(isEnabled, name, volume, pitch)
                    notice.setSound(sound)
                }
            }
        }
    }

    fun reload() {
        settingRegistry.clear()
        config.reload()
        load()
    }
}