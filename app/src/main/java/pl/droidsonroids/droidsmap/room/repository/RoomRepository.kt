package pl.droidsonroids.droidsmap.room.repository

class RoomRepository {
    private lateinit var roomDataSource: RoomDataSource

    init {
        roomDataSource = RoomDataSource()
    }
}