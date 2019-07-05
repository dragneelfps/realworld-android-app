package com.nooblabs.conduit.ui


import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.nooblabs.conduit.R
import com.nooblabs.conduit.databinding.FragmentProfileBinding
import com.nooblabs.conduit.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        profileViewModel = ViewModelProviders.of(this)[ProfileViewModel::class.java].apply {
            onSaved = {
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            }
        }

        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )

        binding.onProfileImageClick = View.OnClickListener {
            showChangeImageDialog()
        }

        binding.onPasswordChangeClick = View.OnClickListener {
            showChangePasswordDialog()
        }

        binding.profileViewModel = profileViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.loadUser()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(profileToolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        profileToolbar.setNavigationOnClickListener {
            activity.finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.item_save -> {
                profileViewModel.onSave()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
