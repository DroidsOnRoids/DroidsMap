package pl.droidsonroids.droidsmap.base

import io.reactivex.disposables.Disposable

class DisposableHandler {
    val disposablesList = mutableListOf<Disposable>()

    infix fun handle(disposable: Disposable) {
        disposablesList += disposable
    }

    fun dispose() = disposablesList.forEach { it.dispose() }
}