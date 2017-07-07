package pl.droidsonroids.droidsmap.feature.room.api

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntity
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntityHolder
import pl.droidsonroids.droidsmap.model.OperationStatus

interface RoomDataEndpoint {
    fun getRoomData(): Observable<RoomEntityHolder>
}