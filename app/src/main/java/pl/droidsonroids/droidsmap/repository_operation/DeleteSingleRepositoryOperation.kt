package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.model.Entity
import pl.droidsonroids.droidsmap.model.OperationStatus

public interface DeleteSingleRepositoryOperation<T : Entity> {
    fun delete(item: T): Observable<Pair<T, OperationStatus>>
}