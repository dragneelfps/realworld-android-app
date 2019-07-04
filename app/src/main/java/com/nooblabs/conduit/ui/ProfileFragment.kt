package com.nooblabs.conduit.ui


import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.nooblabs.conduit.MainActivity
import com.nooblabs.conduit.R
import com.nooblabs.conduit.databinding.FragmentProfileBinding
import com.nooblabs.conduit.viewmodels.ProfileViewModel


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        profileViewModel = ViewModelProviders.of(this)[ProfileViewModel::class.java]

        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )
        binding.onLoginClick = View.OnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }

        binding.onProfileImageClick = View.OnClickListener {
            showChangeImageDialog()
        }

        binding.onPasswordChangeClick = View.OnClickListener {
            showChangePasswordDialog()
        }

        binding.profileViewModel = profileViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.loadUser()
    }

    private fun showChangeImageDialog() {
        MaterialDialog(context!!).show {
            input(prefill = profileViewModel.getImageUrl()) { _, imageUrl ->
                profileViewModel.changeImage(imageUrl.toString())
            }
            title(text = "Enter new profile image URL")
        }
    }

    private fun showChangePasswordDialog() {
        MaterialDialog(context!!).show {
            val type = InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD
            input(inputType = type) { _, password ->
                profileViewModel.changePassword(password.toString())
            }
            title(text = "Enter new password")
        }
    }


}
