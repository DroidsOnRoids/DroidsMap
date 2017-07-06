package pl.droidsonroids.droidsmap.feature.office.api

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.model.OperationStatus

class OfficeDataInteractor : BaseFirebaseInteractor<Pair<OfficeEntity, OperationStatus>>(), OfficeDataEndpoint {

    override fun setDatabaseNode() {
        databaseQueryNode = firebaseDatabase.reference
                .child("office")
    }

    override fun getOfficeData(): Observable<Pair<OfficeEntity, OperationStatus>> {
        setDatabaseNode()
        return Observable.create({ emitter ->
            val queryListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    emitter.onNext(Pair(
                            snapshot.getValue(OfficeEntity::class.java) as OfficeEntity,
                            if (isOfficeDataComplete()) OperationStatus.SUCCESS else OperationStatus.FAILURE))
                    emitter.onComplete()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onNext(Pair(OfficeEntity(), OperationStatus.FAILURE))
                    emitter.onComplete()
                }
            }
            emitter.setCancellable({ databaseQueryNode.removeEventListener(queryListener) })
            databaseQueryNode.addListenerForSingleValueEvent(queryListener)
        })
    }

    private fun isOfficeDataComplete() = true
}
