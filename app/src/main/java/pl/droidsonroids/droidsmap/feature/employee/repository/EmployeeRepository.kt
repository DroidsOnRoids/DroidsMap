package pl.droidsonroids.droidsmap.feature.employee.repository

import io.reactivex.Single
import pl.droidsonroids.droidsmap.feature.employee.api.EmployeeDataEndpoint
import pl.droidsonroids.droidsmap.feature.employee.api.EmployeeDataInteractor
import pl.droidsonroids.droidsmap.feature.employee.api.EmployeeImageEndpoint
import pl.droidsonroids.droidsmap.feature.employee.api.EmployeeImageInteractor
import pl.droidsonroids.droidsmap.feature.employee.mvp.EmployeeUiModel
import pl.droidsonroids.droidsmap.repository_operation.QueryMultipleRepositoryOperation


class EmployeeRepository(
        private val employeeDataSource : EmployeeDataEndpoint = EmployeeDataInteractor(),
        private val employeeImageDataSource : EmployeeImageEndpoint = EmployeeImageInteractor()
) : QueryMultipleRepositoryOperation<EmployeeUiModel> {

    override fun query(): Single<List<EmployeeUiModel>> =
            employeeDataSource.getAllEmployees()
                    .flatMap { (room, employeeId) ->
                        employeeImageDataSource
                                .getEmployeeImageUrl(employeeId)
                                .map {
                                    employeeImageUriString -> EmployeeUiModel.from(room, employeeImageUriString)
                                }
                                .toObservable()
                    }.toList()
}