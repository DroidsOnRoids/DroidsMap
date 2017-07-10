package pl.droidsonroids.droidsmap.feature.employee.api

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import io.reactivex.Single
import io.reactivex.Single.create
import pl.droidsonroids.droidsmap.base.BaseFirebaseStorageInteractor


class EmployeeImageInteractor  : BaseFirebaseStorageInteractor(), EmployeeImageEndpoint {

    override fun setStorageNode() {
        storageQueryNode = firebaseStorage.reference
                .child("employees")
    }

    override fun getEmployeeImageUrl(employeeId: String): Single<String> {
        return create { emitter ->
            setStorageNode()
            val downloadingTask = storageQueryNode.child(employeeId + ".jpg").downloadUrl

            val queryListener = object : OnCompleteListener<Uri> {
                override fun onComplete(task: Task<Uri>) {
                    if (task.isSuccessful) {
                        emitter.onSuccess(task.result.toString())
                    } else {
                        emitter.onError(Exception())    //todo refactor
                    }
                }
            }
            downloadingTask.addOnCompleteListener(queryListener)
        }
    }
}