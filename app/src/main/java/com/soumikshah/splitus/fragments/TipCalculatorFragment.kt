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
import kotlin.math.roundToInt

class TipCalculatorFragment : Fragment(), CalcDialog.CalcDialogCallback {
    private var totalAmount: EditText? = null
    private var tipPercent: EditText? = null
    private var calculate: Button? = null
    private var finalAmountText: TextView? = null
    private var shareButton: ImageButton? = null
    private var calculatorButton: ImageButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_tip_calculator, container, false)
        totalAmount = view.findViewById(R.id.enterTotalAmount)
        tipPercent = view.findViewById(R.id.enterTipPercent)
        calculate = view.findViewById(R.id.calculate)
        finalAmountText = view.findViewById(R.id.totalAmountToBePaid)
        shareButton = view.findViewById(R.id.shareButton)
        calculatorButton = view.findViewById(R.id.calculatorButton)

        val calcDialog = CalcDialog()

        calculate!!.setOnClickListener {
            calculateAmountAndSetIt()
        }

        shareButton!!.setOnClickListener {
            shareAmount()
        }

        calculatorButton!!.setOnClickListener {
            calcDialog.settings.initialValue = null
            calcDialog.settings.isZeroShownWhenNoValue = false
            calcDialog.show(childFragmentManager, "Calc Dialog")
        }

        return view
    }


    private fun calculateAmountAndSetIt() {
        if (totalAmount!!.text != null && totalAmount!!.text.isNotEmpty()) {
            if (tipPercent!!.text == null || tipPercent!!.text.isBlank()) {
                tipPercent!!.setText("0")
                Toast.makeText(
                    requireContext(),
                    "You have not entered tip amount, so we are setting it to 0 :(",
                    Toast.LENGTH_LONG
                ).show()
            }
            val percentage =
                (tipPercent!!.text.toString().toDouble() / 100) * totalAmount!!.text.toString()
                    .toDouble()
            val totalAmountToBePaid = totalAmount!!.text.toString().toDouble() + percentage
            val finalText = "Total amount to be paid after adding tip would be " +
                    "${totalAmountToBePaid.roundToInt()}"
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
        val dataToShare = "${finalAmountText!!.text}+ - Splitus"
        shareText.putExtra(Intent.EXTRA_SUBJECT, "Total amount after adding tip")
        shareText.putExtra(Intent.EXTRA_TEXT, dataToShare)
        startActivity(Intent.createChooser(shareText, "Share Via"))
    }

    override fun onValueEntered(requestCode: Int, value: BigDecimal?) {
        totalAmount!!.setText(value!!.toInt().toString())
    }
}