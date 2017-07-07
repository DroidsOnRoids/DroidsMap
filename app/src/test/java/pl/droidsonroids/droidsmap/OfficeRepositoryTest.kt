package pl.droidsonroids.droidsmap

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.droidsmap.feature.office.api.OfficeDataEndpoint
import pl.droidsonroids.droidsmap.feature.office.api.OfficeDataInteractor
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository

class OfficeRepositoryTest {

    lateinit var repository: OfficeRepository
    lateinit var dataSource: OfficeDataEndpoint

    val officeEntity = OfficeEntity(51.0, 17.0, 51.0, 17.0, 200.0, 290.0)
    val mappedUiModel = OfficeUiModel.from(officeEntity)
    val dataEmitter: Single<OfficeEntity> = Single.create<OfficeEntity> { it.onSuccess(officeEntity) }

    @Before
    fun setUp() {
        dataSource = mock<OfficeDataInteractor>()
        repository = OfficeRepository(dataSource)
    }

    @Test
    fun `queried repository returns correctly mapped ui model`() {
        whenever(dataSource.getOfficeData()).thenReturn(dataEmitter)

        val testSubscriber = repository.query().test()
        testSubscriber.assertNoTimeout()
        testSubscriber.assertNoErrors()
        testSubscriber.assertResult(mappedUiModel)
        testSubscriber.assertComplete()
    }
}