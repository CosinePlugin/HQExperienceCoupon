package kr.cosine.experiencecoupon.registry

import kr.cosine.experiencecoupon.data.LazyItemStack
import kr.hqservice.framework.global.core.component.Bean

@Bean
class SettingRegistry {
    private var experienceExtractCoupon: LazyItemStack? = null

    private var experienceCoupon: LazyItemStack? = null

    fun findExperienceExtractCoupon(): LazyItemStack? {
        return experienceExtractCoupon
    }

    fun setExperienceExtractCoupon(experienceExtractCoupon: LazyItemStack?) {
        this.experienceExtractCoupon = experienceExtractCoupon
    }

    fun findExperienceCoupon(): LazyItemStack? {
        return experienceCoupon
    }

    fun setExperienceCoupon(experienceCoupon: LazyItemStack?) {
        this.experienceCoupon = experienceCoupon
    }

    fun clear() {
        experienceExtractCoupon = null
        experienceCoupon = null
    }
}