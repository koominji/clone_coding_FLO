package com.example.flo.data.remote

import android.util.Log
import com.example.flo.LoginView
import com.example.flo.SignUpView
import retrofit2.Call
import com.example.flo.data.entities.User
import com.example.flo.getRetrofit
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun signUp(user: User) {

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.signUp(user).enqueue(object : retrofit2.Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SignUp/Success", response.toString())
                val resp: AuthResponse = response.body()!!
                when (resp.code) {
                    1000 -> signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure(resp.code)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SignUp/Failure", t.message.toString())
            }
        })
        Log.d("SignUp", "HELLO")
    }

    fun login(user: User) {

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.login(user).enqueue(object : retrofit2.Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("Login/Success", response.toString())
                val resp: AuthResponse = response.body()!!
                when (val code = resp.code) {
                    1000 -> loginView.onLoginSuccess(code,resp.result!!)
                    else -> loginView.onLoginFailure()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("Login/Failure", t.message.toString())
            }
        })
        Log.d("Login", "HELLO")
    }
}