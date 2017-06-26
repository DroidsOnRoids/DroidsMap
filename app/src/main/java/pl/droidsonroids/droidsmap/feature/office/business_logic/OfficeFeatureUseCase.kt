package pl.droidsonroids.droidsmap.feature.office.business_logic

import pl.droidsonroids.droidsmap.feature.office.repository.OfficeRepository

class OfficeFeatureUseCase : IOfficeFeatureBoundary {

    val officeRepository: OfficeRepository = OfficeRepository()

    override fun requestOffice(gateway: IOfficeFeatureBoundary.Gateway) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setOfficeCenterLocation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
