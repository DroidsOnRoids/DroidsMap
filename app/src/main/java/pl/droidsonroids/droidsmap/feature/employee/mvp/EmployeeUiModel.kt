package pl.droidsonroids.droidsmap.feature.employee.mvp

import pl.droidsonroids.droidsmap.base.UiModel
import pl.droidsonroids.droidsmap.feature.employee.business_logic.EmployeeEntity


class EmployeeUiModel (
    val name : String,
    val surname : String,
    val position : String,
    val imageUrl : String
) : UiModel {
    companion object {
        fun from(entity: EmployeeEntity, imageUrl : String) = EmployeeUiModel(
                entity.name,
                entity.surname,
                entity.position,
                imageUrl
        )
    }
}
