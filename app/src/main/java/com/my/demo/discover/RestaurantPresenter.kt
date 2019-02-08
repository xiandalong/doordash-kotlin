package com.my.demo.discover

import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import com.my.demo.network.DoorDashApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class RestaurantPresenter(private val view: RestaurantListView, private val doorDashApi: DoorDashApi) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val publishProcessor: BehaviorSubject<Int> = BehaviorSubject.createDefault(0)

    fun onCreate() {
        val disposable = publishProcessor
            .flatMapSingle {
                doorDashApi.getRestaurantsByLocation("37.422740", "-122.139956", it.toString(), "20")
                    .subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ restaurants ->
                view.addRestaurants(restaurants)
            }, { view.onErrorLoadingRestaurants(it) })
        compositeDisposable.add(disposable)
    }

    fun onDestroy() {
        compositeDisposable.clear()
    }

    fun onLoadMore(page: Int) {
        publishProcessor.onNext(page)
    }

}

data class Restaurant(@SerializedName("name") val name: String?,
                      @SerializedName("description") val description: String?,
                      @SerializedName("cover_img_url") val coverImageUrl: String?,
                      @SerializedName("status") val status: String?,
                      @SerializedName("status_type") val statusType: String?){
    fun getShortStatus():String?{
        if(statusType=="pre-order"){
            return "pre-order"
        }
        return status
    }
}
