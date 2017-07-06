package pl.droidsonroids.droidsmap.feature.office.business_logic

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import pl.droidsonroids.droidsmap.DataObserverAdapter
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository

class OfficeFeatureUseCase : OfficeFeatureBoundary {

    val officeRepository: OfficeRepository = OfficeRepository()
    val disposablesList = mutableListOf<Disposable>()

    override fun requestOffice(dataObserver: DataObserverAdapter<OfficeEntity>) {
        disposablesList += officeRepository
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
