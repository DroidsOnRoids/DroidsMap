package pl.droidsonroids.droidsmap.feature.employee.api

import io.reactivex.Single

interface EmployeeImageEndpoint {
    fun getEmployeeImageUrl(employeeId: String): Single<String>
}