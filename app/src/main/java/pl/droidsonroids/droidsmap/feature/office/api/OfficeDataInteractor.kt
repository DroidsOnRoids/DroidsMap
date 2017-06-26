package pl.droidsonroids.droidsmap.feature.office.api

import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.model.OperationStatus
import rx.Observable

class OfficeDataInteractor : IOfficeDataInteractor {
    override fun getOfficeData(): Observable<Pair<OfficeEntity, OperationStatus>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}