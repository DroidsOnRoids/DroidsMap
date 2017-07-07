package pl.droidsonroids.droidsmap.feature.room.api

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import pl.droidsonroids.droidsmap.feature.office.api.BaseFirebaseInteractor
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntity
import pl.droidsonroids.droidsmap.model.OperationStatus

class RoomDataInteractor : BaseFirebaseInteractor<Pair<OfficeEntity, OperationStatus>>(), RoomDataEndpoint {

    override fun setDatabaseNode() {
        databaseQueryNode = firebaseDatabase.reference
                .child("rooms")
                .child("employees_rooms")
    }

    override fun getRoomData(): Observable<Pair<List<RoomEntity>, OperationStatus>> {
        setDatabaseNode()
        return Observable.create({
            val queryListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    it.onNext(Pair(
                            retrieveRoomsList(snapshot),
                            if (isOfficeDataComplete(snapshot)) OperationStatus.SUCCESS else OperationStatus.FAILURE))
                    it.onComplete()
                }

                override fun onCancelled(p0: DatabaseError?) {
                    it.onNext(Pair(emptyList(), OperationStatus.FAILURE))
                    it.onComplete()
                }
            }

            it.setCancellable { databaseQueryNode.removeEventListener(queryListener) }
            databaseQueryNode.addListenerForSingleValueEvent(queryListener)
        })
    }

    private fun isOfficeDataComplete(snapshot: DataSnapshot?): Boolean = true

    private fun retrieveRoomsList(snapshot: DataSnapshot?): List<RoomEntity> =
            snapshot?.children?.map { it.getValue<RoomEntity>() } ?: emptyList<RoomEntity>()

    private inline fun <reified T> DataSnapshot.getValue() = getValue(T::class.java) as T

}