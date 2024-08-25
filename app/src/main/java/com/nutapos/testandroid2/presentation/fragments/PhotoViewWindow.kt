package com.nutapos.testandroid2.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nutapos.testandroid2.databinding.FragmentPhotoViewBinding
import com.nutapos.testandroid2.utils.convertStringToBitMap

class PhotoViewWindow : DialogFragment() {

    private var _binding: FragmentPhotoViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageStr = arguments?.getString(INCOME_IMAGE) ?: ""
        with(binding) {
            val imageBitmap = convertStringToBitMap(imageStr)
            ivPhotoView.setImageBitmap(imageBitmap)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val INCOME_IMAGE = "income_image"

        @JvmStatic
        fun newInstance(image: String) =
            PhotoViewWindow().apply {
                arguments = Bundle().apply {
                    putString(INCOME_IMAGE, image)
                }
            }
    }
}
