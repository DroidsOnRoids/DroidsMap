package pl.droidsonroids.droidsmap.feature.room.api

import io.reactivex.Single

interface RoomImagesEndpoint {
    fun getRoomImageUrl(imageId : String): Single<String>
}