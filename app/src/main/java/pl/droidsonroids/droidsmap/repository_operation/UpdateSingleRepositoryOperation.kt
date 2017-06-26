package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.model.Entity
import pl.droidsonroids.droidsmap.model.OperationStatus
import rx.Observable

public interface UpdateSingleRepositoryOperation<in T : Entity> {
    fun update(item: T): Observable<OperationStatus>
}