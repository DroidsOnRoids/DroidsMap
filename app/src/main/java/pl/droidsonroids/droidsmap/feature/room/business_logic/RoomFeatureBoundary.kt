package pl.droidsonroids.droidsmap.feature.room.business_logic

import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary

interface RoomFeatureBoundary {
    companion object {
        fun create(officeFeatureBoundary: OfficeFeatureBoundary) = RoomFeatureUseCase(officeFeatureBoundary)
    }

    fun onRoomPerspectiveGained() = Unit
}