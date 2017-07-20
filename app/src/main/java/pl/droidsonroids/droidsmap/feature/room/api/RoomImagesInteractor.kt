package pl.droidsonroids.droidsmap.feature.room.api

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import io.reactivex.Single
import io.reactivex.Single.create
import pl.droidsonroids.droidsmap.base.BaseFirebaseStorageInteractor

class RoomImagesInteractor : BaseFirebaseStorageInteractor(), RoomImagesEndpoint {

    override fun setStorageNode() {
        storageQueryNode = firebaseStorage.reference
                .child("rooms")
    }

    override fun getRoomImageUrl(imageId: String): Single<String> {
        return create { emitter ->
            setStorageNode()
            val downloadingTask = storageQueryNode.child(imageId + ".svg").downloadUrl

            val queryListener = OnCompleteListener<Uri> { task ->
                if (task.isSuccessful) {
                    emitter.onSuccess(task.result.toString())
                } else {
                    task.exception?.let(emitter::onError)
                }
            }
            downloadingTask.addOnCompleteListener(queryListener)
        }
    }
}