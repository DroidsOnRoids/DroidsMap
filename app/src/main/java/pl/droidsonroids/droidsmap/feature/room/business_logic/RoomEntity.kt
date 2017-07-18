package pl.droidsonroids.droidsmap.feature.room.business_logic

import pl.droidsonroids.droidsmap.model.Entity

data class RoomEntity(
        val roomHeightPx: Int = 0,
        val roomWidthPx: Int = 0,
        val relativeCenterXPosition: Double = 0.0,
        val relativeCenterYPosition: Double = 0.0
) : Entity()