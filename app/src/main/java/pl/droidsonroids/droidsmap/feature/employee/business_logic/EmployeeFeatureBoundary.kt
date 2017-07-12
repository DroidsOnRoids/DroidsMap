package pl.droidsonroids.droidsmap.feature.employee.business_logic

import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity


interface EmployeeFeatureBoundary {
    fun requestOffice(dataObserver: DataObserverAdapter<OfficeEntity>)

    companion object {
        fun create() = EmployeeFeatureUseCase()
    }
}