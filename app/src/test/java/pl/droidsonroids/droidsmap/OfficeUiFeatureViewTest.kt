package pl.droidsonroids.droidsmap

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.droidsmap.base.MapActivityWrapper
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficePresenter
import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiFeatureView

class OfficeUiFeatureViewTest {

    lateinit var activityWrapperMock: MapActivityWrapper
    lateinit var uiFeature: OfficeUiFeatureView
    lateinit var presenterMock: OfficePresenter

    @Before
    fun setUp() {
        activityWrapperMock = mock()
        presenterMock = mock()
        uiFeature = OfficeUiFeatureView(activityWrapperMock, presenterMock)
    }

    @Test
    fun `view registers flow change callback`() {
        val flowCallback = mock<ForwardFlowChangeListener>()
        uiFeature.registerFlowChangeCallback(flowCallback)

        verify(presenterMock).onFlowCallbackRegistered(any())
    }
}