package dev.liinahamari.highlights.helper

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

interface RxSubscriptionsDelegate {
    fun <T : Any> Observable<T>.subscribeUi(doOnSubscribe: Consumer<T>): Disposable
    fun <T: Any> Observable<T>.subscribeUi(): Disposable

    fun <T: Any> Single<T>.subscribeUi(doOnSubscribe: Consumer<T>): Disposable
    fun <T: Any> Single<T>.subscribeUi(): Disposable

    fun <T: Any> Maybe<T>.subscribeUi(doOnSubscribe: Consumer<T>): Disposable
    fun <T: Any> Maybe<T>.subscribeUi(): Disposable

    fun <T: Any> Flowable<T>.subscribeUi(doOnSubscribe: Consumer<T>): Disposable
    fun <T: Any> Flowable<T>.subscribeUi(): Disposable

    fun Completable.subscribeUi(): Disposable

    /**
     * @param doOnComplete operates in main thread
     * */
    fun Completable.subscribeUi(doOnComplete: Action): Disposable

    fun <T: Any> Observable<T>.addToDisposable(doOnSubscribe: Consumer<T>): Disposable
    fun <T: Any> Observable<T>.addToDisposable(): Disposable

    fun <T: Any> Single<T>.addToDisposable(doOnSubscribe: Consumer<T>): Disposable
    fun <T: Any> Single<T>.addToDisposable(): Disposable

    fun <T: Any> Maybe<T>.addToDisposable(doOnSubscribe: Consumer<T>): Disposable
    fun <T: Any> Maybe<T>.addToDisposable(): Disposable

    fun <T: Any> Flowable<T>.addToDisposable(doOnSubscribe: Consumer<T>): Disposable
    fun <T: Any> Flowable<T>.addToDisposable(): Disposable

    fun Completable.addToDisposable(): Disposable

    /** Must be called manually on lifecycle 'terminate' event */
    fun disposeSubscriptions()
}

class RxSubscriptionDelegateImpl : RxSubscriptionsDelegate {
    private val compositeDisposable = CompositeDisposable()

    override fun <T: Any> Single<T>.subscribeUi(): Disposable = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe()
        .also(compositeDisposable::add)

    override fun <T: Any> Maybe<T>.subscribeUi(): Disposable = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe()
        .also(compositeDisposable::add)

    override fun <T: Any> Flowable<T>.subscribeUi(): Disposable = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe()
        .also(compositeDisposable::add)

    override fun <T: Any> Observable<T>.subscribeUi(): Disposable = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe()
        .also(compositeDisposable::add)

    override fun <T: Any> Observable<T>.subscribeUi(doOnSubscribe: Consumer<T>): Disposable = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(doOnSubscribe)
        .also(compositeDisposable::add)

    override fun <T: Any> Single<T>.subscribeUi(doOnSubscribe: Consumer<T>): Disposable = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(doOnSubscribe)
        .also(compositeDisposable::add)

    override fun <T: Any> Maybe<T>.subscribeUi(doOnSubscribe: Consumer<T>): Disposable = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(doOnSubscribe)
        .also(compositeDisposable::add)

    override fun <T: Any> Flowable<T>.subscribeUi(doOnSubscribe: Consumer<T>): Disposable = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(doOnSubscribe)
        .also(compositeDisposable::add)

    override fun Completable.subscribeUi(): Disposable = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe()
        .also(compositeDisposable::add)

    override fun Completable.subscribeUi(doOnComplete: Action): Disposable = subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnComplete(doOnComplete)
        .subscribe()
        .also(compositeDisposable::add)

    override fun <T: Any> Single<T>.addToDisposable(): Disposable = subscribe()
        .also(compositeDisposable::add)

    override fun <T: Any> Maybe<T>.addToDisposable(): Disposable = subscribe()
        .also(compositeDisposable::add)

    override fun <T: Any> Flowable<T>.addToDisposable(): Disposable = subscribe()
        .also(compositeDisposable::add)

    override fun <T: Any> Observable<T>.addToDisposable(): Disposable = subscribe()
        .also(compositeDisposable::add)

    override fun <T: Any> Observable<T>.addToDisposable(doOnSubscribe: Consumer<T>): Disposable = subscribe(doOnSubscribe)
        .also(compositeDisposable::add)

    override fun <T: Any> Single<T>.addToDisposable(doOnSubscribe: Consumer<T>): Disposable = subscribe(doOnSubscribe)
        .also(compositeDisposable::add)

    override fun <T: Any> Maybe<T>.addToDisposable(doOnSubscribe: Consumer<T>): Disposable = subscribe(doOnSubscribe)
        .also(compositeDisposable::add)

    override fun <T: Any> Flowable<T>.addToDisposable(doOnSubscribe: Consumer<T>): Disposable = subscribe(doOnSubscribe)
        .also(compositeDisposable::add)

    override fun Completable.addToDisposable(): Disposable = subscribe()
        .also(compositeDisposable::add)

    override fun disposeSubscriptions() = compositeDisposable.clear()
}
