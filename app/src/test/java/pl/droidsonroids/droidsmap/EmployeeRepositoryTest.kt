package pl.droidsonroids.droidsmap

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.Single
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.droidsmap.feature.employee.api.EmployeeDataEndpoint
import pl.droidsonroids.droidsmap.feature.employee.api.EmployeeImageEndpoint
import pl.droidsonroids.droidsmap.feature.employee.business_logic.EmployeeEntity
import pl.droidsonroids.droidsmap.feature.employee.business_logic.EmployeeEntityHolder
import pl.droidsonroids.droidsmap.feature.employee.mvp.EmployeeUiModel
import pl.droidsonroids.droidsmap.feature.employee.repository.EmployeeRepository

val TEST_EMPLOYEE_FIRST = EmployeeEntity("Jesica", "Kowalsky", "manager")
val TEST_EMPLOYEE_SECOND = EmployeeEntity("Brian", "Nowak", "programmer")

val TEST_EMPLOYEE_FIRST_ID = "employee_1"
val TEST_EMPLOYEE_SECOND_ID = "employee_2"

val TEST_EMPLOYEE_FIRST_IMAGE_URL = "http://example.com/employee_1.jpg"
val TEST_EMPLOYEE_SECOND_IMAGE_URL = "http://example.com/employee_2.jpg"

class EmployeeRepositoryTest {

    @get:Rule val softly = JUnitSoftAssertions()

    lateinit var employeeDataEndpointMock: EmployeeDataEndpoint
    val employeeImageEndpointMock: EmployeeImageEndpoint = mock()

    lateinit var employeeRepository: EmployeeRepository

    @Before
    fun setUp() {
        val mockedEmployeesList: List<EmployeeEntityHolder> = listOf(
                EmployeeEntityHolder(TEST_EMPLOYEE_FIRST, TEST_EMPLOYEE_FIRST_ID),
                EmployeeEntityHolder(TEST_EMPLOYEE_SECOND, TEST_EMPLOYEE_SECOND_ID)
        )

        val mockedImageUrls: Map<String, String> = mapOf(
                Pair(TEST_EMPLOYEE_FIRST_ID, TEST_EMPLOYEE_FIRST_IMAGE_URL),
                Pair(TEST_EMPLOYEE_SECOND_ID, TEST_EMPLOYEE_SECOND_IMAGE_URL)
        )

        employeeDataEndpointMock = mock<EmployeeDataEndpoint> {
            on { getAllEmployees() } doReturn
                    Observable.fromArray(mockedEmployeesList).flatMapIterable { list -> list }
        }


        whenever(employeeImageEndpointMock.getEmployeeImageUrl(any()))
                .thenAnswer { return@thenAnswer Single.just(mockedImageUrls.get(it.arguments[0])) }

        employeeRepository = EmployeeRepository(employeeDataEndpointMock, employeeImageEndpointMock)
    }

    @Test
    fun `emitted employees list has proper size`() {
        var employees = employeeRepository.query().blockingGet()
        softly.assertThat(employees).hasSize(2)
    }

    @Test
    fun `emitted employees data is correct`() {
        val employees = employeeRepository.query().blockingGet()
        val expectedFirstItem = EmployeeUiModel.from(TEST_EMPLOYEE_FIRST, TEST_EMPLOYEE_FIRST_IMAGE_URL)
        val expectedSecondItem = EmployeeUiModel.from(TEST_EMPLOYEE_SECOND, TEST_EMPLOYEE_SECOND_IMAGE_URL)

        softly.assertThat(employees[0]).isEqualTo(expectedFirstItem)
        softly.assertThat(employees[1]).isEqualTo(expectedSecondItem)
    }
}
