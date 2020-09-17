package com.sostrovsky.travelup.ui.app_init

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.sostrovsky.travelup.R
import com.sostrovsky.travelup.databinding.FragmentAppInitBinding

/**
 * Fragment that will be shown on the application start.
 */
class AppInitFragment : Fragment() {
    private lateinit var binding: FragmentAppInitBinding
    private lateinit var viewModel: AppInitViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_app_init, container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AppInitViewModel::class.java)
        viewModel.appInitComplete.observe(viewLifecycleOwner, Observer { data ->
            if (data) {
                isIndeterminate(false)
                moveForward()
            }
        })
        isIndeterminate(true)
    }

    /**
     * Changes state of the progress bar:
     *      * true  - is indeterminate (show progress all time it is shown)
     *      * false - is not indeterminate (stops progress)
     */
    private fun isIndeterminate(isIndeterminate: Boolean) {
        binding.appInitProgressBar.isIndeterminate = isIndeterminate
    }

    private fun moveForward() {
        val action =
            AppInitFragmentDirections.actionAppInitFragmentToTicketSearchFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }
}
