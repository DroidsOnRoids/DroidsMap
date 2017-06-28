package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.model.Entity
import pl.droidsonroids.droidsmap.model.OperationStatus
import rx.Observable

public interface QuerySingleRepositoryOperation<T : Entity> {
    fun query(): Observable<Pair<T, OperationStatus>>
}