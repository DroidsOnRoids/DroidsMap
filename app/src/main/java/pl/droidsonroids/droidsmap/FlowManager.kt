package pl.droidsonroids.droidsmap

import pl.droidsonroids.droidsmap.base.MvpView
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass

class FlowManager(
        private val terminateCallback: TerminationCallback,
        vararg views: MvpView
) : FlowNavigator {

    val perspectiveViewsMap: MutableMap<KClass<out MvpView>, MvpView> = HashMap()
    val perspectiveStack: MutableList<KClass<out MvpView>> = LinkedList()

    init {
        views.forEach { mvpView ->
            perspectiveViewsMap.put(mvpView.javaClass.kotlin, mvpView)
            mvpView.registerFlowNavigator(this)
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

    override fun changePerspective(newPerspective: KClass<out MvpView>) {
        if (perspectiveStack.isNotEmpty()) {
            if (newPerspective.equals(perspectiveStack.last())) {
                return
            }

            var currentView = getView(perspectiveStack.last())
            currentView.onPerspectiveChanged(false)
        }

        perspectiveStack += newPerspective

        var newView = getView(newPerspective)
        newView.onPerspectiveChanged(true)
    }

    private fun getView(clazz: KClass<out MvpView>): MvpView {
        return perspectiveViewsMap.get(clazz) as MvpView
    }
}

interface FlowNavigator {
    fun changePerspective(perspective: KClass<out MvpView>)
}
