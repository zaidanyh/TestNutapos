package com.nutapos.testandroid2.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.nutapos.testandroid2.R
import com.nutapos.testandroid2.databinding.FragmentConfirmBinding
import com.nutapos.testandroid2.presentation.IncomeEvent
import com.nutapos.testandroid2.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmFragment : DialogFragment() {
    private var _binding: FragmentConfirmBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogNoCloseTheme)
    }

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