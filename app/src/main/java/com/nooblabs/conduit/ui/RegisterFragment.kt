package com.nooblabs.conduit.ui


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.nooblabs.conduit.HomeActivity
import com.nooblabs.conduit.R
import com.nooblabs.conduit.databinding.FragmentRegisterBinding
import com.nooblabs.conduit.toast
import com.nooblabs.conduit.viewmodels.RegisterViewModel
import timber.log.Timber


class RegisterFragment : Fragment() {

    lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(
            inflater,
            R.layout.fragment_register,
            container,
            false
        )
        registerViewModel = ViewModelProviders.of(this)[RegisterViewModel::class.java].apply {
            goToLogin = {
                findNavController().navigate(R.id.action_registerToLogin)
            }
        }

        binding.registerViewModel = registerViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel.userRegistration.observe(viewLifecycleOwner, Observer { user ->
            Timber.d("User registered: $user")
//            findNavController().navigate(R.id.action_registerSuccess)
            startActivity(Intent(context, HomeActivity::class.java))
        })
        registerViewModel.error.observe(viewLifecycleOwner, Observer { error ->
            context.toast("error: $error")
        })
    }


}
