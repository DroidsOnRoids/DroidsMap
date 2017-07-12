package pl.droidsonroids.droidsmap.feature.room.api

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import pl.droidsonroids.droidsmap.base.BaseFirebaseInteractor
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntity
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntityHolder

class RoomDataInteractor : BaseFirebaseInteractor(), RoomDataEndpoint {

    override fun setDatabaseNode() {
        databaseQueryNode = firebaseDatabase.reference
                .child("rooms")
    }

    override fun getRoomData(): Observable<RoomEntityHolder> {
        setDatabaseNode()
        return Observable.create({ emitter ->
            val queryListener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    retrieveRoomsList(snapshot).forEach(emitter::onNext)
                    emitter.onComplete()
                }

                override fun onCancelled(databaseError: DatabaseError) = emitter.onError(databaseError.toException())
            }

            emitter.setCancellable { databaseQueryNode.removeEventListener(queryListener) }
            databaseQueryNode.addListenerForSingleValueEvent(queryListener)
        })
    }

    private fun retrieveRoomsList(snapshot: DataSnapshot?): List<RoomEntityHolder> =
            snapshot?.children?.map {
                RoomEntityHolder(it.getSnapshotValue<RoomEntity>(), it.key)
            } ?: emptyList<RoomEntityHolder>()

    private inline fun <reified T> DataSnapshot.getSnapshotValue() = getValue(T::class.java) as T

}