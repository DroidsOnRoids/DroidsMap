package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Single
import pl.droidsonroids.droidsmap.base.UiModel

public interface QueryMultipleRepositoryOperation<T : UiModel> {
    fun query(): Single<List<T>>
}