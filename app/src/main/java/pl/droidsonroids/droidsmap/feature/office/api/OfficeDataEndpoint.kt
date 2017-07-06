package pl.droidsonroids.droidsmap.feature.office.api

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.model.OperationStatus

interface OfficeDataEndpoint {
    fun getOfficeData(): Observable<Pair<OfficeEntity, OperationStatus>>
}