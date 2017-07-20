package pl.droidsonroids.droidsmap.business_logic

import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureUseCase
import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureBoundary
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureUseCase

class OfficeFeatureUseCaseTest {

    lateinit var useCase: OfficeFeatureUseCase
    lateinit var roomBoundary: RoomFeatureBoundary
    lateinit var officeRepository: OfficeRepository

    @Before
    fun setUp() {
        roomBoundary = mock<RoomFeatureUseCase>()
        officeRepository = mock<OfficeRepository>()
        useCase = OfficeFeatureBoundary.create(roomBoundary, officeRepository)
    }
}