package pl.droidsonroids.droidsmap.base

abstract class BaseFeatureView<P : Presenter> {
    lateinit var presenter: P
}
