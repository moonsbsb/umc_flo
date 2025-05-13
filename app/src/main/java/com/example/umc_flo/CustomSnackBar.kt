package com.example.umc_flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.umc_flo.databinding.CustomSnackbarBinding
import com.google.android.material.snackbar.Snackbar

class CustomSnackBar(view: View, private val message: String) {

    companion object{
        fun make(view: View, message: String) = CustomSnackBar(view, message)
    }
    private val context = view.context
    private val snackbar = Snackbar.make(view, "", 5000)
    @SuppressLint("RestrictedApi")
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val snackbarBinding: CustomSnackbarBinding
            = DataBindingUtil.inflate(inflater, R.layout.custom_snackbar, null, false)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackbarLayout) {
            removeAllViews()
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackbarBinding.root, 0)
        }
    }

    private fun initData() {
        snackbarBinding.snackBarTxt.text = message
        snackbarBinding.snackBarBtn.setOnClickListener {
            // OK 버튼을 클릭했을 때 실행할 동작을 정의할 수 있다.
        }
    }

    fun show() {
        snackbar.show()
    }
}