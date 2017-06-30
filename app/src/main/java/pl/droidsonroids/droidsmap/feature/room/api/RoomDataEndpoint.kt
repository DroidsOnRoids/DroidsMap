package pl.droidsonroids.droidsmap.feature.room.api

import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntity
import pl.droidsonroids.droidsmap.model.OperationStatus
import rx.Observable

interface RoomDataEndpoint {
    fun getRoomData(): Observable<Pair<List<RoomEntity>, OperationStatus>>
}