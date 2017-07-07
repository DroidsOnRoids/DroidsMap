package pl.droidsonroids.droidsmap.feature.worker.business_logic

import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.base.DisposableHandler
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository

class WorkerFeatureUseCase : WorkerFeatureBoundary {

    val officeRepository: OfficeRepository = OfficeRepository()
    val disposableHandler = DisposableHandler()
    override fun requestOffice(dataObserver: DataObserverAdapter<OfficeEntity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
