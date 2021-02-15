package com.geranzo.sliidetest.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geranzo.sliidetest.MyApp
import com.geranzo.sliidetest.databinding.MainFragmentBinding
import com.geranzo.sliidetest.ui.ScrollChildSwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as MyApp).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = MainFragmentBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupList(binding.recyclerView, binding.refreshLayout)

        viewModel.users.observe(viewLifecycleOwner) { users ->
            (binding.recyclerView.adapter as? UsersAdapter)?.submitList(users)
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->
            if (message != null && isResumed) {
                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()

                requireView().postDelayed(TimeUnit.SECONDS.toMillis(5)) {
                    viewModel.clearError()
                }
            }
        }

        return binding.root
    }

    private fun setupList(recyclerView: RecyclerView, swipeRefreshLayout: ScrollChildSwipeRefreshLayout) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UsersAdapter()
            recyclerView.setHasFixedSize(true)
        }
        swipeRefreshLayout.scrollUpChild = recyclerView
    }
}
