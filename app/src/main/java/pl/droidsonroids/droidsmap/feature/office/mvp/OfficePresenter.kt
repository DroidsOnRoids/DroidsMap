package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary

class OfficePresenter private constructor(private val view: OfficeMvpView) {

    private val officeFeatureBoundary = OfficeFeatureBoundary.create()

    fun onViewInitialized() = officeFeatureBoundary.requestOffice(object : OfficeFeatureBoundary.Gateway {
        override fun onOfficeEntityAvailable(entity: OfficeEntity) = updateUi(OfficeUiModel.from(entity))
    })

    private fun updateUi(uiModel: OfficeUiModel) = view.showMap(uiModel)

    companion object {
        fun create(view: OfficeMvpView) = OfficePresenter(view)
    }
}