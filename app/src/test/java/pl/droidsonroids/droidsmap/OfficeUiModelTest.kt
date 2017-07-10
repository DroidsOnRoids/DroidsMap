package pl.droidsonroids.droidsmap

import org.junit.Before
import org.junit.Test
import pl.droidsonroids.droidsmap.feature.office.business_logic.OfficeEntity
import pl.droidsonroids.droidsmap.feature.office.mvp.OfficeUiModel

class OfficeUiModelTest {

    lateinit var officeEntity: OfficeEntity
    lateinit var correctlyMappedUiModel: OfficeUiModel
    lateinit var badlyMappedUiModel: OfficeUiModel

    @Before
    fun setUp() {
        officeEntity = OfficeEntity(1.0, 2.0, 3.0, 4.0)
        correctlyMappedUiModel = OfficeUiModel(1.0, 2.0, 3.0, 4.0, officeEntity.mapViewportBearing, officeEntity.translatedMapViewportBearing, officeEntity.mapViewportConstraint)
        badlyMappedUiModel = OfficeUiModel(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0)
    }

    @Test
    fun `correct ui models are created from entities`() {
        assert(correctlyMappedUiModel == OfficeUiModel.from(officeEntity))
        assert(badlyMappedUiModel != OfficeUiModel.from(officeEntity))
    }
}