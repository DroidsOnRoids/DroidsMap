package pl.droidsonroids.droidsmap.data

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.Single
import org.assertj.core.api.Assertions
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.droidsmap.SchedulersTestRule
import pl.droidsonroids.droidsmap.feature.office.api.OfficeDataEndpoint
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository
import pl.droidsonroids.droidsmap.feature.room.api.RoomDataEndpoint
import pl.droidsonroids.droidsmap.feature.room.api.RoomImagesEndpoint
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntity
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntityHolder
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomUiModel

class OfficeRepositoryTest {

    @get:Rule val softly = JUnitSoftAssertions()
    @get:Rule val testScheduler = SchedulersTestRule()

    lateinit var repository: OfficeRepository
    lateinit var officeDataEndpoint: OfficeDataEndpoint
    lateinit var roomDataEndpoint: RoomDataEndpoint
    lateinit var roomImageEndpoint: RoomImagesEndpoint

    val officeEntity = OfficeEntity(51.0, 17.0, 51.0, 17.0, 200.0, 290.0)
    val roomFirst = RoomEntity(0, 0, 0.0, 0.0)
    val roomSecond = RoomEntity(1, 1, 1.0, 1.0)
    val roomFirstId = "room_first"
    val roomSecondId = "room_second"
    val roomFirstImageUrl = "http://image.first.com"
    val roomSecondImageUrl = "http://image.second.com"

    lateinit var mappedUiModel: OfficeUiModel
    val dataEmitter: Single<OfficeEntity> = Single.create<OfficeEntity> { it.onSuccess(officeEntity) }

    @Before
    fun setUp() {
        val mockedEmployeesList = listOf(
                RoomEntityHolder(roomFirst, roomFirstId),
                RoomEntityHolder(roomSecond, roomSecondId)
        )

        val mockedImageUrls = mapOf(
                Pair(roomFirstId, roomFirstImageUrl),
                Pair(roomSecondId, roomSecondImageUrl)
        )
        officeDataEndpoint = mock {
            on { getOfficeData() } doReturn Single.just(officeEntity)
        }
        roomDataEndpoint = mock {
            on { getRoomData() } doReturn
                    Observable.just(mockedEmployeesList).flatMapIterable { list -> list }
        }
        roomImageEndpoint = mock()
        whenever(roomImageEndpoint.getRoomImageUrl(any()))
                .thenAnswer { return@thenAnswer Single.just(mockedImageUrls[it.arguments[0]]) }
        repository = OfficeRepository(officeDataEndpoint, roomDataEndpoint, roomImageEndpoint)

        mappedUiModel = OfficeUiModel.from(officeEntity,
                listOf(RoomUiModel.from(roomFirst, roomFirstImageUrl), RoomUiModel.from(roomSecond, roomSecondImageUrl)))
    }

    @Test
    fun `queried repository returns correctly mapped ui model`() {
        whenever(officeDataEndpoint.getOfficeData()).thenReturn(dataEmitter)

        val testSubscriber = repository.query().test()
        testSubscriber.assertNoTimeout()
        testSubscriber.assertNoErrors()
        testSubscriber.assertResult(mappedUiModel)
        testSubscriber.assertComplete()
    }

    @Test
    fun `repository merges data from various endpoints`() {
        val officeUiModel = repository.query().blockingGet()
        Assertions.assertThat(officeUiModel).isEqualTo(mappedUiModel)
    }
}