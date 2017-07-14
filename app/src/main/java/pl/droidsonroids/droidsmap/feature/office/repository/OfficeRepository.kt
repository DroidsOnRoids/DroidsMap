package pl.droidsonroids.droidsmap.feature.office.repository

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import pl.droidsonroids.droidsmap.feature.office.api.OfficeDataEndpoint
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.feature.room.api.RoomDataEndpoint
import pl.droidsonroids.droidsmap.feature.room.api.RoomImagesEndpoint
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomUiModel
import pl.droidsonroids.droidsmap.repository_operation.QuerySingleRepositoryOperation

open class OfficeRepository(
        private val officeDataSource: OfficeDataEndpoint,
        private val roomsDataEndpoint: RoomDataEndpoint,
        private val roomImagesEndpoint: RoomImagesEndpoint
) : QuerySingleRepositoryOperation<OfficeUiModel> {

    override fun query(): Single<OfficeUiModel> {
        return Observable.combineLatest(
                officeDataSource.getOfficeData().toObservable(),
                getRooms().toObservable(),
                BiFunction  {
                    officeEntiity: OfficeEntity, roomUiModels: List<RoomUiModel> ->
                    return@BiFunction OfficeUiModel.from(officeEntiity, roomUiModels)
                }).singleOrError()
    }

    fun getRooms(): Single<List<RoomUiModel>> {
        return roomsDataEndpoint.getRoomData()
                .flatMap { (room, roomId) ->
                    roomImagesEndpoint
                            .getRoomImageUrl(roomId)
                            .map {
                                roomUriString -> RoomUiModel.from(room, roomUriString)
                            }
                            .toObservable()
                }.toList()
    }
}