package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.model.Entity
import pl.droidsonroids.droidsmap.model.OperationStatus
import rx.Observable

public interface DeleteSingleRepositoryOperation<T : Entity> {
    fun delete(item: T): Observable<Pair<T, OperationStatus>>
}