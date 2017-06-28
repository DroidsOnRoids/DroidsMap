package pl.droidsonroids.droidsmap.feature.office.api

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

abstract class BaseFirebaseInteractor<T> {
    protected lateinit var firebaseDatabase: FirebaseDatabase
    protected lateinit var databaseQueryNode: DatabaseReference

    abstract fun setDatabaseNode()
}