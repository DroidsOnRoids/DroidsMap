package pl.droidsonroids.droidsmap.presentation_logic

import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeMvpView
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficePresenter
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomUiModel
import pl.droidsonroids.droidsmap.model.Coordinates


class OfficePresenterTest {

    lateinit var officeView: OfficeMvpView<OfficeUiModel>
    lateinit var officeBoundary: OfficeFeatureBoundary
    lateinit var presenter: OfficePresenter
    @get:Rule val softly = JUnitSoftAssertions()

    @Before
    fun setUp() {
        officeView = mock()
        officeBoundary = mock()
        presenter = OfficePresenter.create(officeView, officeBoundary)
    }

    @Test
    fun `should show map once data is provided`() {
        val officeUiModel = OfficeUiModel.from(OfficeEntity())
        whenever(officeBoundary.requestOffice(any())).thenAnswer {
            (it.arguments[0] as OfficePresenter.OfficeDataObserver).onNext(officeUiModel)
        }

        presenter.onRequestOffice()

        with(officeUiModel) {
            inOrder(officeView) {
                verify(officeView).setMapPanningConstraints(this@with)
                verify(officeView).focusMapOnOfficeLocation(this@with)
            }
        }
    }

    @Test
    fun `use case is informed once app perspective changes from office to particular room`() {
        presenter.onRoomClicked(Coordinates(0.0, 0.0))

        verify(officeBoundary).changeToRoomPerspective()
    }

    @Test
    fun `view animates camera to clicked room`() {
        presenter.onRoomClicked(Coordinates(0.0, 0.0))

        verify(officeView).animateCameraToClickedRoom(any())
    }

    @Test
    fun `view animates the camera using the exact coordinates taken from map ground overlay`() {
        val coordinates = Coordinates(51.002345, 17.289457)
        presenter.onRoomClicked(coordinates)

        val captor = argumentCaptor<Coordinates>()
        verify(officeView).animateCameraToClickedRoom(captor.capture())
        softly.assertThat(captor.firstValue).isEqualTo(coordinates)
    }

    @Test
    fun `rooms list is passed after office UI model is captured`() {
        val officeUiModel = OfficeUiModel.from(OfficeEntity(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0))
        whenever(officeBoundary.requestOffice(any())).thenAnswer {
            (it.arguments[0] as OfficePresenter.OfficeDataObserver).onNext(officeUiModel)
        }

        val roomUiModel = RoomUiModel(0, 0, 0.0, 0.0, "")
        whenever(officeBoundary.requestRooms(any())).thenAnswer {
            (it.arguments[0] as DataObserverAdapter<Collection<RoomUiModel>>).onNext(listOf(roomUiModel))
        }

        presenter.onRequestRooms()

        verify(officeView, never()).displayOfficeRooms(any(), any())

        presenter.onRequestOffice()

        val officeCaptor = argumentCaptor<OfficeUiModel>()
        val roomsCaptor = argumentCaptor<Collection<RoomUiModel>>()

        verify(officeView).displayOfficeRooms(officeCaptor.capture(), roomsCaptor.capture())
        softly.assertThat(roomsCaptor.firstValue).hasSize(1)
        softly.assertThat(roomsCaptor.firstValue.elementAt(0)).isEqualTo(roomUiModel)
        softly.assertThat(officeCaptor.firstValue).isEqualTo(officeUiModel)
    }

    @Test
    fun `view prepares for room transition once map camera animation has completed`() {
        presenter.onMapCameraAnimationCompleted()

        verify(officeView).prepareForRoomTransition()
    }
}
