package pl.droidsonroids.droidsmap

import io.reactivex.disposables.Disposable

class DisposableHandler {
    val disposablesList = mutableListOf<Disposable>()

    fun dispose() = disposablesList.forEach { it.dispose() }

    infix fun handle(disposable: Disposable) {
        disposablesList += disposable
    }
}