package pl.droidsonroids.droidsmap.feature.room.mvp

import pl.droidsonroids.droidsmap.base.UiModel
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntity

class RoomUiModel(
        val roomHeightPx: Int,
        val roomWidthPx: Int,
        val relativeCenterXPositionPx: Double,
        val relativeCenterYPositionPx: Double,
        val imageUrl: String) : UiModel {
    companion object {
        fun from(entity: RoomEntity, imageUrl : String) = RoomUiModel(
                entity.roomHeightPx,
                entity.roomWidthPx,
                entity.relativeCenterXPositionPx,
                entity.relativeCenterYPositionPx,
                imageUrl
        )
    }
}