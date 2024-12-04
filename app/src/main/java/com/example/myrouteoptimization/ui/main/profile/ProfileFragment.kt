package com.example.myrouteoptimization.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myrouteoptimization.databinding.FragmentProfileBinding
import com.example.myrouteoptimization.ui.AuthViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var factory: AuthViewModelFactory
    private val viewModel: ProfileViewModel by viewModels {
        factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = AuthViewModelFactory.getInstanceUser(requireContext())

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.textName.text = user.username
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}