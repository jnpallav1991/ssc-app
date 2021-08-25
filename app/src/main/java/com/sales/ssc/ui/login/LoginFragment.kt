package com.sales.ssc.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sales.ssc.R
import com.sales.ssc.response.UserLogin
import com.sales.ssc.response.UserLoginResponse
import com.sales.ssc.utils.OnFragmentInteractionListener
import com.sales.ssc.utils.SharedPreferenceUtil
import com.sales.ssc.viewmodel.LoginViewModel
import com.sales.ssc.viewmodel.ProductsViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel =
            ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSignIn.setOnClickListener {

            if (checkCredential())
            {
                loginResponse(UserLogin(edUserName.text.toString().trim(),edPassword.text.toString().trim()))
            }
        }

    }

    private fun loginResponse(userLogin: UserLogin) {

        loginViewModel.getUserLogin(userLogin)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { responseSscBody ->
                try {
                    if (responseSscBody != null) {

                        val type = object : TypeToken<UserLoginResponse>() {}.type
                        val l = Gson().toJson(responseSscBody.Data)
                        val userLoginResponse = Gson().fromJson<UserLoginResponse>(l, type)
                        if (responseSscBody.Message == "SUCCESS")
                        {
                            SharedPreferenceUtil(context!!).setBoolean("IS_ADMIN",userLoginResponse.IsAdmin!!)
                            listener!!.signInSuccess()
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            })
    }


    private fun checkCredential():Boolean
    {
        if (edUserName.text.isNullOrEmpty())
        {
            return false
        }
        else if (edPassword.text.isNullOrEmpty())
        {
            return false
        }
        return true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

}
