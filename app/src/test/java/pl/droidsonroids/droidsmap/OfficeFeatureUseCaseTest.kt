package pl.droidsonroids.droidsmap

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureUseCase
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureBoundary
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureUseCase

class OfficeFeatureUseCaseTest {

    lateinit var useCase: OfficeFeatureUseCase
    lateinit var roomBoundary: RoomFeatureBoundary
    lateinit var repository: OfficeRepository

    @Before
    fun setUp() {
        roomBoundary = mock<RoomFeatureUseCase>()
        repository = mock<OfficeRepository>()
        useCase = OfficeFeatureBoundary.create(roomBoundary, repository)
    }

    @Test
    fun `use case notifies room boundary that it's current app perspective`() {
        useCase.changeToRoomPerspective()

        verify(roomBoundary).onRoomPerspectiveGained()
    }
}