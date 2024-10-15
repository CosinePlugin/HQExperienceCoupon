package kr.cosine.experiencecoupon.enums

import kr.cosine.experiencecoupon.data.Sound
import org.bukkit.entity.Player

enum class Notice(
    private var message: String,
    private var sound: Sound?
) {
    ZERO_EXPERIENCE("§c보유 중인 경험치가 없습니다.", null),
    LACK_INVENTORY("§c인벤토리에 공간이 부족합니다.", null),
    USE_EXPERIENCE_EXTRACT_COUPON("§a%experience% 경험치를 추출하였습니다.", null),
    USE_EXPERIENCE_COUPON("§a경험치권을 사용하였습니다. (+%experience%EXP)", null);

    fun notice(player: Player, replace: (String) -> String = { it }) {
        if (message.isNotEmpty()) {
            player.sendMessage(replace(message))
        }
        sound?.playSound(player)
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun setSound(sound: Sound?) {
        this.sound = sound
    }

    companion object {
        fun of(text: String): Notice? {
            return runCatching { valueOf(text.uppercase().replace("-", "_")) }.getOrNull()
        }
    }
}