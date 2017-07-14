package pl.droidsonroids.droidsmap.feature.room.api

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntityHolder

interface RoomDataEndpoint {
    fun getRoomData(): Observable<RoomEntityHolder>

    companion object {
        fun create(): RoomDataEndpoint {
            return RoomDataInteractor()
        }
    }
}