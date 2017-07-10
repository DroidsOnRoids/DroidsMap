package pl.droidsonroids.droidsmap.feature.office.business_logic

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.base.DisposableHandler
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureBoundary

class OfficeFeatureUseCase(roomBoundary: RoomFeatureBoundary?, val officeRepository: OfficeRepository) : OfficeFeatureBoundary {

    val disposableHandler = DisposableHandler()
    val roomFeatureBoundary: RoomFeatureBoundary

    init {
        if (roomBoundary == null) roomFeatureBoundary = RoomFeatureBoundary.create(this) else roomFeatureBoundary = roomBoundary
    }

    override fun requestOffice(dataObserver: DataObserverAdapter<OfficeUiModel>) {
        disposableHandler handle officeRepository
                .query()
                .toObservable()
                .applySchedulers()
                .subscribeWith(dataObserver)
    }

    override fun changeToRoomPerspective() = roomFeatureBoundary.onRoomPerspectiveGained()
}

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
