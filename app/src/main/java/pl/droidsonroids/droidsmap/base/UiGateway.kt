package pl.droidsonroids.droidsmap.base

import pl.droidsonroids.droidsmap.FlowNavigator

interface UiGateway {
    fun onPerspectiveGained()
    fun onPerspectiveLost()
    fun registerFlowNavigator(flowNavigator: FlowNavigator)
}