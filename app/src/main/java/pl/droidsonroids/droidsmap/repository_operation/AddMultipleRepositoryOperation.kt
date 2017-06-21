package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.Entity
import pl.droidsonroids.droidsmap.StatusGateway
import rx.Observable

public interface AddMultipleRepositoryOperation<in T : Entity> {
    fun add(items: List<T>): Observable<StatusGateway>
}