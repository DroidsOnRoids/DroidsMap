package pl.droidsonroids.droidsmap.feature.office.repository

import pl.droidsonroids.droidsmap.feature.office.api.OfficeDataEndpoint
import pl.droidsonroids.droidsmap.feature.office.api.OfficeDataInteractor
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.model.OperationStatus
import pl.droidsonroids.droidsmap.repository_operation.QuerySingleRepositoryOperation
import rx.Observable

class OfficeRepository : QuerySingleRepositoryOperation<OfficeEntity> {
    private val officeDataSource: OfficeDataEndpoint = OfficeDataInteractor()

    override fun query(): Observable<Pair<OfficeEntity, OperationStatus>> = officeDataSource.getOfficeData()
}