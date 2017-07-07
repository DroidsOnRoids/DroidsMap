package pl.droidsonroids.droidsmap.feature.office.business_logic

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.base.DisposableHandler
import pl.droidsonroids.droidsmap.feature.office.api.OfficeDataEndpoint
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository

class OfficeFeatureUseCase : OfficeFeatureBoundary {

    val officeRepository: OfficeRepository = OfficeRepository(OfficeDataEndpoint.create())
    val disposableHandler = DisposableHandler()

    override fun requestOffice(dataObserver: DataObserverAdapter<OfficeUiModel>) {
        disposableHandler handle officeRepository
                .query()
                .toObservable()
                .applySchedulers()
                .subscribeWith(dataObserver)
    }

    override fun setOfficeCenterLocation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
