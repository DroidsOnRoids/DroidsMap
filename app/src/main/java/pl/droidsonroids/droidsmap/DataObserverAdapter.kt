package pl.droidsonroids.droidsmap

import io.reactivex.observers.DisposableObserver

open class DataObserverAdapter<T> : DisposableObserver<T>() {
    override fun onNext(entity: T) = Unit

    override fun onError(e: Throwable) = Unit

    override fun onComplete() = Unit
}