package pl.droidsonroids.droidsmap.base

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

const val OFFICE_NODE = "office"

abstract class BaseFirebaseInteractor {
    protected val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    protected lateinit var databaseQueryNode: DatabaseReference

    abstract fun setDatabaseNode()
}