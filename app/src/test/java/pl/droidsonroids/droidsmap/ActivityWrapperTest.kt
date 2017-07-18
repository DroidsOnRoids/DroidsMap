package pl.droidsonroids.droidsmap

import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiFeatureView
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiFeatureView

class ActivityWrapperTest {

    lateinit var roomViewMock: RoomUiFeatureView
    lateinit var officeViewMock: OfficeUiFeatureView

    @Before
    fun setUp() {
        roomViewMock = mock(RoomUiFeatureView::class.java)
        officeViewMock = mock(OfficeUiFeatureView::class.java)
    }

    @Test
    fun `room view gets notified about perspective change`() {
        val activityWrapper = ActivityWrapper(roomFeature = roomViewMock, officeFeature = officeViewMock)

        activityWrapper.notifyPerspectiveChanged(Perspective.ROOM)

        verify(roomViewMock).onPerspectiveChanged(Perspective.ROOM)
    }
}