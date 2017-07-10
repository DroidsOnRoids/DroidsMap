package pl.droidsonroids.droidsmap

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeMvpView
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficePresenter
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel

class OfficePresenterTest {

    lateinit var officeView: OfficeMvpView<Any?>
    lateinit var officeBoundary : OfficeFeatureBoundary
    lateinit var presenter: OfficePresenter

    @Before
    fun setUp() {
        officeView = mock<OfficeMvpView<Any?>>()
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
        presenter.onRoomClicked()

        verify(officeBoundary).changeToRoomPerspective()
    }
}
