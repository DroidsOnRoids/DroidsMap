package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.Entity
import pl.droidsonroids.droidsmap.StatusGateway
import rx.Observable

public interface UpdateSingleRepositoryOperation<in T : Entity> {
    fun update(item: T): Observable<StatusGateway>
}