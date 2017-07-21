package pl.droidsonroids.droidsmap.base

import pl.droidsonroids.droidsmap.FlowNavigator

interface UiGateway {
    fun onPerspectiveChanged(active: Boolean)
    fun registerFlowNavigator(flowNavigator: FlowNavigator)
}