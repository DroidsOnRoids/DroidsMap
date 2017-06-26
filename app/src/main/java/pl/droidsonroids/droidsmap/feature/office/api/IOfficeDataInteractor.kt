package pl.droidsonroids.droidsmap.feature.office.api

import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.model.OperationStatus
import rx.Observable

interface IOfficeDataInteractor {
    fun getOfficeData(): Observable<Pair<OfficeEntity, OperationStatus>>
}