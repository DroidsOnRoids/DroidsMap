package pl.droidsonroids.droidsmap

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiGateway
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiGateway

class FlowManagerTest {

    lateinit var roomGatewayMock: RoomUiGateway
    lateinit var officeGatewayMock: OfficeUiGateway
    lateinit var terminateListener: Terminatable
    lateinit var flowManager: FlowManager

    @Before
    fun setUp() {
        roomGatewayMock = mock()
        officeGatewayMock = mock()
        terminateListener = mock()
        flowManager = FlowManager(officeGatewayMock, roomGatewayMock, terminateListener)
    }

    @Test
    fun `view features get notified about incoming room perspective change`() {

        flowManager.currentPerspective = Perspective.OFFICE
        flowManager.onBackButtonPressed()

        verify(officeGatewayMock).onPerspectiveChanged(false)

        verify(roomGatewayMock, never()).onPerspectiveChanged(false)
        verify(roomGatewayMock, never()).onPerspectiveChanged(true)
        verify(officeGatewayMock, never()).onPerspectiveChanged(true)
    }

    @Test
    fun `view features get notified about incoming office perspective change`() {

        flowManager.currentPerspective = Perspective.ROOM
        flowManager.onBackButtonPressed()

        verify(roomGatewayMock).onPerspectiveChanged(false)
        verify(officeGatewayMock).onPerspectiveChanged(true)

        verify(roomGatewayMock, never()).onPerspectiveChanged(true)
        verify(officeGatewayMock, never()).onPerspectiveChanged(false)
    }

    @Test
    fun `application gets terminated upon back button press if in office perspective`() {
        flowManager.currentPerspective = Perspective.OFFICE
        flowManager.onBackButtonPressed()

        verify(terminateListener).onTerminate()
    }
}