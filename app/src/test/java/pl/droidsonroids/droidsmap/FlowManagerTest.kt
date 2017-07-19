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
    lateinit var OfficeGatewayMock: OfficeUiGateway
    lateinit var flowManager: FlowManager

    @Before
    fun setUp() {
        roomGatewayMock = mock()
        OfficeGatewayMock = mock()
        flowManager = FlowManager(roomFeatureGateway = roomGatewayMock, officeFeatureGateway = OfficeGatewayMock)
    }

    @Test
    fun `view features get notified about incoming room perspective change`() {

        flowManager.currentPerspective = Perspective.OFFICE
        flowManager.onBackButtonPressed()

        verify(roomGatewayMock).onPerspectiveChanged(true)
        verify(OfficeGatewayMock).onPerspectiveChanged(false)

        verify(roomGatewayMock, never()).onPerspectiveChanged(false)
        verify(OfficeGatewayMock, never()).onPerspectiveChanged(true)
    }

    @Test
    fun `view features get notified about incoming office perspective change`() {

        flowManager.currentPerspective = Perspective.ROOM
        flowManager.onBackButtonPressed()

        verify(roomGatewayMock).onPerspectiveChanged(false)
        verify(OfficeGatewayMock).onPerspectiveChanged(true)

        verify(roomGatewayMock, never()).onPerspectiveChanged(true)
        verify(OfficeGatewayMock, never()).onPerspectiveChanged(false)
    }
}