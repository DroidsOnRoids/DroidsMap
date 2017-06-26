package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.model.Entity
import pl.droidsonroids.droidsmap.model.OperationStatus
import rx.Observable

public interface AddMultipleRepositoryOperation<in T : Entity> {
    fun add(items: List<T>): Observable<OperationStatus>
}