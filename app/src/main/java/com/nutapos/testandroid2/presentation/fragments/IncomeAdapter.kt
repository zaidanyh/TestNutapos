package com.nutapos.testandroid2.presentation.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nutapos.testandroid2.R
import com.nutapos.testandroid2.databinding.ItemIncomeBinding
import com.nutapos.testandroid2.datasource.local.ItemIncomePos
import com.nutapos.testandroid2.utils.getNumberRupiah

class IncomeAdapter: RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder>() {

    private val incomes = mutableListOf<ItemIncomePos>()
    private lateinit var onItemClickListener: SetOnItemClickListener

    fun setIncomes(incomes: List<ItemIncomePos>?) {
        if (incomes == null) return
        this.incomes.clear()
        this.incomes.addAll(incomes)
    }

    fun setOnItemClickListener(onItemClickListener: SetOnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class IncomeViewHolder(private val binding: ItemIncomeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(income: ItemIncomePos) {
            with(binding) {
                tvSourceIncome.text = root.context.getString(R.string.desc_format, income.sourceIncome, income.destination)
                tvTime.text = income.time
                tvDesc.text = income.description
                tvNominal.text = getNumberRupiah(income.nominal)
                btnEdit.setOnClickListener {
                    onItemClickListener.onEditClickListener(income)
                }
                btnDelete.setOnClickListener {
                    onItemClickListener.onDeleteClickListener(incomeId = income.incomeId)
                }
                btnShowImage.setOnClickListener {
                    onItemClickListener.onShowPhotoListener(income.image)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IncomeViewHolder {
        return IncomeViewHolder(ItemIncomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        holder.bind(income = incomes[position])
    }

    override fun getItemCount(): Int = incomes.size

    interface SetOnItemClickListener {
        fun onEditClickListener(income: ItemIncomePos)
        fun onDeleteClickListener(incomeId: Int?)
        fun onShowPhotoListener(image: String)
    }
}