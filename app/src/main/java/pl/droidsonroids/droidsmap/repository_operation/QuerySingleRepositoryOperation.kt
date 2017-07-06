package pl.droidsonroids.droidsmap.repository_operation

import io.reactivex.Single
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.model.Entity

public interface QuerySingleRepositoryOperation<T : Entity> {
    fun query(): Single<OfficeEntity>
}