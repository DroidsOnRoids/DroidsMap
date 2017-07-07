package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Single
import pl.droidsonroids.droidsmap.base.UiModel

public interface QuerySingleRepositoryOperation<T : UiModel> {
    fun query(): Single<T>
}