package pl.droidsonroids.droidsmap.feature.worker.business_logic

import pl.droidsonroids.droidsmap.model.Entity


data class WorkerEntity (
        val name : String = "",
        val surname : String = "",
        val photoUrl : String = "",
        val position : String = ""
) : Entity()