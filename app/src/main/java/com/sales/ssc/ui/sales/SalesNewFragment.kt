package com.sales.ssc.ui.sales

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProviders
import com.sales.ssc.AppApplication
import com.sales.ssc.R
import com.sales.ssc.adapter.MyPrintDocumentAdapter
import com.sales.ssc.utils.Constant
import com.sales.ssc.viewmodel.SalesViewModel
import kotlinx.android.synthetic.main.fragment_sales_new.*
import java.text.SimpleDateFormat
import java.util.*

class SalesNewFragment : Fragment() {

    private lateinit var salesViewModel: SalesViewModel
    private lateinit var currentDateSelected: String

    private lateinit var cal: Calendar

    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private lateinit var demoCollectionPagerAdapter: DemoCollectionPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        salesViewModel = ViewModelProviders.of(activity!!).get(SalesViewModel::class.java)
        /*if (savedInstanceState != null) {
            cal = savedInstanceState.getSerializable("counter") as Calendar
        } else {
            //cal = Calendar.getInstance()        }*/
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putSerializable("counter", cal)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sales_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //currentDateSelected =
        //   SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(cal.timeInMillis)

        //selectedDate.text = currentDateSelected

        salesViewModel.getSelectedDate().observe(viewLifecycleOwner, androidx.lifecycle.Observer {

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

                salesViewModel.setCalenderDate(cal)
                // salesPresenter.searchByDate(SearchDate(currentDateSelected))

            }

        selectedDate.setOnClickListener {
            DatePickerDialog(
                context!!, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        demoCollectionPagerAdapter = DemoCollectionPagerAdapter(childFragmentManager)
        pager.adapter = demoCollectionPagerAdapter
        tab_layout.setupWithViewPager(pager)

    }

    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    class DemoCollectionPagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int = 2

        override fun getItem(i: Int): Fragment {

            when (i) {
                0 -> {
                    return SalesFragment()
                }

                1 -> {
                    return ReturnSalesFragment()
                }
            }
            /* val fragment = SalesFragment()
             fragment.arguments = Bundle().apply {
                 // Our object is just an integer :-P
                 putInt(ARG_OBJECT, i + 1)
             }
             return fragment*/
            return SalesFragment()
        }

        override fun getPageTitle(position: Int): CharSequence {

            when (position) {
                0 -> {
                    return AppApplication.appContext.getString(R.string.pager_sales)
                }

                1 -> {
                    return AppApplication.appContext.getString(R.string.pager_return)
                }
            }
            return ""
            //return "OBJECT ${(position + 1)}"
        }
    }

}
