package pl.droidsonroids.droidsmap.base

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

abstract class BaseFirebaseInteractor {
    protected val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    protected lateinit var databaseQueryNode: DatabaseReference

    abstract fun setDatabaseNode()
}