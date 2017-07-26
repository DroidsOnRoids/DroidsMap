package pl.droidsonroids.droidsmap

import com.nhaarman.mockito_kotlin.atLeast
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
    lateinit var terminateCallbackMock: TerminationCallback
    lateinit var flowManager: FlowManager

    @Before
    fun setUp() {
        roomGatewayMock = mock()
        officeGatewayMock = mock()
        terminateCallbackMock = mock()
        flowManager = FlowManager(terminateCallbackMock, officeGatewayMock, roomGatewayMock)
    }

    @Test
    fun `view features get notified about incoming room perspective change after back button press`() {

        flowManager.changePerspective(officeGatewayMock::class)
        flowManager.onBackButtonPressed()

        verify(officeGatewayMock).onPerspectiveLost()

        verify(roomGatewayMock, never()).onPerspectiveLost()
        verify(roomGatewayMock, never()).onPerspectiveGained()
    }

    @Test
    fun `view features get notified about incoming office perspective change after back button press`() {
        flowManager.changePerspective(roomGatewayMock::class)
        flowManager.onBackButtonPressed()

        verify(roomGatewayMock, atLeast(1)).onPerspectiveLost()
        verify(officeGatewayMock, atLeast(1)).onPerspectiveGained()
    }

    @Test
    fun `application gets terminated upon back button press if in office perspective`() {
        flowManager.changePerspective(officeGatewayMock::class)
        flowManager.onBackButtonPressed()

        verify(terminateCallbackMock).onAppTerminate()
    }

    @Test
    fun `flow manager launches first view upon initialization`() {
        verify(officeGatewayMock).onPerspectiveGained()
    }
}