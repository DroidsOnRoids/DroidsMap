package pl.droidsonroids.droidsmap.feature.office.business_logic

import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureBoundary

interface OfficeFeatureBoundary {

    fun requestOffice(officeDataObserver: DataObserverAdapter<OfficeUiModel>)

    fun changeToRoomPerspective()

    companion object {
        fun create(roomBoundary: RoomFeatureBoundary? = null, officeRepository: OfficeRepository)
                = OfficeFeatureUseCase(roomBoundary, officeRepository)
    }
}