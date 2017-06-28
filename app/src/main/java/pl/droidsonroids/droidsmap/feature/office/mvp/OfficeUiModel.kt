package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity

class OfficeUiModel private constructor(val centerLatitude: Float, val centerLongitude: Float, val leftTopCornerLatitude: Float, val leftTopCornerLongitude: Float) {
    companion object {
        fun from(entity: OfficeEntity) = OfficeUiModel(
                entity.centerLatitude,
                entity.centerLongitude,
                entity.leftTopCornerLatitude,
                entity.leftTopCornerLongitude
        )
    }
}
