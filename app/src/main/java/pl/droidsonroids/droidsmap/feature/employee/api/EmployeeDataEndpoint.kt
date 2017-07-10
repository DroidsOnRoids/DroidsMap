package pl.droidsonroids.droidsmap.feature.employee.api

import io.reactivex.Observable
import pl.droidsonroids.droidsmap.feature.employee.business_logic.EmployeeEntity
import pl.droidsonroids.droidsmap.feature.employee.business_logic.EmployeeEntityHolder

interface EmployeeDataEndpoint {
    fun getAllEmployees(): Observable<EmployeeEntityHolder>
}
