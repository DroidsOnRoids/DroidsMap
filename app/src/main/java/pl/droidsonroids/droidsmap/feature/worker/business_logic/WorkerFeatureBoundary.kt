package pl.droidsonroids.droidsmap.feature.worker.business_logic

import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity


interface WorkerFeatureBoundary {
    fun requestOffice(dataObserver: DataObserverAdapter<OfficeEntity>)

    companion object {
        fun create() = WorkerFeatureUseCase()
    }
}