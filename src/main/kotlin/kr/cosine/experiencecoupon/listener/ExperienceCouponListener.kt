package kr.cosine.experiencecoupon.listener

import kr.cosine.experiencecoupon.service.ExperienceCouponService
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent

@Listener
class ExperienceCouponListener(
    private val experienceCouponService: ExperienceCouponService
) {
    @Subscribe
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!event.action.name.contains("RIGHT_CLICK")) return
        val player = event.player
        val itemStack = event.item ?: return
        if (experienceCouponService.useExperienceExtractCoupon(player, itemStack)) {
            event.isCancelled = true
            return
        }
        if (experienceCouponService.useExperienceCoupon(player, itemStack)) {
            event.isCancelled = true
        }
    }

    @Subscribe
    fun onPlayerQuit(event: PlayerQuitEvent) {
        experienceCouponService.removeCooldown(event.player.uniqueId)
    }
}