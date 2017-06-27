package pl.droidsonroids.droidsmap.feature.office.business_logic

import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class OfficeFeatureUseCase : OfficeFeatureBoundary {

    val officeRepository: OfficeRepository = OfficeRepository()

    override fun requestOffice(gateway: OfficeFeatureBoundary.Gateway) {
        officeRepository
                .query()
                .applySchedulers()
                .subscribe({ gateway.onOfficeEntityAvailable(it.first) })
    }

    override fun setOfficeCenterLocation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
