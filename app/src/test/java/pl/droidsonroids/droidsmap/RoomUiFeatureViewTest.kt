package pl.droidsonroids.droidsmap

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.droidsmap.base.MapActivityWrapper
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomPresenterContract
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiFeatureView

class RoomUiFeatureViewTest {

    lateinit var activityWrapperMock: MapActivityWrapper
    lateinit var presenterContractMock: RoomPresenterContract
    lateinit var uiFeature: RoomUiFeatureView

    @Before
    fun setUp() {
        activityWrapperMock = mock()
        presenterContractMock = mock()
        uiFeature = RoomUiFeatureView(activityWrapperMock, presenterContractMock)
    }

    @Test
    fun `view notifies presenter about perspective loss`() {
        uiFeature.onPerspectiveLost()

        verify(presenterContractMock).onPerspectiveLost()
    }
}