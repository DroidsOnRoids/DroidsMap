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

    override fun getRoomData(imageId: String): Single<String> {
        setStorageNode()

        val downloadingTask = storageQueryNode.downloadUrl

        return create { emitter ->
            val queryListener = OnCompleteListener<Uri> { task ->
                if (task.isSuccessful) {
                    emitter.onSuccess(task.result.toString())
                } else {
                    emitter.onError(Exception())    //todo refactor
                }
            }
            downloadingTask.addOnCompleteListener(queryListener)
        }
    }
}