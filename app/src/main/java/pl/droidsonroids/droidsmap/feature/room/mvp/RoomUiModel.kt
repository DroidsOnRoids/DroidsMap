package pl.droidsonroids.droidsmap.feature.room.mvp

import pl.droidsonroids.droidsmap.R
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntity

class RoomUiModel(
        private val roomHeightPx: Int,
        private val roomWidthPx: Int,
        private val relativeCenterXPositionPx: Double,
        private val relativeCenterYPositionPx: Double,
        val tag: String,
        val imageResource: Int) {
    companion object {
        fun from(entity: RoomEntity) = RoomUiModel(
                entity.roomHeightPx,
                entity.roomWidthPx,
                entity.relativeCenterXPositionPx,
                entity.relativeCenterYPositionPx,
                "",
                R.drawable.room_3
        )
    }
}