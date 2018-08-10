package gggroup.com.baron.post

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import gggroup.com.baron.R
import gggroup.com.baron.adapter.ImageAdapter
import gggroup.com.baron.tensorflow.Classifier
import gggroup.com.baron.tensorflow.TensorFlowImageClassifier
import kotlinx.android.synthetic.main.activity_post.*
import java.io.File
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class PostActivity : AppCompatActivity(), PostContract.View {
    private val INPUT_SIZE = 224
    private val IMAGE_MEAN = 128
    private val IMAGE_STD = 128.0f
    private val INPUT_NAME = "input"
    private val OUTPUT_NAME = "final_result"

    private val MODEL_FILE = "file:///android_asset/graph.pb"
    private val LABEL_FILE = "file:///android_asset/labels.txt"
    private var topResultConfidence: Float? = 0.0f
    private var classifier: Classifier? = null
    private var executor = Executors.newSingleThreadExecutor()


    private var presenter: PostContract.Presenter? = null
    private var mAdapter: ImageAdapter? = null
    private var images: ArrayList<Image> = ArrayList()
    private var types = BooleanArray(2)
    private var checkUtils = BooleanArray(16)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        presenter = PostPresenter(this)

        setSupportActionBar(toolbar)
        initTensorFlowAndLoadModel()
        upload_picture.setOnClickListener {
            getImage()
        }
        Arrays.fill(types, false)

        Arrays.fill(checkUtils, false)
        val sex = arrayListOf("Nam", "Nữ", "Nam/Nữ")
        spinnerSex.attachDataSource(sex)
        val city = arrayListOf("Hà Nội", "Hồ Chí Minh")
        spinnerProvince.attachDataSource(city)
        presenter?.getAllDistrict()
        spinnerProvince.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                presenter?.getDistrict(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        })
        onClick()
        post.setOnClickListener {
            post()
        }
        chip_compound.setChipBackgroundColorResource(R.color.selected)
        types[1] = !types[1]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_close -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

    override fun getImage() {
        val imagePicker = ImagePicker.create(this)
                .returnMode(ReturnMode.NONE) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true)
                .toolbarFolderTitle("Bộ sưu tập") // folder selection title
                .toolbarImageTitle("Chọn ảnh")
                .toolbarDoneButtonText("XONG") // done button text
        imagePicker.multi()
                .limit(5) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .origin(images) // original selected images, used in multi mode
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .imageFullDirectory(Environment.getExternalStorageDirectory().path) // can be full path
                .start() // start image picker activity with request code
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            images = ImagePicker.getImages(data) as ArrayList<Image>
            displayImg(images)
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun displayImg(images: ArrayList<Image>?) {
        if (images == null) return
        mAdapter = ImageAdapter(images, this)
        recycler_view.adapter = mAdapter
        val gridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recycler_view.layoutManager = gridLayoutManager
        recycler_view.isNestedScrollingEnabled = false
    }

    override fun showNotification(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun post() {
        var category = "other"
        if (images.size != 0) {
            val mExecutor = Executors.newFixedThreadPool(3)
            mExecutor.execute {
                category = getCategory(images[0])
            }

            mExecutor.shutdown()
            mExecutor.awaitTermination(java.lang.Long.MAX_VALUE, TimeUnit.DAYS)
        }
        when {
            edt_title.text.isEmpty() -> {
                showNotification("Vui lòng nhập tiêu đề bài đăng")
                edt_title.requestFocus()
            }
            edt_price.text.isEmpty() -> {
                showNotification("Vui lòng nhập giá phòng")
                edt_price.requestFocus()
            }
            edt_area.text.isEmpty() -> {
                showNotification("Vui lòng nhập diện tích phòng")
                edt_area.requestFocus()
            }
            edt_address.text.isEmpty() -> {
                showNotification("Vui lòng nhập số nhà, tên đường, tên phường")
                edt_address.requestFocus()
            }
            edt_phone.text.isEmpty() -> {
                showNotification("Vui lòng nhập số điện thoại")
                edt_phone.requestFocus()
            }
            images.size < 1 -> showNotification("Vui lòng thêm ảnh mô tả phòng")
            else -> {
                val title = edt_title.text.toString()
                val price = (edt_price.text.toString().toFloat()) / 1000000
                val area = edt_area.text.toString().toFloat()
                val description = edt_describe.text.toString()
                val phone = edt_phone.text.toString()
                val type: Int = when {
                    types[0] -> 0
                    types[1] -> 1
                    else -> 2
                }
                val utils: ArrayList<String> = ArrayList()
                val city = spinnerProvince.text.toString()
                val district = spinnerDistrict.text.toString()
                val address = edt_address.text.toString()
                val listFile: ArrayList<File>? = ArrayList()
                if (images.size > 0) {
                    for (i in 0 until images.size)
                        listFile?.add(File(images[0].path))
                }
                val nameOfUtils: ArrayList<String> = arrayListOf("Máy lạnh", "Máy giặt", "Tủ lạnh", "WC riêng", "Chổ để xe",
                        "Wifi", "Giờ giấc tự do", "Không chung chủ", "Bếp", "Giường ngủ",
                        "Tivi", "Tủ quần áo", "Gác lửng", "Camera", "Bảo vệ", "Thú cưng")
                var count = 0
                for (i in 0 until checkUtils.size) {
                    if (checkUtils[i]) {
                        //val myUtils = RequestBody.create(MediaType.parse("text/plain"), nameOfUtils[i])
                        utils.add(count, nameOfUtils[i])
                        count++
                    }
                }
                val sex = spinnerSex.selectedIndex
                val quantity = amountPeople.text.toString().toInt()
                presenter?.post(title, price, area, description, phone,
                        type, sex, quantity, utils, city, district, address, listFile, category)
            }
        }
    }

    override fun setPresenter(presenter: PostContract.Presenter) {
        this.presenter = presenter
    }

    override fun show(isShow: Boolean) {
        layout_post.visibility = if (isShow) View.VISIBLE else View.GONE
        progress_bar.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    override fun onResponse(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun setSpinnerDistrict(districts: LinkedList<String>) {
        spinnerDistrict.attachDataSource(districts)
    }

    override fun isPost(isPost: Boolean) {
        progress_bar.visibility = if (isPost) View.VISIBLE else View.GONE
        post.isEnabled = !isPost
        if (!isPost) {
            finish()
        }
    }

    private fun initTensorFlowAndLoadModel() {
        executor.execute {
            try {
                classifier = TensorFlowImageClassifier.create(
                        this@PostActivity.assets,
                        MODEL_FILE,
                        LABEL_FILE,
                        INPUT_SIZE,
                        IMAGE_MEAN,
                        IMAGE_STD,
                        INPUT_NAME,
                        OUTPUT_NAME)
            } catch (e: Exception) {
                throw RuntimeException("Error initializing TensorFlow!", e)
            }
        }
    }

    override fun getCategory(image: Image): String {
        var category = "other"
        var bitmap = BitmapFactory.decodeFile(image.path)
        val mExecutor = Executors.newFixedThreadPool(2)
        mExecutor.execute {
            bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)
        }
        mExecutor.shutdown()
        mExecutor.awaitTermination(java.lang.Long.MAX_VALUE, TimeUnit.DAYS)
        val results: List<Classifier.Recognition>? = classifier?.recognizeImage(bitmap)
        if (results != null) {
            category = results[0].title!!
            topResultConfidence = results[0].confidence
            if (topResultConfidence!! < 0.5) {
                category = "other"
            }
        }
        return category
    }


    override fun onDestroy() {
        super.onDestroy()
        System.gc()
        Runtime.getRuntime().gc()
        finish()
    }
}