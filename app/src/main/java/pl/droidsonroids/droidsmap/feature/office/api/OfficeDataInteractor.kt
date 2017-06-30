package pl.droidsonroids.droidsmap.feature.office.api

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.model.OperationStatus
import rx.Observable
import rx.Observable.create
import rx.subscriptions.Subscriptions

class OfficeDataInteractor : BaseFirebaseInteractor<Pair<OfficeEntity, OperationStatus>>(), OfficeDataEndpoint {

    override fun setDatabaseNode() {
        databaseQueryNode = firebaseDatabase.reference
                .child("office")
    }

    override fun getOfficeData(): Observable<Pair<OfficeEntity, OperationStatus>> {
        setDatabaseNode()
        return create({
            val queryListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    it.onNext(Pair(
                            snapshot.getValue(OfficeEntity::class.java) as OfficeEntity,
                            if (isOfficeDataComplete()) OperationStatus.SUCCESS else OperationStatus.FAILURE))
                    it.onCompleted()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    it.onNext(Pair(OfficeEntity(), OperationStatus.FAILURE))
                    it.onCompleted()
                }
            }

            databaseQueryNode.addListenerForSingleValueEvent(queryListener)

            it.add(Subscriptions.create { databaseQueryNode.removeEventListener(queryListener) })
        })
    }

    private fun isOfficeDataComplete() = true
}
