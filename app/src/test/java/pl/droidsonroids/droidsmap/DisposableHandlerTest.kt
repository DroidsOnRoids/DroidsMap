package pl.droidsonroids.droidsmap

import io.reactivex.disposables.Disposables
import org.junit.Before
import org.junit.Test
import pl.droidsonroids.droidsmap.base.DisposableHandler

class DisposableHandlerTest {

    lateinit var handler: DisposableHandler

    @Before
    fun setUp() {
        handler = DisposableHandler()
    }

    @Test
    fun `disposables are being added to maintained list`() {
        val iterations = 5
        (0..iterations).forEach { handler handle Disposables.empty() }

        assert(handler.disposablesList.size == iterations)
    }

    @Test
    fun `disposables are disposed by the handler`() {
        val iterations = 5
        (0..iterations).forEach { handler handle Disposables.empty() }

        handler.disposablesList.forEach { assert(!it.isDisposed) }

        handler.dispose()

        handler.disposablesList.forEach { assert(it.isDisposed) }
    }

    @Test
    fun `disposables are not deleted by the handler`() {
        val iterationsCount = 5
        (0..iterationsCount).forEach { handler handle Disposables.empty() }

        handler.dispose()

        assert(handler.disposablesList.size == iterationsCount)
    }
}