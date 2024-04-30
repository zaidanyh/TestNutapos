package com.example.testandroid2.presentation.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.testandroid2.R
import com.example.testandroid2.databinding.FragmentAddIncomeBinding
import com.example.testandroid2.datasource.local.ItemIncomePos
import com.example.testandroid2.presentation.IncomeEvent
import com.example.testandroid2.presentation.MainActivity
import com.example.testandroid2.presentation.MainViewModel
import com.example.testandroid2.utils.convertStringToBitMap
import com.example.testandroid2.utils.getBase64FromUri
import com.github.dhaval2404.imagepicker.ImagePicker
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddIncomeFragment : Fragment() {

    private var _binding: FragmentAddIncomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by sharedViewModel()
    private val args: AddIncomeFragmentArgs by navArgs()

    private lateinit var photo: String
    private var date: String? = null
    private var time: String? = null

    private val getImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    binding.resultPhoto.setImageURI(data?.data)
                    photo = getBase64FromUri(data?.data, requireActivity().contentResolver)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val sdf = SimpleDateFormat("dd MMMM yyyy-HH:mm", Locale("in", "ID"))
            val now = Date()
            val dateTime = sdf.format(now)
            val dateSplit = dateTime.split("-")
            date = dateSplit.first()
            time = dateSplit.last()

            if (args.dataIncome.incomeId != null) {
                edtDestination.setText(args.dataIncome.destination)
                edtSourceIncome.setText(args.dataIncome.sourceIncome)
                edtNominal.setText(args.dataIncome.nominal.toString())
                edtDesc.setText(args.dataIncome.description)
                typeIncome.setText(args.dataIncome.type)
                photo = args.dataIncome.image
                resultPhoto.setImageBitmap(convertStringToBitMap(args.dataIncome.image))
                date = args.dataIncome.date
                time = args.dataIncome.time
            }
            val typeIncomes = resources.getStringArray(R.array.type_income)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, typeIncomes)
            typeIncome.setAdapter(arrayAdapter)
            btnBack.setOnClickListener {
                backStack()
            }
            btnAddPhoto.setOnClickListener {
                ImagePicker.with(requireActivity()).cameraOnly()
                    .crop(3f, 3f)
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent { intent ->
                        getImageForResult.launch(intent)
                    }
            }

            btnSave.setOnClickListener {
                val destination = edtDestination.text.toString().trim()
                val sourceIncome = edtSourceIncome.text.toString().trim()
                val nominal = edtNominal.text.toString().trim()
                val description = edtDesc.text.toString().trim()
                val type = typeIncome.text.toString().trim()

                val data = ItemIncomePos(
                    incomeId = if (args.dataIncome.incomeId == null) null else args.dataIncome.incomeId,
                    destination = destination, sourceIncome = sourceIncome, nominal = nominal.toInt(),
                    description = description, type = type, image = photo, date = date.toString(),
                    time = time.toString()
                )
                if (args.dataIncome.incomeId == null)
                    viewModel.onTriggerEvents(IncomeEvent.InsertIncome(data))
                else viewModel.onTriggerEvents(IncomeEvent.UpdateIncome(data))

                backStack()
            }
        }
    }

    private fun backStack() {
        (activity as MainActivity).wrapperFirstOnClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
