package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.base.DataObserverAdapter
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeFeatureBoundary

class OfficePresenter private constructor(
        private val view: OfficeMvpView,
        private val officeFeatureBoundary : OfficeFeatureBoundary) {

    fun onRequestOffice() = officeFeatureBoundary.requestOffice(OfficeDataObserver())

    private fun updateUi(uiModel: OfficeUiModel) {
        view.setMapPanningConstraints(uiModel)
        view.focusMapOnOfficeLocation(uiModel)
        view.displayOfficeRooms(uiModel)
    }

    companion object {
        fun create(view: OfficeMvpView, officeFeatureBoundary: OfficeFeatureBoundary)
                = OfficePresenter(view, officeFeatureBoundary)
    }

    inner class OfficeDataObserver : DataObserverAdapter<OfficeEntity>() {
        override fun onNext(entity: OfficeEntity) {
            updateUi(OfficeUiModel.from(entity))
        }

        override fun onError(e: Throwable) {
            TODO("NYI") // implement data fetch error
        }
    }
}