package pl.droidsonroids.droidsmap.base

import io.reactivex.observers.DisposableObserver

open class DataObserverAdapter<T> : DisposableObserver<T>() {
    override fun onNext(model: T) = Unit

    override fun onError(e: Throwable) = Unit

    override fun onComplete() = Unit
}