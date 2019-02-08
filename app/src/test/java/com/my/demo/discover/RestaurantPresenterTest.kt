package com.my.demo.discover

import com.my.demo.network.DoorDashApi
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class RestaurantPresenterTest {

    @Mock
    private lateinit var mockView:RestaurantListView

    @Mock
    private lateinit var mockApi: DoorDashApi

    private lateinit var presenter: RestaurantPresenter

    private val restaurants = listOf(Restaurant("KFC"), Restaurant("Burger King"))

    private val testScheduler = TestScheduler()

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> testScheduler }
//        RxAndroidPlugins.setMainThreadSchedulerHandler { scheduler -> testScheduler }
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
//        RxJavaPlugins.setComputationSchedulerHandler { s -> testScheduler }
        presenter = RestaurantPresenter(mockView, mockApi)
        Mockito.`when`(mockApi.getRestaurantsByLocation(anyString(),anyString(),anyString(),anyString())).thenReturn(Single.just(restaurants))
    }

    @Test
    fun onCreate() {
        presenter.onCreate()
        testScheduler.triggerActions()
        verify(mockView).addRestaurants(restaurants)
    }
}
