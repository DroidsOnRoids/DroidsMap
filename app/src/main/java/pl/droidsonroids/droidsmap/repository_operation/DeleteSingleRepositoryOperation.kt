package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.Entity
import pl.droidsonroids.droidsmap.StatusGateway
import rx.Observable

public interface DeleteSingleRepositoryOperation<T : Entity> {
    fun delete(item: T): Observable<Pair<T, StatusGateway>>
}