package pl.droidsonroids.droidsmap.feature.office.api

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

abstract class BaseFirebaseInteractor<T> {
    protected val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    protected lateinit var databaseQueryNode: DatabaseReference

    abstract fun setDatabaseNode()
}