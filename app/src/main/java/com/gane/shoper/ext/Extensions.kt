package com.gane.shoper.ext

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 */
fun ImageView.loadUrl(url: String) {

}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener (object : ViewTreeObserver.OnGlobalLayoutListener{
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                if (Build.VERSION.SDK_INT >= 16) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                f()
            }
        }
    })
}

fun Context.token() = getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE)
        .getString("TOKEN", null)


fun Context.token(token: String?) {
    getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE).edit().putString("TOKEN", token).commit()
}

fun Context.name() = getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE)
        .getString("LOGIN_NAME", null)

fun Context.name(name: String?) {
    getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE).edit().putString("LOGIN_NAME", name).commit()
}

fun Context.pass() = getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE)
        .getString("LOGIN_PASS", null)

fun Context.pass(pass: String?) {
    getSharedPreferences("_shoper_prefs", Context.MODE_PRIVATE).edit().putString("LOGIN_PASS", pass).commit()
}

inline fun Observable<Any>.io_main() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())



