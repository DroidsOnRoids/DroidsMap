package pl.droidsonroids.droidsmap.business_logic

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureUseCase
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureBoundary
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureUseCase
import pl.droidsonroids.droidsmap.feature.room.repository.RoomRepository

class OfficeFeatureUseCaseTest {

    lateinit var useCase: OfficeFeatureUseCase
    lateinit var roomBoundary: RoomFeatureBoundary
    lateinit var officeRepository: OfficeRepository
    lateinit var roomRepository: RoomRepository

    @Before
    fun setUp() {
        roomBoundary = mock<RoomFeatureUseCase>()
        officeRepository = mock<OfficeRepository>()
        useCase = OfficeFeatureBoundary.create(roomBoundary, officeRepository, roomRepository)
    }

    @Test
    fun `use case notifies room boundary that it's current app perspective`() {
        useCase.changeToRoomPerspective()

        verify(roomBoundary).onRoomPerspectiveGained()
    }
}