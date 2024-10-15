package kr.cosine.experiencecoupon.service

import kr.cosine.experiencecoupon.data.LazyItemStack
import kr.cosine.experiencecoupon.enums.Notice
import kr.cosine.experiencecoupon.registry.SettingRegistry
import kr.hqservice.framework.global.core.component.Service
import kr.hqservice.framework.inventory.util.hasSpace
import kr.hqservice.framework.nms.extension.getNmsItemStack
import kr.hqservice.framework.nms.extension.nms
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.UUID

@Service
class ExperienceCouponService(
    private val settingRegistry: SettingRegistry
) {
    private val cooldownMap = mutableMapOf<UUID, Long>()

    fun useExperienceExtractCoupon(player: Player, itemStack: ItemStack): Boolean {
        val experienceExtractCoupon = settingRegistry.findExperienceExtractCoupon() ?: return false
        if (!experienceExtractCoupon.isSimilar(itemStack)) return false
        val experienceCoupon = settingRegistry.findExperienceCoupon() ?: return false
        val experience = player.totalExperience
        if (experience <= 0) {
            Notice.ZERO_EXPERIENCE.notice(player)
            return true
        }
        val experienceCouponItemStack = experienceCoupon.createExperienceCoupon(experience)
        val playerInventory = player.inventory
        if (!playerInventory.hasSpace(experienceCouponItemStack)) {
            Notice.LACK_INVENTORY.notice(player)
            return true
        }
        cooldownMap[player.uniqueId] = System.currentTimeMillis() + 500
        itemStack.amount--
        player.totalExperience = 0
        player.level = 0
        player.exp = 0f
        playerInventory.addItem(experienceCouponItemStack)
        Notice.USE_EXPERIENCE_EXTRACT_COUPON.notice(player) {
            it.replace(EXPERIENCE_REPLACER, "$experience")
        }
        return true
    }

    fun useExperienceCoupon(player: Player, itemStack: ItemStack): Boolean {
        val experience = itemStack.getNmsItemStack().getTagOrNull()?.getIntOrNull(EXPERIENCE_KEY) ?: return false
        if (cooldownMap.getOrDefault(player.uniqueId, 0) > System.currentTimeMillis()) return true
        removeCooldown(player.uniqueId)
        itemStack.amount--
        player.giveExp(experience)
        Notice.USE_EXPERIENCE_COUPON.notice(player) {
            it.replace(EXPERIENCE_REPLACER, "$experience")
        }
        return true
    }

    fun giveExperienceExtractCoupon(player: Player) {
        val experienceExtractCoupon = settingRegistry.findExperienceExtractCoupon() ?: run {
            player.sendMessage("§c경험치 추출권이 설정되어 있지 않습니다.")
            return
        }
        val experienceExtractCouponItemStack = experienceExtractCoupon.toItemStack()
        player.inventory.addItem(experienceExtractCouponItemStack)
        player.sendMessage("§a경험치 추출권이 지급되었습니다.")
    }

    fun giveExperienceCoupon(player: Player, experience: Int) {
        val experienceCoupon = settingRegistry.findExperienceCoupon() ?: run {
            player.sendMessage("§c경험치 쿠폰이 설정되어 있지 않습니다.")
            return
        }
        val experienceCouponItemStack = experienceCoupon.createExperienceCoupon(experience)
        player.inventory.addItem(experienceCouponItemStack)
        player.sendMessage("§a$experience 경험치 쿠폰이 지급되었습니다.")
    }

    private fun LazyItemStack.createExperienceCoupon(experience: Int): ItemStack {
        return toItemStack {
            it.replace(EXPERIENCE_REPLACER, "$experience")
        }.nms {
            tag {
                setInt(EXPERIENCE_KEY, experience)
            }
        }
    }

    fun removeCooldown(uniqueId: UUID) {
        cooldownMap.remove(uniqueId)
    }

    fun clearCooldown() {
        cooldownMap.clear()
    }

    private companion object {
        const val EXPERIENCE_KEY = "HQExperienceCoupon"
        const val EXPERIENCE_REPLACER = "%experience%"
    }
}