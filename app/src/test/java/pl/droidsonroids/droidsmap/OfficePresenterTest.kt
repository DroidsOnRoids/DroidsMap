package pl.droidsonroids.droidsmap

import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeMvpView
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficePresenter
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel
import pl.droidsonroids.droidsmap.model.Coordinates


class OfficePresenterTest {

    lateinit var officeView: OfficeMvpView<OfficeUiModel>
    lateinit var officeBoundary : OfficeFeatureBoundary
    lateinit var presenter: OfficePresenter
    @get:Rule val softly = JUnitSoftAssertions()

    @Before
    fun setUp() {
        officeView = mock<OfficeMvpView<OfficeUiModel>>()
        officeBoundary = mock<OfficeFeatureBoundary>()
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
                verify(officeView).displayOfficeRooms(this@with)
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
}
