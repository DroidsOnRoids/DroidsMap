package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.base.UiModel
import pl.droidsonroids.droidsmap.model.OperationStatus

public interface AddMultipleRepositoryOperation<in T : UiModel> {
    fun add(items: List<T>): Observable<OperationStatus>
}