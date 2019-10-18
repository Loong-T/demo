package `in`.nerd_is.demos.operator_name

import `in`.nerd_is.libs.permission_util.handlePermissionRequest
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnRes.setOnClickListener {
            clearText()
            val mcc = resources.configuration.mcc
            val mnc = resources.configuration.mnc
            val operator = NetworkOperator.from(mcc * 100 + mnc)
            textView.text = """
                MCC: $mcc
                MNC: $mnc
                Operator name: ${operator.opName}
            """.trimIndent()
        }

        btnPermission.setOnClickListener {
            handlePermissionRequest(
                PERMISSION,
                "需要权限来获取运营商信息。", {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(PERMISSION),
                        REQ_PERMISSION_PHONE_STATE
                    )
                },
                ::thenReadState,
                ::phonePermissionDenied
            )
        }
    }

    private fun phonePermissionDenied() {
        toast("请求权限被拒绝")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_PERMISSION_PHONE_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                thenReadState()
            } else {
                phonePermissionDenied()
            }
        }
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun clearText() {
        textView.text = ""
    }

    private fun thenReadState() {
        val tm = getSystemService<TelephonyManager>()
        if (tm == null) {
            toast("获取信息失败")
            return
        }

        clearText()
        val text = """
            TelephonyManager.getSimOperator(): ${tm.simOperator}
            TelephonyManager.getSimOperatorName(): ${tm.simOperatorName}
            TelephonyManager.getNetworkOperator(): ${tm.networkOperator}
            TelephonyManager.getNetworkOperatorName(): ${tm.networkOperatorName}
            TelephonyManager.getSubscriberId(): ${tm.subscriberId}
            Operator name: ${NetworkOperator.from(Integer.valueOf(tm.simOperator)).opName}
        """.trimIndent()
        textView.text = text
    }

    companion object {
        private const val PERMISSION = Manifest.permission.READ_PHONE_STATE
        private const val REQ_PERMISSION_PHONE_STATE = 0x2332
    }
}
