package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.model.Entity
import pl.droidsonroids.droidsmap.model.OperationStatus
import rx.Observable

public interface AddSingleRepositoryOperation<in T : Entity> {
    fun add(item: T): Observable<OperationStatus>
}
