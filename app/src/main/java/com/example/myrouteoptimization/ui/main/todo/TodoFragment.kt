package com.example.myrouteoptimization.ui.main.todo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myrouteoptimization.databinding.FragmentTodoBinding
import com.example.myrouteoptimization.ui.addroute.AddRouteActivity

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val todoViewModel =
            ViewModelProvider(this)[TodoViewModel::class.java]

        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        todoViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivAddRoute.setOnClickListener {
            val intent = Intent(requireContext(), AddRouteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}