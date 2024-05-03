package com.nutapos.testandroid2.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nutapos.testandroid2.databinding.FragmentIncomesBinding
import com.nutapos.testandroid2.datasource.local.ItemIncomePos
import com.nutapos.testandroid2.presentation.IncomeEvent
import com.nutapos.testandroid2.presentation.MainViewModel
import com.nutapos.testandroid2.utils.SpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IncomesFragment : Fragment() {
    private var _binding: FragmentIncomesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var incomeAdapter: IncomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncomesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        incomeAdapter = IncomeAdapter()
        viewModel.onTriggerEvents(IncomeEvent.GetIncomes)
        with(binding) {
            with(rvIncome) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = incomeAdapter
                addItemDecoration(SpacingItemDecoration(32))
            }
            btnAddIncome.setOnClickListener {
                findNavController().navigate(IncomesFragmentDirections.actionIncomesFragmentToAddIncomeFragment())
            }
        }
        incomeAdapter.setOnItemClickListener(object: IncomeAdapter.SetOnItemClickListener {
            override fun onEditClickListener(income: ItemIncomePos) {
                viewModel.onTriggerEvents(IncomeEvent.SetIncomePos(income))
                findNavController().navigate(IncomesFragmentDirections.actionIncomesFragmentToAddIncomeFragment())
            }

            override fun onDeleteClickListener(incomeId: Int?) {
                if (incomeId == null || incomeId < 1) {
                    Toast.makeText(requireContext(), "Invalid Income Id", Toast.LENGTH_SHORT).show()
                    return
                }
                ConfirmFragment.newInstance(incomeId).show(requireActivity().supportFragmentManager, "Delete")
            }

            override fun onShowPhotoListener(image: String) {
                Log.d("imageIncome", image)
                PhotoViewFragment.newInstance(image = image).show(requireActivity().supportFragmentManager, "Show Image")
            }
        })

        collectData()
    }

    private fun collectData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.incomes.collectLatest {
                        incomeAdapter.setIncomes(it)
                        incomeAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
