package pl.droidsonroids.droidsmap.feature.room.api

import io.reactivex.Single

interface RoomImagesEndpoint {
    fun getRoomData(imageId : String): Single<String>
}