package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.model.Entity
import pl.droidsonroids.droidsmap.model.OperationStatus

public interface UpdateSingleRepositoryOperation<in T : Entity> {
    fun update(item: T): Observable<OperationStatus>
}