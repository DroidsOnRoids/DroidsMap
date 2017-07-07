package pl.droidsonroids.droidsmap.feature.office.business_logic

import pl.droidsonroids.droidsmap.base.DataObserverAdapter

interface OfficeFeatureBoundary {

    fun requestOffice(dataObserver: DataObserverAdapter<OfficeEntity>)

    fun setOfficeCenterLocation()

    companion object {
        fun create() = OfficeFeatureUseCase()
    }
}