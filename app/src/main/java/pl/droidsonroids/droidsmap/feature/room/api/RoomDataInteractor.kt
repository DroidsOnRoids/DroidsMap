package pl.droidsonroids.droidsmap.feature.room.api

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import pl.droidsonroids.droidsmap.feature.office.api.BaseFirebaseInteractor
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntity
import pl.droidsonroids.droidsmap.model.OperationStatus
import rx.Observable
import rx.subscriptions.Subscriptions

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
                    it.onCompleted()
                }

                override fun onCancelled(p0: DatabaseError?) {
                    it.onNext(Pair(emptyList(), OperationStatus.FAILURE))
                    it.onCompleted()
                }
            }

            databaseQueryNode.addListenerForSingleValueEvent(queryListener)

            it.add(Subscriptions.create { databaseQueryNode.removeEventListener(queryListener) })
        })
    }

    private fun isOfficeDataComplete(snapshot: DataSnapshot?): Boolean = true

    private fun retrieveRoomsList(snapshot: DataSnapshot?): List<RoomEntity> =
            snapshot?.children?.map { it.getValue<RoomEntity>() } ?: emptyList<RoomEntity>()

    private inline fun <reified T> DataSnapshot.getValue() = getValue(T::class.java) as T

}