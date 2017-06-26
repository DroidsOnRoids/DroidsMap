package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.model.Entity
import pl.droidsonroids.droidsmap.model.OperationStatus
import rx.Observable

public interface UpdateMultipleRepositoryOperation<in T : Entity> {
    fun update(items: List<T>): Observable<OperationStatus>
}