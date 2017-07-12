package pl.droidsonroids.droidsmap.feature.office.business_logic

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.base.DisposableHandler
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureBoundary
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomUiModel

class OfficeFeatureUseCase(roomBoundary: RoomFeatureBoundary?, val officeRepository: OfficeRepository) : OfficeFeatureBoundary {

    val disposableHandler = DisposableHandler()
    val roomFeatureBoundary = roomBoundary ?: RoomFeatureBoundary.create(this)

    override fun requestOffice(dataObserver: DataObserverAdapter<OfficeUiModel>) {
        disposableHandler handle officeRepository
                .query()
                .toObservable()
                .applySchedulers()
                .subscribeWith(dataObserver)
    }

    override fun requestRooms(dataObserver: DataObserverAdapter<Collection<RoomUiModel>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeToRoomPerspective() = roomFeatureBoundary.onRoomPerspectiveGained()
}

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
