package pl.droidsonroids.droidsmap.feature.office.business_logic

interface OfficeFeatureBoundary {

    fun requestOffice(gateway: Gateway)

    fun setOfficeCenterLocation()

    interface Gateway {
        fun onOfficeEntityAvailable(entity: OfficeEntity)
    }

    companion object {
        fun create() = OfficeFeatureUseCase()
    }
}