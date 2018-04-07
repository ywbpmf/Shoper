package com.gane.shoper.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.gane.shoper.R
import com.gane.shoper.app.ShoperApp
import com.gane.shoper.entity.UserLoginBean
import com.gane.shoper.ext.name
import com.gane.shoper.ext.pass
import com.gane.shoper.ext.token
import com.gane.shoper.ui.main.MainActivity
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.ui_login.*


/**
 *
 */
class LoginActivity: AppCompatActivity(), View.OnClickListener, LoginContract.View {

    override var presenter: LoginContract.Presenter = LoginPresenter(this)


    private var kProgress: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_login)

        initView()
    }

    private fun initView() {
        btn_login.setOnClickListener(this)

        if (!TextUtils.isEmpty(name()) && !TextUtils.isEmpty(pass())) {
            et_phone.setText(name())
            et_pass.requestFocus()
            presenter.doLogin(name(), pass())
        }
    }

    override fun onClick(v: View?) {
        val phone = et_phone.text.toString().trim()
        val pass = et_pass.text.toString().trim()

        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(pass)) {
            Toast.makeText(this, R.string.input_name_pass, Toast.LENGTH_SHORT).show()
            return
        }

        presenter.doLogin(phone, pass)
    }


    override fun showLoading() {
        if (kProgress == null)
            kProgress = KProgressHUD(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(false)
        if (!kProgress!!.isShowing) {
            kProgress!!.show()
        }
    }

    override fun loginFailed() {
        kProgress?.dismiss()
        Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show()
    }

    override fun loginSuccess(userLoginBean: UserLoginBean) {
        token(userLoginBean.token)
        name(et_phone.text.toString())
        pass(et_pass.text.toString())
        kProgress?.dismiss()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}