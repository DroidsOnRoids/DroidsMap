package pl.droidsonroids.droidsmap.feature.office.api

import io.reactivex.Single
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity

interface OfficeDataEndpoint {
    fun getOfficeData(): Single<OfficeEntity>

    companion object {
        fun create() = OfficeDataInteractor()
    }
}