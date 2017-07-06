package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.model.Entity
import pl.droidsonroids.droidsmap.model.OperationStatus

public interface UpdateMultipleRepositoryOperation<in T : Entity> {
    fun update(items: List<T>): Observable<OperationStatus>
}