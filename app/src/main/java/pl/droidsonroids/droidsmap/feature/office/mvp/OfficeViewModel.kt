package pl.droidsonroids.droidsmap.feature.office.mvp

import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity

class OfficeViewModel private constructor(centerLatitude: Float, centerLongitude: Float) {
    class Mapper {
        fun map(entity: OfficeEntity) = OfficeViewModel(entity.centerLatitude, entity.centerLongitude)
    }
}
