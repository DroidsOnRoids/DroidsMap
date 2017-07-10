package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.base.UiModel

public interface DeleteMultipleRepositoryOperation<T : UiModel> {
    fun delete(items: List<T>): Observable<List<T>>
}