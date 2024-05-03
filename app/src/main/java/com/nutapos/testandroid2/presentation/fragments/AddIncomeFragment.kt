package com.nutapos.testandroid2.presentation.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.nutapos.testandroid2.R
import com.nutapos.testandroid2.databinding.FragmentAddIncomeBinding
import com.nutapos.testandroid2.datasource.local.ItemIncomePos
import com.nutapos.testandroid2.presentation.IncomeEvent
import com.nutapos.testandroid2.presentation.MainActivity
import com.nutapos.testandroid2.presentation.MainViewModel
import com.nutapos.testandroid2.utils.convertStringToBitMap
import com.nutapos.testandroid2.utils.getBase64FromUri
import com.nutapos.testandroid2.presentation.photo.TakePhotoActivity
import com.nutapos.testandroid2.utils.hasRequiredPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddIncomeFragment : Fragment() {

    private var _binding: FragmentAddIncomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private var date: String? = null
    private var time: String? = null
    private var itemIncomePos: ItemIncomePos? = null

    private lateinit var photo: String
    private lateinit var sdf: SimpleDateFormat

    private val permissionCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            checkPermission()
            return@registerForActivityResult
        }
        Toast.makeText(requireContext(), "Berikan izin untuk mengakses kamera anda", Toast.LENGTH_SHORT).show()
    }

    private val getImageFromCameraX =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data?.getStringExtra(TakePhotoActivity.RESULT_IMAGE_CAPTURE)
                binding.resultPhoto.setImageURI(Uri.parse(data.toString()))
                photo = getBase64FromUri(Uri.parse(data.toString()), requireActivity().contentResolver)
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

        sdf = SimpleDateFormat("dd MMMM yyyy-HH:mm", Locale("in", "ID"))

        with(binding) {
            val now = Date()
            val dateTime = sdf.format(now)
            val dateSplit = dateTime.split("-")
            date = dateSplit.first()
            time = dateSplit.last()

            val typeIncomes = resources.getStringArray(R.array.type_income)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, typeIncomes)
            typeIncome.setAdapter(arrayAdapter)
            btnBack.setOnClickListener {
                backStack()
            }
            btnAddPhoto.setOnClickListener {
                checkPermission()
            }

            btnSave.setOnClickListener {
                val destination = edtDestination.text.toString().trim()
                val sourceIncome = edtSourceIncome.text.toString().trim()
                val nominal = edtNominal.text.toString().trim()
                val description = edtDesc.text.toString().trim()
                val type = typeIncome.text.toString().trim()

                val data = ItemIncomePos(
                    incomeId = if (itemIncomePos == null) null else itemIncomePos?.incomeId,
                    destination = destination, sourceIncome = sourceIncome, nominal = nominal.toInt(),
                    description = description, type = type, image = photo, date = date.toString(),
                    time = time.toString()
                )
                if (itemIncomePos == null)
                    viewModel.onTriggerEvents(IncomeEvent.InsertIncome(data))
                else viewModel.onTriggerEvents(IncomeEvent.UpdateIncome(data))

                backStack()
            }
        }
        collectData()
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.dataIncome.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collectLatest {
                if (it != null) {
                    itemIncomePos = it.copy()
                    with(binding) {
                        edtDestination.setText(it.destination)
                        edtSourceIncome.setText(it.sourceIncome)
                        edtNominal.setText(it.nominal.toString())
                        edtDesc.setText(it.description)
                        typeIncome.setText(it.type)
                        photo = it.image
                        resultPhoto.setImageBitmap(convertStringToBitMap(it.image))
                        date = it.date
                        time = it.time
                    }
                }
            }
        }
    }

    private fun checkPermission() {
        if (hasRequiredPermissions(requireContext(), arrayOf(Manifest.permission.CAMERA))) {
            getImageFromCameraX.launch(Intent(requireContext(), TakePhotoActivity::class.java))
            return
        }
        permissionCamera.launch(Manifest.permission.CAMERA)
    }

    private fun backStack() {
        (activity as MainActivity).wrapperFirstOnClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
