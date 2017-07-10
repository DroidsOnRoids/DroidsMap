package pl.droidsonroids.droidsmap.feature.employee.business_logic

import pl.droidsonroids.droidsmap.model.Entity


data class EmployeeEntity(
        val name : String = "",
        val surname : String = "",
        val position : String = ""
) : Entity()