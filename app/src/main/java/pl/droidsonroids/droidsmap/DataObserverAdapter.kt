package pl.droidsonroids.droidsmap

import io.reactivex.observers.DisposableObserver

open class DataObserverAdapter<T> : DisposableObserver<T>() {
    override fun onNext(entity: T) {
        //no-op by default
    }

    override fun onError(e: Throwable) {
        //no-op by default
    }

    override fun onComplete() {
        //no-op by default
    }
}