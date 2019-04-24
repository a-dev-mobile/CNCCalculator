package a.dev.mobile_cnc

import a.dev.mobile_cnc.R.id
import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_formula_find_ra_rz.btnFindRzClickFo
import kotlinx.android.synthetic.main.layout_formula_find_ra_rz.btnFindRzClickR
import kotlinx.android.synthetic.main.layout_formula_find_ra_rz.tvRa
import kotlinx.android.synthetic.main.layout_formula_find_ra_rz.tvRz
import kotlinx.android.synthetic.main.layout_formula_find_v.btnFindVClickD
import kotlinx.android.synthetic.main.layout_formula_find_v.btnFindVClickN
import kotlinx.android.synthetic.main.layout_formula_find_v.btnFindVClickV
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private var calculator: Calculator? = null
    private var displayPrimary: TextView? = null
    private var displaySecondary: TextView? = null
    private var hsv: HorizontalScrollView? = null

    private var isLastFindV: Boolean = true
    private val TAG = "MainActivity"
    private val DECIMAL_ONE = "#.#"
    private val DECIMAL_TWO = "#.##"
    private val DECIMAL_THREE = "#.###"
    var text: String?
        get() = calculator!!.text
        set(s) {
            calculator!!.text = s
        }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if (sp.getBoolean("pref_dark", false))
            when (sp.getString("pref_theme", "0")) {
                "0" -> setTheme(R.style.AppTheme_Dark_Blue)
                "1" -> setTheme(R.style.AppTheme_Dark_Cyan)
                "2" -> setTheme(R.style.AppTheme_Dark_Gray)
                "3" -> setTheme(R.style.AppTheme_Dark_Green)
                "4" -> setTheme(R.style.AppTheme_Dark_Purple)
                "5" -> setTheme(R.style.AppTheme_Dark_Red)
            }
        else
            when (sp.getString("pref_theme", "0")) {
                "0" -> setTheme(R.style.AppTheme_Light_Blue)
                "1" -> setTheme(R.style.AppTheme_Light_Cyan)
                "2" -> setTheme(R.style.AppTheme_Light_Gray)
                "3" -> setTheme(R.style.AppTheme_Light_Green)
                "4" -> setTheme(R.style.AppTheme_Light_Purple)
                "5" -> setTheme(R.style.AppTheme_Light_Red)
            }
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        viewPager.adapter = CustomPagerAdapter(this)





        displayPrimary = findViewById<View>(R.id.display_primary) as TextView
        displaySecondary = findViewById<View>(R.id.display_secondary) as TextView
        hsv = findViewById<View>(R.id.display_hsv) as HorizontalScrollView
        val digits = arrayOf(
            findViewById<View>(R.id.button_0) as TextView,
            findViewById<View>(R.id.button_1) as TextView,
            findViewById<View>(R.id.button_2) as TextView,
            findViewById<View>(R.id.button_3) as TextView,
            findViewById<View>(R.id.button_4) as TextView,
            findViewById<View>(R.id.button_5) as TextView,
            findViewById<View>(R.id.button_6) as TextView,
            findViewById<View>(R.id.button_7) as TextView,
            findViewById<View>(R.id.button_8) as TextView,
            findViewById<View>(R.id.button_9) as TextView
        )
        for (i in digits.indices) {
            val id = digits[i].text as String
            digits[i].setOnClickListener { calculator!!.digit(id[0]) }
        }
        val buttons = arrayOf(

            findViewById<View>(R.id.button_add) as TextView,
            findViewById<View>(R.id.button_subtract) as TextView,
            findViewById<View>(R.id.button_multiply) as TextView,
            findViewById<View>(R.id.button_divide) as TextView,
            findViewById<View>(R.id.button_decimal) as TextView,
            findViewById<View>(R.id.button_equals) as TextView
        )
        for (i in buttons.indices) {
            val id = buttons[i].text as String
            buttons[i].setOnClickListener {

                if (id == "÷")
                    calculator!!.numOpNum('/')
                if (id == "×")
                    calculator!!.numOpNum('*')
                if (id == "−")
                    calculator!!.numOpNum('-')
                if (id == "+")
                    calculator!!.numOpNum('+')
                if (id == ".")
                    calculator!!.decimal()
                if (id == "=" && text != "")
                    calculator!!.equal()
            }
        }
        findViewById<View>(R.id.button_delete).setOnClickListener { calculator!!.delete() }
        findViewById<View>(R.id.button_delete).setOnLongClickListener {
            if (displayPrimary!!.text.toString().trim { it <= ' ' } != "") {
                clearDisplay()
            }
            false
        }
        findViewById<View>(R.id.settings).setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    SettingsActivity::class.java
                )
            )
        }
        calculator = Calculator(this)
        if (savedInstanceState != null)
            text = savedInstanceState.getString("text")
        if (sp.getInt("launch_count", 5) == 0) {
            RateDialog.show(this)
            val editor = sp.edit()
            editor.putInt("launch_count", -1)
            editor.apply()
        }
    }

    private fun clearDisplay() {
        val displayOverlay = findViewById<View>(id.display_overlay)
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            val circle = ViewAnimationUtils.createCircularReveal(
                displayOverlay,
                displayOverlay.measuredWidth / 2,
                displayOverlay.measuredHeight,
                0f,
                Math.hypot(
                    displayOverlay.width.toDouble(),
                    displayOverlay.height.toDouble()
                ).toInt().toFloat()
            )
            circle.duration = 300
            circle.addListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    calculator!!.text = ""
                }

                override fun onAnimationCancel(animation: Animator) {
                    calculator!!.text = ""
                }

                override fun onAnimationRepeat(animation: Animator) {}
            })
            val fade = ObjectAnimator.ofFloat(displayOverlay, "alpha", 0f)
            fade.interpolator = DecelerateInterpolator()
            fade.duration = 200
            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(circle, fade)
            displayOverlay.alpha = 1f
            animatorSet.start()
        } else
            calculator!!.text = ""
    }

    override fun onResume() {
        super.onResume()
        text = text
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString("text", text)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        text = savedInstanceState.getString("text")
    }

    fun displayPrimaryScrollLeft(`val`: String) {
        displayPrimary!!.text = formatToDisplayMode(`val`)
        val vto = hsv!!.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                hsv!!.viewTreeObserver.removeGlobalOnLayoutListener(this)
                hsv!!.fullScroll(View.FOCUS_LEFT)
            }
        })
    }

    fun displayPrimaryScrollRight(`val`: String) {
        displayPrimary!!.text = formatToDisplayMode(`val`)
        val vto = hsv!!.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                hsv!!.viewTreeObserver.removeGlobalOnLayoutListener(this)
                hsv!!.fullScroll(View.FOCUS_RIGHT)
            }
        })
    }

    fun displaySecondary(`val`: String) {
        displaySecondary!!.text = formatToDisplayMode(`val`)
    }

    private fun formatToDisplayMode(s: String): String {
        return s.replace("/", "÷").replace("*", "×").replace(",", ".")
            .replace(" ", "").replace("∞", "Infinity").replace("NaN", "Undefined")
    }

    fun clickBtnFormulaV(view: View) {

        if (displaySecondary!!.text.isEmpty()) {
            return
        }

        val btnD = btnFindVClickD
        val btnV = btnFindVClickV
        val btnN = btnFindVClickN



        when (view.id) {
            btnD.id -> {
                btnD.text = getFormatTextFrom(displaySecondary!!.text.toString(), DECIMAL_ONE)


                if (isLastFindV) {
                    btnV.text = calcV(btnN, btnD)
                    changeColor(btnV, btnN)
                } else {
                    changeColor(btnN, btnV)

                    btnN.text = calcN(btnV, btnD)
                }
            }
            btnN.id -> {
                changeColor(btnV, btnN)
                isLastFindV = true
                btnN.text = getFormatTextFrom(displaySecondary!!.text.toString(), DECIMAL_TWO)
                btnV.text = calcV(btnN, btnD)
            }

            btnV.id -> {
                changeColor(btnN, btnV)
                isLastFindV = false
                btnV.text = getFormatTextFrom(displaySecondary!!.text.toString(), DECIMAL_TWO)
                btnN.text = calcN(btnV, btnD)
            }

        }


        clearDisplay()
    }

    private fun changeColor(btn1: Button, btn2: Button) {
        btn1.setTextColor(Color.RED)
        btn2.setTextColor(Color.WHITE)
    }

    private fun getFormatTextFrom(str: String, pattern: String): String? {

        val s = str.replace(",", ".").replace("-", "")
        val d: Double

        d = try {
            s.toDouble()
        } catch (e: NumberFormatException) {
            0.001
        }

/*        Log.d(TAG, " displaySecondary?.text= ${displaySecondary?.text}")
        Log.d(TAG, " double = $d")*/

        return decFormat((Math.abs(d)), pattern)
    }

    private fun getFormatDoubleFrom(str: String): Double {

        val s = str.replace(",", ".").replace("-", "")
        val d: Double

        d = try {
            s.toDouble()
        } catch (e: NumberFormatException) {
            0.001
        }

/*        Log.d(TAG, " displaySecondary?.text= ${displaySecondary?.text}")
        Log.d(TAG, " double = $d")*/

        return d
    }

    private fun calcN(btnV: Button, btnD: Button): String? {

        val v = getFormatDoubleFrom(btnV.text.toString())
        val d = getFormatDoubleFrom(btnD.text.toString())
        val result = (1000 * v) / (Math.PI * d)

        return decFormat(result, DECIMAL_ONE)
    }

    private fun calcV(btnN: Button, btnD: Button): String? {

        val n = getFormatDoubleFrom(btnN.text.toString())
        val d = getFormatDoubleFrom(btnD.text.toString())
        val result = (Math.PI * d * n) / 1000

        return decFormat(result, DECIMAL_ONE)
    }

    private fun decFormat(number: Double, pattern: String): String? {

        val df = DecimalFormat(pattern)

        return df.format(number)
    }

    fun clickBtnFormulaRz(view: View) {

        val btnF = btnFindRzClickFo
        val btnR = btnFindRzClickR



        when (view.id) {

            btnF.id -> {
                btnF.text = getFormatTextFrom(displaySecondary!!.text.toString(), DECIMAL_TWO)
            }
            btnR.id -> {
                btnR.text = getFormatTextFrom(displaySecondary!!.text.toString(), DECIMAL_TWO)
            }
        }
        //получаем 0 = Rz
        //получаем 1 = Ra
        tvRz.text = calcRzRa(btnF, btnR)[0]
        tvRa.text = calcRzRa(btnF, btnR)[1]
        clearDisplay()
    }

    private fun calcRzRa(btnF: Button, btnR: Button): Array<String?> {

        val f = getFormatDoubleFrom(btnF.text.toString())
        val r = getFormatDoubleFrom(btnR.text.toString())

        val rz = ((f * f) / (8 * r)) * 1000

        val ra: Double
        ra =
            if (rz > 10)
                rz / 4
            else
                rz / 5
        val rzs = "Rz = ${decFormat(rz, DECIMAL_TWO)}"
        val ras = "Ra = ${decFormat(ra, DECIMAL_TWO)}"


        return arrayOf(rzs, ras)
    }
}


