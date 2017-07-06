package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.model.Entity
import pl.droidsonroids.droidsmap.model.OperationStatus

public interface AddSingleRepositoryOperation<in T : Entity> {
    fun add(item: T): Observable<OperationStatus>
}
