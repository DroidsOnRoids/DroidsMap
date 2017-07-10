package pl.droidsonroids.droidsmap.feature.office.business_logic

import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel

interface OfficeFeatureBoundary {

    fun requestOffice(dataObserver: DataObserverAdapter<OfficeUiModel>)

    fun changeToRoomPerspective()

    companion object {
        fun create() = OfficeFeatureUseCase()
    }
}