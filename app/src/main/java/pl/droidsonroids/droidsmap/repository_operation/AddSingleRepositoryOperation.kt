package pl.droidsonroids.droidsmap.repository_operation

import pl.droidsonroids.droidsmap.Entity
import pl.droidsonroids.droidsmap.StatusGateway
import rx.Observable

public interface AddSingleRepositoryOperation<in T : Entity> {
    fun add(item: T): Observable<StatusGateway>
}
