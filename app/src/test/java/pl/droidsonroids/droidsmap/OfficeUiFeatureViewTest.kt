package pl.droidsonroids.droidsmap

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.droidsmap.base.MapActivityWrapper
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficePresenterContract
import pl.droidsonroids.droidsmap.feature.office.ui.OfficeUiFeatureView

class OfficeUiFeatureViewTest {

    lateinit var activityWrapperMock: MapActivityWrapper
    lateinit var uiFeature: OfficeUiFeatureView
    lateinit var presenterContractMock: OfficePresenterContract

    @Before
    fun setUp() {
        activityWrapperMock = mock()
        presenterContractMock = mock()
        uiFeature = OfficeUiFeatureView(activityWrapperMock, presenterContractMock)
    }

    @Test
    fun `view registers flow change callback`() {
        val flowCallback = mock<FlowNavigator>()
        uiFeature.registerFlowNavigator(flowCallback)

        verify(presenterContractMock).registerFlowNavigator(flowCallback)
    }

    @Test
    fun `view attaches itself to presenter`() {
        uiFeature.onPerspectiveGained()

        verify(presenterContractMock).attachView(uiFeature)
    }
}