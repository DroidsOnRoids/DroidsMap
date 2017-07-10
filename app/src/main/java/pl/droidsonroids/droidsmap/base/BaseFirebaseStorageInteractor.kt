package pl.droidsonroids.droidsmap.base

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

abstract class BaseFirebaseStorageInteractor {
    protected val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    protected lateinit var storageQueryNode: StorageReference

    abstract fun setStorageNode()
}
