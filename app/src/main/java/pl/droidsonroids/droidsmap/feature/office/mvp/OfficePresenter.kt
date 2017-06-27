package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary

class OfficePresenter private constructor(val view: OfficeMvpView) {

    private val officeFeatureBoundary = OfficeFeatureBoundary.Factory().create()

    fun onViewInitialized() {
        officeFeatureBoundary.requestOffice(object : OfficeFeatureBoundary.Gateway {
            override fun onOfficeEntityAvailable(entity: OfficeEntity) {
                updateUi()
            }
        })
    }

    private fun updateUi() {
        TODO("NYI")
    }

    class Factory {
        fun create(view: OfficeMvpView) = OfficePresenter(view)
    }
}