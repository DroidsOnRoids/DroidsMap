package pl.droidsonroids.droidsmap.feature.office.api

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import rx.subjects.PublishSubject

abstract class BaseFirebaseInteractor<T> {
    protected lateinit var firebaseDatabase: FirebaseDatabase
    protected lateinit var databaseQueryNode: DatabaseReference
    protected val publishSubject: PublishSubject<T> = PublishSubject.create()

    abstract fun setDatabaseNode()
}