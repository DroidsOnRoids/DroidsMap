package pl.droidsonroids.droidsmap.feature.room.repository

import io.reactivex.Single
import pl.droidsonroids.droidsmap.feature.room.api.RoomDataEndpoint
import pl.droidsonroids.droidsmap.feature.room.api.RoomDataInteractor
import pl.droidsonroids.droidsmap.feature.room.api.RoomImagesEndpoint
import pl.droidsonroids.droidsmap.feature.room.api.RoomImagesInteractor
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomUiModel
import pl.droidsonroids.droidsmap.repository_operation.QueryMultipleRepositoryOperation

class RoomRepository : QueryMultipleRepositoryOperation<RoomUiModel> {
    private val roomDataSource: RoomDataEndpoint = RoomDataInteractor()
    private val roomImagesDataSource: RoomImagesEndpoint = RoomImagesInteractor()

    override fun query(): Single<List<RoomUiModel>> =
            roomDataSource.getRoomData()
                    .flatMap { (room, roomId) ->
                        roomImagesDataSource
                                .getRoomImageUrl(roomId)
                                .map {
                                    roomUriString -> RoomUiModel.from(room, roomUriString)
                                }
                                .toObservable()
                    }.toList()
}