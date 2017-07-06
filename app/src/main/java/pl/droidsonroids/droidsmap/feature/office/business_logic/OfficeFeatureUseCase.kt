package pl.droidsonroids.droidsmap.feature.office.business_logic

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository

class OfficeFeatureUseCase : OfficeFeatureBoundary {

    val officeRepository: OfficeRepository = OfficeRepository()

    override fun requestOffice(gateway: OfficeFeatureBoundary.Gateway) {
        officeRepository
                .query()
                .applySchedulers()
                .subscribe { gateway.onOfficeEntityAvailable(it.first) }
    }

    override fun setOfficeCenterLocation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
