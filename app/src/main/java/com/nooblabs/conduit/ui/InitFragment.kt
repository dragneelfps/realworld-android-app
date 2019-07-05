package com.nooblabs.conduit.ui


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.nooblabs.conduit.HomeActivity
import com.nooblabs.conduit.R
import com.nooblabs.conduit.toast
import com.nooblabs.conduit.viewmodels.InitViewModel
import timber.log.Timber

class InitFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_init, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val initViewModel = ViewModelProviders.of(this)[InitViewModel::class.java]

        initViewModel.currentUserData.observe(viewLifecycleOwner, Observer { user ->
            Timber.d("user: $user")
            if(user != null) {
//                findNavController().navigate(R.id.action_userFound)
                startActivity(Intent(context, HomeActivity::class.java))
            } else {
                findNavController().navigate(R.id.action_noUserFound)
            }
        })

        initViewModel.error.observe(viewLifecycleOwner, Observer { error ->
            context.toast("error: $error")
        })

        initViewModel.loadUser()
    }


}
