package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.base.UiModel
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomUiModel

data class OfficeUiModel(
        val centerLatitude: Double,
        val centerLongitude: Double,
        val leftTopCornerLatitude: Double,
        val leftTopCornerLongitude: Double,
        val mapViewportBearing: Double,
        val translatedMapViewportBearing: Double,
        val mapViewportConstraint: Double,
        val roomUiModels: Collection<RoomUiModel>) : UiModel {
    companion object {
        fun from(entity: OfficeEntity, roomModels: Collection<RoomUiModel>) = OfficeUiModel(
                entity.centerLatitude,
                entity.centerLongitude,
                entity.topLeftCornerLatitude,
                entity.topLeftCornerLongitude,
                entity.mapViewportBearing,
                entity.translatedMapViewportBearing,
                entity.mapViewportConstraint,
                roomModels
        )
    }
}
