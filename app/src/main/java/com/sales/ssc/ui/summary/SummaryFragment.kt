package com.sales.ssc.ui.summary

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.sales.ssc.R
import com.sales.ssc.utils.Constant
import com.sales.ssc.viewmodel.SummaryViewModel
import kotlinx.android.synthetic.main.fragment_summary.*

import java.text.SimpleDateFormat
import java.util.*

class SummaryFragment : Fragment() {

    private lateinit var summaryViewModel: SummaryViewModel
    private lateinit var currentDateSelected: String
    private lateinit var cal: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        summaryViewModel = ViewModelProviders.of(activity!!).get(SummaryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        summaryViewModel.getSelectedDate().observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            if (it != null) {
                cal = it
                currentDateSelected =
                    SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH).format(cal.timeInMillis)
                selectedDate.text = currentDateSelected
            }
        }
        )

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                summaryViewModel.setCalenderDate(cal)
            }


        selectedDate.setOnClickListener {
            DatePickerDialog(
                context!!, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}
