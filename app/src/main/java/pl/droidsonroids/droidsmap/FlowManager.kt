package pl.droidsonroids.droidsmap

import pl.droidsonroids.droidsmap.base.UiGateway
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass

class FlowManager(
        private val terminateCallback: TerminationCallback,
        vararg views: UiGateway
) : FlowNavigator {

    val perspectiveViewsMap: MutableMap<KClass<out UiGateway>, UiGateway> = HashMap()
    val perspectiveStack: MutableList<KClass<out UiGateway>> = LinkedList()

    init {
        views.forEach {
            perspectiveViewsMap.put(it.javaClass.kotlin, it)
            it.registerFlowNavigator(this)
        }
        changePerspective(views.first().javaClass.kotlin)
    }

    fun onBackButtonPressed() {
        if (perspectiveStack.isNotEmpty()) {
            getView(perspectiveStack.last()).onPerspectiveChanged(false)
            perspectiveStack.removeAt(perspectiveStack.size - 1)

            if (perspectiveStack.isNotEmpty()) {
                getView(perspectiveStack.last()).onPerspectiveChanged(true)
            } else {
                terminateCallback.onAppTerminate()
            }
        }
    }

    override fun changePerspective(perspective: KClass<out UiGateway>) {
        if (perspectiveStack.isNotEmpty()) {
            if (perspective == perspectiveStack.last()) {
                return
            }

            val currentView = getView(perspectiveStack.last())
            currentView.onPerspectiveChanged(false)
        }

        perspectiveStack += perspective

        val newView = getView(perspective)
        newView.onPerspectiveChanged(true)
    }

    private fun getView(clazz: KClass<out UiGateway>): UiGateway {
        return perspectiveViewsMap[clazz] as UiGateway
    }
}

interface FlowNavigator {
    fun changePerspective(perspective: KClass<out UiGateway>)
}
