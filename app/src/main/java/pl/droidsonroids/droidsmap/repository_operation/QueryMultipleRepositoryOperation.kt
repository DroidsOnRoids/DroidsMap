package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.Entity
import pl.droidsonroids.droidsmap.StatusGateway
import rx.Observable

public interface QueryMultipleRepositoryOperation<T : Entity> {
    fun query(): Observable<Pair<List<T>, StatusGateway>>
}