package pl.droidsonroids.droidsmap.feature.room.repository

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.feature.room.api.RoomDataInteractor
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntity
import pl.droidsonroids.droidsmap.model.OperationStatus
import pl.droidsonroids.droidsmap.repository_operation.QueryMultipleRepositoryOperation

class RoomRepository : QueryMultipleRepositoryOperation<RoomEntity> {
    private val roomDataSource: RoomDataInteractor = RoomDataInteractor()

    override fun query(): Observable<Pair<List<RoomEntity>, OperationStatus>> = roomDataSource.getRoomData()
}