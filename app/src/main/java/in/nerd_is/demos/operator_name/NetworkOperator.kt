package `in`.nerd_is.demos.operator_name

/**
 * @author Xuqiang ZHENG on 19/8/15.
 */
enum class NetworkOperator(val opName: String) {

    Mobile("移动"),
    Unicom("联通"),
    Telecom("电信"),
    Tietong("铁通"),
    Other("其他");

    companion object {
        /**
         * 根据 [code]（MCC+MNC) 返回运营商
         */
        fun from(code: Int) = when (code) {
            46000, 46002, 46004, 46007, 46008 -> Mobile
            46001, 46006, 46009 -> Unicom
            46003, 46005, 46011 -> Telecom
            46020 -> Tietong
            else -> Other
        }

        fun fromOpName(name: String) = when (name) {
            "移动" -> Mobile
            "联通" -> Unicom
            "电信" -> Telecom
            "铁通" -> Tietong
            else -> Other
        }
    }
}