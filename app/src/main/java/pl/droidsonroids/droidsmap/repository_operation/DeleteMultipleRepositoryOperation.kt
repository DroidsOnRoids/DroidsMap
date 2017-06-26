package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.model.Entity
import pl.droidsonroids.droidsmap.model.OperationStatus
import rx.Observable

public interface DeleteMultipleRepositoryOperation<T : Entity> {
    fun delete(items: List<T>): Observable<Pair<List<T>, OperationStatus>>
}