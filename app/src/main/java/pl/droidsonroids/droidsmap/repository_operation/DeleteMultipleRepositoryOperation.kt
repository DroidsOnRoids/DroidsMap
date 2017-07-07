package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.model.Entity
import pl.droidsonroids.droidsmap.model.OperationStatus

public interface DeleteMultipleRepositoryOperation<T : Entity> {
    fun delete(items: List<T>): Observable<Pair<List<T>, OperationStatus>>
}