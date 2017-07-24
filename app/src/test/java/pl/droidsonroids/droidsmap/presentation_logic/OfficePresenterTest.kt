package pl.droidsonroids.droidsmap.presentation_logic

import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.droidsmap.FlowNavigator
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeMvpView
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficePresenter
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomUiModel
import pl.droidsonroids.droidsmap.feature.room.ui.RoomUiFeatureView
import pl.droidsonroids.droidsmap.model.Coordinates


class OfficePresenterTest {

    lateinit var officeViewMock: OfficeMvpView
    lateinit var officeBoundaryMock: OfficeFeatureBoundary
    lateinit var presenter: OfficePresenter
    @get:Rule val softly = JUnitSoftAssertions()

    @Before
    fun setUp() {
        officeViewMock = mock()
        officeBoundaryMock = mock()
        presenter = OfficePresenter.create(officeBoundaryMock)
        presenter.attachView(officeViewMock)
    }

    @Test
    fun `should show map once data is provided`() {
        val officeUiModel = OfficeUiModel.from(OfficeEntity(), emptyList())
        whenever(officeBoundaryMock.requestOffice(any())).thenAnswer {
            (it.arguments[0] as OfficePresenter.OfficeDataObserver).onNext(officeUiModel)
        }

        presenter.onRequestOffice()

        with(officeUiModel) {
            inOrder(officeViewMock) {
                verify(officeViewMock).setMapPanningConstraints(this@with)
                verify(officeViewMock).focusMapOnOfficeLocation(this@with)
            }
        }
    }

    @Test
    fun `view animates camera to clicked room`() {
        presenter.onRoomClicked(Coordinates(0.0, 0.0))

        verify(officeViewMock).animateCameraToClickedRoom(any())
    }

    @Test
    fun `view animates the camera using the exact coordinates taken from map ground overlay`() {
        val coordinates = Coordinates(51.002345, 17.289457)
        presenter.onRoomClicked(coordinates)

        val captor = argumentCaptor<Coordinates>()
        verify(officeViewMock).animateCameraToClickedRoom(captor.capture())
        softly.assertThat(captor.firstValue).isEqualTo(coordinates)
    }

    @Test
    fun `rooms list is passed after office UI model is captured`() {
        val roomUiModel = RoomUiModel(0, 0, 0.0, 0.0, "")
        val officeUiModel
                = OfficeUiModel.from(OfficeEntity(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0), listOf(roomUiModel))
        whenever(officeBoundaryMock.requestOffice(any())).thenAnswer {
            (it.arguments[0] as OfficePresenter.OfficeDataObserver).onNext(officeUiModel)
        }

        presenter.onRequestOffice()

        val officeCaptor = argumentCaptor<OfficeUiModel>()

        verify(officeViewMock).displayOfficeRooms(officeCaptor.capture())
        softly.assertThat(officeCaptor.firstValue.roomUiModels).hasSize(1)
        softly.assertThat(officeCaptor.firstValue.roomUiModels.elementAt(0)).isEqualTo(roomUiModel)
        softly.assertThat(officeCaptor.firstValue).isEqualTo(officeUiModel)
    }

    @Test
    fun `view performs room transition once map camera animation has completed`() {
        val flowNavigatorMock = mock<FlowNavigator>()

        presenter.registerFlowNavigator(flowNavigatorMock)
        presenter.onMapCameraAnimationCompleted()

        inOrder(officeViewMock, flowNavigatorMock) {
            verify(officeViewMock).prepareForRoomTransition()
            verify(officeViewMock).performRoomTransition()
            verify(flowNavigatorMock).changePerspective(RoomUiFeatureView::class)
        }
    }

    @Test
    fun `presenter initalizes office map upon view attach`() {
        verify(officeViewMock).initMap()
    }
}
