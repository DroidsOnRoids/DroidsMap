package pl.droidsonroids.droidsmap.presentation_logic

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomFeatureBoundary
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomMvpView
import pl.droidsonroids.droidsmap.feature.room.mvp.RoomPresenter

class RoomPresenterTest {

    lateinit var roomViewMock: RoomMvpView
    lateinit var roomBoundaryMock: RoomFeatureBoundary
    lateinit var presenter: RoomPresenter

    @Before
    fun setUp() {
        roomViewMock = mock()
        roomBoundaryMock = mock()
        presenter = RoomPresenter.create(roomBoundaryMock)
    }

    @Test
    fun `room presenter makes the view perform transition back to office`() {
        presenter.onPerspectiveLost()

        verify(roomViewMock).performOfficeTransition()
    }
}