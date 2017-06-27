package pl.droidsonroids.droidsmap.feature.office.ui

import pl.droidsonroids.droidsmap.MapActivity
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeMvpView
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficePresenter

class OfficeUiFeatureView(rootView: MapActivity) : OfficeMvpView {
    private val presenter = OfficePresenter.Factory().create(this)
    private val viewBinder = OfficeUiViewBinder(rootView)

    fun requestOffice() = presenter.onViewInitialized()
}