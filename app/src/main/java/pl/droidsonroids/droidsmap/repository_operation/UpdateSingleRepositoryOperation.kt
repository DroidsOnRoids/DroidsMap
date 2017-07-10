package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.base.UiModel
import pl.droidsonroids.droidsmap.model.OperationStatus

public interface UpdateSingleRepositoryOperation<in T : UiModel> {
    fun update(item: T): Observable<OperationStatus>
}