package pl.droidsonroids.droidsmap.office.business_logic

import android.support.annotation.NonNull

interface IOfficeFeatureBoundary {


    interface Gateway {
        fun onOfficeEntityAvailable(entity: OfficeEntity)
    }

    class Factory {
        @NonNull
        fun create() = OfficeFeatureUseCase()
    }
}