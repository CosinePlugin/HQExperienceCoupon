package kr.cosine.experiencecoupon.command

import kr.cosine.experiencecoupon.config.SettingConfig
import kr.cosine.experiencecoupon.service.ExperienceCouponService
import kr.hqservice.framework.command.ArgumentLabel
import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(label = "경험치쿠폰관리", isOp = true)
class ExperienceCouponAdminCommand(
    private val settingConfig: SettingConfig,
    private val experienceCouponService: ExperienceCouponService
) {
    @CommandExecutor("추출권지급", "경험치 추출권을 지급합니다.", priority = 1)
    fun giveExperienceExtractCoupon(player: Player) {
        experienceCouponService.giveExperienceExtractCoupon(player)
    }

    @CommandExecutor("쿠폰지급", "경험치 쿠폰을 지급합니다.", priority = 2)
    fun giveExperienceCoupon(
        player: Player,
        @ArgumentLabel("경험치") experience: Int
    ) {
        experienceCouponService.giveExperienceCoupon(player, experience)
    }

    @CommandExecutor("리로드", "config.yml을 리로드합니다.", priority = 3)
    fun reload(sender: CommandSender) {
        settingConfig.reload()
        experienceCouponService.clearCooldown()
        sender.sendMessage("§aconfig.yml을 리로드하였습니다.")
    }
}