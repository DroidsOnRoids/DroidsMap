package pl.droidsonroids.droidsmap.feature.employee.api

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import pl.droidsonroids.droidsmap.base.BaseFirebaseInteractor
import pl.droidsonroids.droidsmap.feature.employee.business_logic.EmployeeEntity
import pl.droidsonroids.droidsmap.feature.employee.business_logic.EmployeeEntityHolder

class EmployeeDataInteractor : BaseFirebaseInteractor(), EmployeeDataEndpoint {

    override fun setDatabaseNode() {
        databaseQueryNode = firebaseDatabase.reference
                .child("employees")
    }

    override fun getAllEmployees(): Observable<EmployeeEntityHolder> {
        setDatabaseNode()
        return Observable.create({ emitter ->
            val queryListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    retrieveRoomsList(snapshot).forEach(emitter::onNext)
                    emitter.onComplete()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            }

            emitter.setCancellable { databaseQueryNode.removeEventListener(queryListener) }
            databaseQueryNode.addListenerForSingleValueEvent(queryListener)
        })
    }

    private fun retrieveRoomsList(snapshot: DataSnapshot?): List<EmployeeEntityHolder> =
            snapshot?.children?.map {
                EmployeeEntityHolder(it.getValue<EmployeeEntity>(), it.key)
            } ?: emptyList<EmployeeEntityHolder>()

    private inline fun <reified T> DataSnapshot.getValue() = getValue(T::class.java) as T
}
