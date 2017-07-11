package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.base.UiModel
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity

data class OfficeUiModel(
        val centerLatitude: Double,
        val centerLongitude: Double,
        val leftTopCornerLatitude: Double,
        val leftTopCornerLongitude: Double,
        val mapViewportBearing: Double,
        val translatedMapViewportBearing: Double,
        val mapViewportConstraint: Double) : UiModel {
    companion object {
        fun from(entity: OfficeEntity) = OfficeUiModel(
                entity.centerLatitude,
                entity.centerLongitude,
                entity.topLeftCornerLatitude,
                entity.topLeftCornerLongitude,
                entity.mapViewportBearing,
                entity.translatedMapViewportBearing,
                entity.mapViewportConstraint
        )
    }
}
