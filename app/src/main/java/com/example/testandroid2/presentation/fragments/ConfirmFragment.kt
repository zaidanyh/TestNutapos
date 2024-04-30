package com.example.testandroid2.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.testandroid2.R
import com.example.testandroid2.databinding.FragmentConfirmBinding
import com.example.testandroid2.presentation.IncomeEvent
import com.example.testandroid2.presentation.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ConfirmFragment : DialogFragment() {
    private var _binding: FragmentConfirmBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val incomeId = arguments?.getInt(INCOME_ID) ?: -1

        with(binding) {
            btnPositive.setOnClickListener {
                viewModel.onTriggerEvents(IncomeEvent.DeleteIncome(incomeId))
                this@ConfirmFragment.dismiss()
            }
            btnNegative.setOnClickListener { this@ConfirmFragment.dismiss() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val INCOME_ID = "income_id"

        @JvmStatic
        fun newInstance(incomeId: Int) =
            ConfirmFragment().apply {
                arguments = Bundle().apply {
                    putInt(INCOME_ID, incomeId)
                }
            }
    }
}