package pl.droidsonroids.droidsmap.feature.office.business_logic

import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureBoundary
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomUiModel

interface OfficeFeatureBoundary {

    fun requestOffice(dataObserver: DataObserverAdapter<OfficeUiModel>)

    fun requestRooms(dataObserver: DataObserverAdapter<Collection<RoomUiModel>>)

    fun changeToRoomPerspective()

    companion object {
        fun create(roomBoundary: RoomFeatureBoundary? = null, repository: OfficeRepository) = OfficeFeatureUseCase(roomBoundary, repository)
    }
}