package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.Entity
import pl.droidsonroids.droidsmap.StatusGateway
import rx.Observable

public interface DeleteMultipleRepositoryOperation<T : Entity> {
    fun delete(items: List<T>): Observable<Pair<List<T>, StatusGateway>>
}