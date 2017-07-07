package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.model.Entity
import pl.droidsonroids.droidsmap.model.OperationStatus

public interface AddMultipleRepositoryOperation<in T : Entity> {
    fun add(items: List<T>): Observable<OperationStatus>
}