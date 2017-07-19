package pl.droidsonroids.droidsmap

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiGateway
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiGateway

class FlowManagerTest {

    lateinit var roomViewMock: RoomUiGateway
    lateinit var officeViewMock: OfficeUiGateway
    lateinit var flowManager: FlowManager

    @Before
    fun setUp() {
        roomViewMock = mock()
        officeViewMock = mock()
        flowManager = FlowManager(roomFeatureGateway = roomViewMock, officeFeatureGateway = officeViewMock)
    }

    @Test
    fun `room view gets notified about perspective change`() {

        flowManager.notifyPerspectiveChanged(Perspective.ROOM)

        verify(roomViewMock).onPerspectiveChanged(Perspective.ROOM)
    }
}