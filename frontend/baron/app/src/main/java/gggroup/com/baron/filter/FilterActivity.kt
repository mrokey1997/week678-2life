package gggroup.com.baron.filter

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import gggroup.com.baron.R
import gggroup.com.baron.entities.ItemSearch
import gggroup.com.baron.posts.ListPostActivity
import kotlinx.android.synthetic.main.activity_filter.*
import java.text.DecimalFormat
import java.util.*
import java.util.Arrays.asList


class FilterActivity : AppCompatActivity(), FilterContract.View {
    private var presenter: FilterContract.Presenter? = null

    //private var types: BooleanArray = booleanArrayOf(false,false)
    private var minPrice: Float = 0F
    private var maxPrice: Float = 12F
    private var types = BooleanArray(2)
    private var checkUtils = BooleanArray(16)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        presenter = FilterPresenter(this)
        //setToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
        Arrays.fill(types, false)

        Arrays.fill(checkUtils, false)
        val sex = LinkedList(asList("Nam", "Nữ", "Cả 2"))
        spinnerSex.attachDataSource(sex)
        val city = LinkedList(asList("Hà Nội", "Hồ Chí Minh"))
        spinnerProvince.attachDataSource(city)
        presenter?.getAllDistrict()
        spinnerProvince.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                (presenter as FilterPresenter).getDistrict(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        })
        onClick()
        apply.setOnClickListener {
            startActivity(Intent(this, ListPostActivity::class.java))
        }
        val formatter = DecimalFormat("#,###,###")
        txtMinPrice.text = formatter.format(0) + " VNĐ"
        txtMaxPrice.text = formatter.format(12000 * 1000) + " VNĐ"
        seekbar.setValue(0F, 12000F)
        seekbar.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(view: RangeSeekBar, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                txtMinPrice.text = formatter.format(leftValue.toInt() * 1000) + " VNĐ"
                txtMaxPrice.text = formatter.format(rightValue.toInt() * 1000) + " VNĐ"
                minPrice = (leftValue.toInt()).toFloat() / 1000
                maxPrice = (rightValue.toInt()).toFloat() / 1000
            }

            override fun onStartTrackingTouch(view: RangeSeekBar, isLeft: Boolean) {

            }

            override fun onStopTrackingTouch(view: RangeSeekBar, isLeft: Boolean) {
            }
        })
        apply.setOnClickListener { actionSearch() }
        onClick()
        chip_compound.setChipBackgroundColorResource(R.color.selected)
        types[1] = !types[1]
    }

    override fun actionSearch() {
        val type: Int = when {
            types[0] -> 0
            types[1] -> 1
            else -> 2
        }
        //val utils: ArrayList<String> = ArrayList()
        val city = spinnerProvince.text.toString()
        val district = spinnerDistrict.text.toString()
        val intent = Intent(this, ListPostActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("search", ItemSearch(city, district, minPrice, maxPrice, type))
        intent.putExtra("myBundle", bundle)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.enter, 0)
        startActivity(intent, options.toBundle())
        // presenter?.actionSearch(city, district, minPrice, maxPrice, type)
    }

    override fun onClick() {

        minus.setOnClickListener {
            var amount = amountPeople.text.toString().toInt()
            amount--
            amountPeople.text = amount.toString()
            if (amount == 1)
                minus.isEnabled = false
            if (amount == 9)
                plus.isEnabled = true
        }
        plus.setOnClickListener {
            var amount = amountPeople.text.toString().toInt()
            amount++
            if (amount == 2)
                minus.isEnabled = true
            if (amount == 10)
                plus.isEnabled = false
            amountPeople.text = amount.toString()
        }
        chip_compound.setOnClickListener {
            if (!types[1]) {
                chip_compound.setChipBackgroundColorResource(R.color.selected)
                types[1] = !types[1]
                chip_rent.setChipBackgroundColorResource(R.color.background_chip)
                types[0] = !types[0]
            }
        }

        chip_rent.setOnClickListener {
            if (!types[0]) {
                chip_rent.setChipBackgroundColorResource(R.color.selected)
                types[0] = !types[0]
                chip_compound.setChipBackgroundColorResource(R.color.background_chip)
                types[1] = !types[1]
            }
        }
        air_conditioner.setOnClickListener {
            if (checkUtils[0]) {
                air_conditioner.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[0] = !checkUtils[0]
            } else {
                air_conditioner.setChipBackgroundColorResource(R.color.selected)
                checkUtils[0] = !checkUtils[0]
            }
        }
        washing.setOnClickListener {
            if (checkUtils[1]) {
                washing.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[1] = !checkUtils[1]
            } else {
                washing.setChipBackgroundColorResource(R.color.selected)
                checkUtils[1] = !checkUtils[1]
            }
        }
        fridge.setOnClickListener {
            if (checkUtils[2]) {
                fridge.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[2] = !checkUtils[2]
            } else {
                fridge.setChipBackgroundColorResource(R.color.selected)
                checkUtils[2] = !checkUtils[2]
            }
        }
        wc.setOnClickListener {
            if (checkUtils[3]) {
                wc.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[3] = !checkUtils[3]
            } else {
                wc.setChipBackgroundColorResource(R.color.selected)
                checkUtils[3] = !checkUtils[3]
            }
        }
        parking.setOnClickListener {
            if (checkUtils[4]) {
                parking.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[4] = !checkUtils[4]
            } else {
                parking.setChipBackgroundColorResource(R.color.selected)
                checkUtils[4] = !checkUtils[4]
            }
        }
        wifi.setOnClickListener {
            if (checkUtils[5]) {
                wifi.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[5] = !checkUtils[5]
            } else {
                wifi.setChipBackgroundColorResource(R.color.selected)
                checkUtils[5] = !checkUtils[5]
            }
        }
        free.setOnClickListener {
            if (checkUtils[6]) {
                free.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[6] = !checkUtils[6]
            } else {
                free.setChipBackgroundColorResource(R.color.selected)
                checkUtils[6] = !checkUtils[6]
            }
        }
        key.setOnClickListener {
            if (checkUtils[7]) {
                key.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[7] = !checkUtils[7]
            } else {
                key.setChipBackgroundColorResource(R.color.selected)
                checkUtils[7] = !checkUtils[7]
            }
        }
        kitchen.setOnClickListener {
            if (checkUtils[8]) {
                kitchen.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[8] = !checkUtils[8]
            } else {
                kitchen.setChipBackgroundColorResource(R.color.selected)
                checkUtils[8] = !checkUtils[8]
            }
        }
        bed.setOnClickListener {
            if (checkUtils[9]) {
                bed.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[9] = !checkUtils[9]
            } else {
                bed.setChipBackgroundColorResource(R.color.selected)
                checkUtils[9] = !checkUtils[9]
            }
        }
        television.setOnClickListener {
            if (checkUtils[10]) {
                television.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[10] = !checkUtils[10]
            } else {
                television.setChipBackgroundColorResource(R.color.selected)
                checkUtils[10] = !checkUtils[10]
            }
        }
        closet.setOnClickListener {
            if (checkUtils[11]) {
                closet.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[11] = !checkUtils[11]
            } else {
                closet.setChipBackgroundColorResource(R.color.selected)
                checkUtils[11] = !checkUtils[11]
            }
        }
        mezzanine.setOnClickListener {
            if (checkUtils[12]) {
                mezzanine.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[12] = !checkUtils[12]
            } else {
                mezzanine.setChipBackgroundColorResource(R.color.selected)
                checkUtils[12] = !checkUtils[12]
            }
        }
        camera.setOnClickListener {
            if (checkUtils[13]) {
                camera.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[13] = !checkUtils[13]
            } else {
                camera.setChipBackgroundColorResource(R.color.selected)
                checkUtils[13] = !checkUtils[13]
            }
        }
        security_man.setOnClickListener {
            if (checkUtils[14]) {
                security_man.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[14] = !checkUtils[14]
            } else {
                security_man.setChipBackgroundColorResource(R.color.selected)
                checkUtils[14] = !checkUtils[14]
            }
        }
        pet.setOnClickListener {
            if (checkUtils[15]) {
                pet.setChipBackgroundColorResource(R.color.background_chip)
                checkUtils[15] = !checkUtils[15]
            } else {
                pet.setChipBackgroundColorResource(R.color.selected)
                checkUtils[15] = !checkUtils[15]
            }
        }
    }

    override fun setPresenter(presenter: FilterContract.Presenter) {
        this.presenter = presenter
    }

    override fun show(isShow: Boolean) {
        layout_filter.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun setSpinnerDistrict(districts: LinkedList<String>) {
        spinnerDistrict.attachDataSource(districts)
    }

    override fun onPause() {
        super.onPause()
        System.gc()
        Runtime.getRuntime().gc()
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}