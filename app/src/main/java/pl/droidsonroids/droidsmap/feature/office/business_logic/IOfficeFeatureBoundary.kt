package pl.droidsonroids.droidsmap.feature.office.business_logic

interface IOfficeFeatureBoundary {

    fun requestOffice(gateway: Gateway)

    fun setOfficeCenterLocation()

    interface Gateway {
        fun onOfficeEntityAvailable(entity: OfficeEntity)
    }

    class Factory {
        fun create() = OfficeFeatureUseCase()
    }
}