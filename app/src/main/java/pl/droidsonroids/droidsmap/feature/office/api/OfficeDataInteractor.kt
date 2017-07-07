package pl.droidsonroids.droidsmap.feature.office.api

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single
import pl.droidsonroids.droidsmap.base.BaseFirebaseInteractor
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity

class OfficeDataInteractor : BaseFirebaseInteractor(), OfficeDataEndpoint {

    override fun setDatabaseNode() {
        databaseQueryNode = firebaseDatabase.reference
                .child("office")
    }

    override fun getOfficeData(): Single<OfficeEntity> {
        setDatabaseNode()
        return Single.create { emitter ->
            val queryListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    emitter.onSuccess(snapshot.getValue(OfficeEntity::class.java) as OfficeEntity)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            }
            emitter.setCancellable({ databaseQueryNode.removeEventListener(queryListener) })
            databaseQueryNode.addListenerForSingleValueEvent(queryListener)
        }
    }

    private fun isOfficeDataComplete() = true
}
