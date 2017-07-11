package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.base.UiModel

public interface DeleteSingleRepositoryOperation<T : UiModel> {
    fun delete(item: T): Observable<T>
}