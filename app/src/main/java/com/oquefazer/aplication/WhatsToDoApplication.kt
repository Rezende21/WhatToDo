package com.oquefazer.aplication

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.oquefazer.notification.SetNotification
import dagger.hilt.android.HiltAndroidApp
import java.util.Date

private const val LOG_TAG = "AppOpenAdManager"
private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294"

@HiltAndroidApp
class WhatsToDoApplication : Application(), Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private lateinit var appOprenAdManager: AppOprenAdManager
    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        registerActivityLifecycleCallbacks(this)
        MobileAds.initialize(this){}
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOprenAdManager = AppOprenAdManager()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            SetNotification.channelId,
            "Remember",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Used to remember what to do Notification"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        currentActivity?.let { appOprenAdManager.showAdIfAcalable(it)}
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {}

    override fun onActivityStarted(p0: Activity) {
        if (!appOprenAdManager.isShowingAd) {
            currentActivity = p0
        }
    }

    override fun onActivityResumed(p0: Activity) {}

    override fun onActivityPaused(p0: Activity) {}

    override fun onActivityStopped(p0: Activity) {}

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

    override fun onActivityDestroyed(p0: Activity) {}

    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        appOprenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    private inner class AppOprenAdManager {
        private var loadTime : Long = 0
        private  var appOpenAd: AppOpenAd? = null
        private var isLoading = false
        var isShowingAd = false

        fun loadAd(context : Context) {
            if (isLoading || isAdvailable()) {
                return
            }

            isLoading = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                object : AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        super.onAdLoaded(ad)
                        //Log.i(LOG_TAG, "onAdLoaded")
                        appOpenAd = ad
                        isLoading = false
                        loadTime = Date().time
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        //Log.i(LOG_TAG, "failed")
                        isLoading = false
                    }

                }
            )
        }

        private fun wasLoadTimeLessThanHorsAgo(numHours : Long) : Boolean {
            val dateDifference : Long = Date().time - loadTime
            val numMilliSecondsPerHour : Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        private fun isAdvailable() : Boolean {
            return appOpenAd != null && wasLoadTimeLessThanHorsAgo(4)
        }

        fun showAdIfAcalable(activity: Activity) {
            showAdIfAvailable(activity,
                object : OnShowAdCompleteListener {
                    override fun onShowAdComplete() {

                    }

                }
            )
        }

        fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener : OnShowAdCompleteListener) {
            if (isShowingAd) {
                return
            }
            if (!isAdvailable()) {
                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity)
                return
            }

            appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    appOpenAd = null
                    isShowingAd = false
                    //Log.i(LOG_TAG, "onAdDismissedFullScreem")
                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd(activity)
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    super.onAdFailedToShowFullScreenContent(error)
                    appOpenAd = null
                    isShowingAd = false
                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd(activity)
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()

                }
            }
            isShowingAd = true
            appOpenAd!!.show(activity)
        }
    }
}
