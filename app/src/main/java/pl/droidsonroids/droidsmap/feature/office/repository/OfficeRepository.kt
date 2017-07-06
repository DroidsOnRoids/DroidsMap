package pl.droidsonroids.droidsmap.feature.office.repository

import io.reactivex.Single
import pl.droidsonroids.droidsmap.feature.office.api.OfficeDataEndpoint
import pl.droidsonroids.droidsmap.feature.office.api.OfficeDataInteractor
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.repository_operation.QuerySingleRepositoryOperation

class OfficeRepository : QuerySingleRepositoryOperation<OfficeEntity> {
    private val officeDataSource: OfficeDataEndpoint = OfficeDataInteractor()

    override fun query(): Single<OfficeEntity> = officeDataSource.getOfficeData()
}