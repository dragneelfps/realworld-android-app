package com.nooblabs.conduit.ui


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.nooblabs.conduit.HomeActivity
import com.nooblabs.conduit.R
import com.nooblabs.conduit.databinding.FragmentLoginBinding
import com.nooblabs.conduit.toast
import com.nooblabs.conduit.viewmodels.LoginViewModel
import timber.log.Timber


class LoginFragment : Fragment() {

    lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        loginViewModel = ViewModelProviders.of(this)[LoginViewModel::class.java].apply {
            goToRegister = {
                findNavController().navigate(R.id.action_loginToRegister)
            }
        }
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.loginAnonymously = View.OnClickListener {
            startActivity(Intent(activity, HomeActivity::class.java))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.userLogin.observe(viewLifecycleOwner, Observer { user ->
            Timber.d("User $user logged in")
//            findNavController().navigate(R.id.action_loginSuccess)
            startActivity(Intent(context, HomeActivity::class.java))
        })
        loginViewModel.error.observe(viewLifecycleOwner, Observer { error ->
            context.toast("error: $error")
        })
    }
}
