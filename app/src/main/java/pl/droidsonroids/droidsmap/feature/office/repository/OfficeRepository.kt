package pl.droidsonroids.droidsmap.feature.office.repository

import io.reactivex.Single
import pl.droidsonroids.droidsmap.feature.office.api.OfficeDataEndpoint
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.repository_operation.QuerySingleRepositoryOperation

open class OfficeRepository(private val officeDataSource: OfficeDataEndpoint) : QuerySingleRepositoryOperation<OfficeUiModel> {

    override fun query(): Single<OfficeUiModel> = officeDataSource
            .getOfficeData()
            .map { OfficeUiModel.from(it) }
}