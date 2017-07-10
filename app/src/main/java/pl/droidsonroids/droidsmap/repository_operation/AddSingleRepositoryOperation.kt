package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.base.UiModel
import pl.droidsonroids.droidsmap.model.OperationStatus

public interface AddSingleRepositoryOperation<in T : UiModel> {
    fun add(item: T): Observable<OperationStatus>
}
