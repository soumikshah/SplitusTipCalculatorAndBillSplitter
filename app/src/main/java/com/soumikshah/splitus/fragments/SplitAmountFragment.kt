package com.soumikshah.splitus.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.maltaisn.calcdialog.CalcDialog
import com.soumikshah.splitus.R
import java.math.BigDecimal

class SplitAmountFragment:Fragment(), CalcDialog.CalcDialogCallback {
    var totalAmount: EditText? = null
    var numberOfPeople: EditText? = null
    var calculate: Button? = null
    var finalAmountText: TextView? = null
    var shareButton: ImageButton? = null
    var calculatorButton: ImageButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_split_amount,container,false)
        totalAmount = view.findViewById(R.id.enterTotalAmount)
        numberOfPeople = view.findViewById(R.id.enterNumberOfPeople)
        calculate = view.findViewById(R.id.calculate)
        finalAmountText = view.findViewById(R.id.totalAmountToBePaid)
        shareButton = view.findViewById(R.id.shareButton)
        calculatorButton = view.findViewById(R.id.calculatorButton)

        val calcDialog: CalcDialog = CalcDialog()
        calculate!!.setOnClickListener {
            calculateAmountAndSetIt()
        }

        shareButton!!.setOnClickListener {
            shareAmount()
        }

        calculatorButton!!.setOnClickListener {
            calcDialog.settings.initialValue = null
            calcDialog.settings.isZeroShownWhenNoValue = false
            calcDialog.show(childFragmentManager,"Calc Dialog")
        }
        return view
    }

    private fun calculateAmountAndSetIt() {
        if (totalAmount!!.text != null && totalAmount!!.text.isNotEmpty()) {
            if (numberOfPeople!!.text == null || numberOfPeople!!.text.isBlank()) {
                numberOfPeople!!.setText("0")
                Toast.makeText(
                    requireContext(),
                    "You have not entered number of people, so we are setting it to 0",
                    Toast.LENGTH_LONG
                ).show()
            }
            val finalAmount = totalAmount!!.text.toString().toDouble() / numberOfPeople!!.text.toString().toInt()
            val finalText = "Total amount of ${totalAmount!!.text} is divided between ${numberOfPeople!!.text} people" +
                    ". \nSo each person will pay $finalAmount - Splitus"
            finalAmountText!!.text = finalText
        } else {
            Toast.makeText(
                requireContext(), getString(R.string.enter_valid_amount),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun shareAmount() {
        val shareText = Intent(Intent.ACTION_SEND)
        shareText.type = "text/plain"
        val dataToShare = "${finalAmountText!!.text}"
        shareText.putExtra(Intent.EXTRA_SUBJECT, "Total amount after adding tip")
        shareText.putExtra(Intent.EXTRA_TEXT, dataToShare)
        startActivity(Intent.createChooser(shareText, "Share Via"))
    }

    override fun onValueEntered(requestCode: Int, value: BigDecimal?) {
        totalAmount!!.setText(value!!.toInt().toString())
    }
}