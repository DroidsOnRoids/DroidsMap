package pl.droidsonroids.droidsmap.feature.room.repository

class RoomRepository {
    private lateinit var roomDataSource: RoomDataSource

    init {
        roomDataSource = RoomDataSource()
    }
}