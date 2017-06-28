package pl.droidsonroids.droidsmap.feature.office.api

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.model.OperationStatus
import rx.Observable
import rx.Observable.create
import rx.functions.Action1
import rx.observers.Subscribers

class OfficeDataInteractor : BaseFirebaseInteractor<Pair<OfficeEntity, OperationStatus>>(), OfficeDataEndpoint {

    private val OFFICE_ENDPOINT = "droidsmap/office"

    override fun setDatabaseNode() {
        databaseQueryNode = firebaseDatabase.getReference(OFFICE_ENDPOINT)
    }

    override fun getOfficeData(): Observable<Pair<OfficeEntity, OperationStatus>> {

        return create({

            val queryListener = object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    it.onNext(
                            Pair(
                                    OfficeEntity(snapshot.child("center_latitude").value as Float, snapshot.child("center_longitude").value as Float),
                                    if (isOfficeDataComplete()) OperationStatus.SUCCESS else OperationStatus.FAILURE))
                    it.onCompleted()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    it.onNext(Pair(OfficeEntity(), OperationStatus.FAILURE))
                    it.onCompleted()
                }
            }

            it.add(Subscribers.create(Action1 {
                databaseQueryNode.removeEventListener(queryListener)
            }))

            databaseQueryNode.addListenerForSingleValueEvent(queryListener)
        })
    }

    private fun isOfficeDataComplete(): Boolean {
        return true
    }
}
