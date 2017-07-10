package pl.droidsonroids.droidsmap

import io.reactivex.disposables.Disposables
import org.assertj.core.api.Assertions
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.droidsmap.base.DisposableHandler

class DisposableHandlerTest {

    @get:Rule val softly = JUnitSoftAssertions()
    lateinit var handler: DisposableHandler

    @Before
    fun setUp() {
        handler = DisposableHandler()
    }

    @Test
    fun `disposables are being added to maintained list`() {
        val iterationsCount = 4
        (0..iterationsCount - 1).forEach { handler handle Disposables.empty() }

        Assertions.assertThat(handler.disposablesList).hasSize(iterationsCount)
    }

    @Test
    fun `disposables are disposed by the handler`() {
        val iterationsCount = 5
        (0..iterationsCount - 1).forEach { handler handle Disposables.empty() }

        handler.disposablesList.forEach { softly.assertThat(it.isDisposed).isFalse }

        handler.dispose()

        handler.disposablesList.forEach { softly.assertThat(it.isDisposed).isTrue }
    }

    @Test
    fun `disposables are not deleted by the handler`() {
        val iterationsCount = 5
        (0..iterationsCount - 1).forEach { handler handle Disposables.empty() }

        handler.dispose()

        Assertions.assertThat(handler.disposablesList).hasSize(iterationsCount)
    }
}