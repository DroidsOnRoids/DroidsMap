package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.Entity
import pl.droidsonroids.droidsmap.StatusGateway
import rx.Observable

public interface UpdateMultipleRepositoryOperation<in T : Entity> {
    fun update(items: List<T>): Observable<StatusGateway>
}