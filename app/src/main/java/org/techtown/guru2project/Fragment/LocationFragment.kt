package org.techtown.guru2project.Fragment

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_date.*
import kotlinx.android.synthetic.main.fragment_location.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.email
import org.techtown.guru2project.R
import org.techtown.guru2project.SettingActivity
import org.w3c.dom.Element
import java.io.IOException
import java.util.concurrent.CountDownLatch
import javax.xml.parsers.DocumentBuilderFactory


/**
 * A simple [Fragment] subclass.
 */

private const val REQUEST_ACCESS_FINE_LOCATION = 1000

var myLatitude = 0.0
var myLongitude = 0.0
var targetLatitude = 0.0
var targetLongitude = 0.0
var diffLatitude = 0.0
var diffLongitude = 0.0

class LocationFragment : Fragment(), OnMapReadyCallback{
    val serviceKey = "nynKQpN7ybxaSAstA9pWvReQOXQ9pP9ENPUKE%2BmoT%2BOCmvTMUtMhFQNoosQ9sMNvRMGK43nNWoTIcdDFZkUHkg%3D%3D"
    private lateinit var mMap: GoogleMap    // 1
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: MyLocationCallBack
    private var firestore: FirebaseFirestore? = null

    var email:String = ""
    var name:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // API 이용해서 검색한 장소의 위도, 경도 받아오는 코드 //
        super.onViewCreated(view, savedInstanceState)
        searchBtn.setOnClickListener{

            val act = activity as SettingActivity
            email = act.getEmail()
            name = act.getName()

            //val positions: List<Float>
            if(enSearch.text.isNotEmpty()){
                try{
                    resultTxt.text = getData(serviceKey, enSearch.text.toString())
                }catch(e: Exception){
                    resultTxt.text = e.message
                }
            }
            diffLatitude = LatitudeInDifference(300)
            diffLongitude = LongitudeInDifference(targetLatitude, 300)

            viewLat.setText("${targetLatitude-diffLatitude} ~ ${targetLatitude+diffLatitude}")
            viewLong.setText("${myLongitude-diffLongitude} ~ ${myLongitude+diffLongitude}")

            calculation()
        }
        /* 나의 현재 위도, 경도 가져오는 코드*/
        locationInit()
    }

    private fun getData(serviceKey: String, name: String): String{
        var positions = mutableListOf<Array<Float>>()
        val request_p = getRequestUrl_p(serviceKey, name)
        val request_h = getRequestUrl_h(serviceKey, name)
        val client = OkHttpClient()
        val itemList: ArrayList<HashMap<String, String>> = ArrayList()

        var result:String=""
        var place:String=""

        /*

         */
        var lanch1 = CountDownLatch(1)
        client.newCall(request_p).enqueue(object: okhttp3.Callback{
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                lanch1.countDown()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val body = response.body?.string()?.byteInputStream()
                val buildFactory = DocumentBuilderFactory.newInstance()
                val docBuilder = buildFactory.newDocumentBuilder()
                val doc = docBuilder.parse(body, null)
                val nList = doc.getElementsByTagName("item")

                for(n in 0 until nList.length) {
                    val element = nList.item(n) as Element
                    val dataHashMap = HashMap<String, String>()
                    dataHashMap.put("addr", getValueFromKey(element, "addr"))
                    dataHashMap.put("clCd", getValueFromKey(element, "clCd"))
                    dataHashMap.put("clCdNm", getValueFromKey(element, "clCdNm"))
                    dataHashMap.put("estbDd", getValueFromKey(element, "estbDd"))
                    dataHashMap.put("postNo", getValueFromKey(element, "postNo"))
                    dataHashMap.put("sgguCd", getValueFromKey(element, "sgguCd"))
                    dataHashMap.put("sgguCdNm", getValueFromKey(element, "sgguCdNm"))
                    dataHashMap.put("sidoCd", getValueFromKey(element, "sidoCd"))
                    dataHashMap.put("sidoCdNm", getValueFromKey(element, "sidoCdNm"))
                    dataHashMap.put("telno", getValueFromKey(element, "telno"))
                    dataHashMap.put("XPos", getValueFromKey(element, "XPos"))
                    dataHashMap.put("YPos", getValueFromKey(element, "YPos"))
                    dataHashMap.put("yadmNm", getValueFromKey(element, "yadmNm"))

                    itemList.add(dataHashMap)
                }
                lanch1.countDown()
            }
        })
        lanch1.await()

        var lanch2 = CountDownLatch(1)
        client.newCall(request_h).enqueue(object : okhttp3.Callback {

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response){
                val body = response.body?.string()?.byteInputStream()
                val buildFactory = DocumentBuilderFactory.newInstance()
                val docBuilder = buildFactory.newDocumentBuilder()
                val doc = docBuilder.parse(body, null)
                val nList = doc.getElementsByTagName("item")

                for (n in 0 until nList.length) {
                    val element = nList.item(n) as Element
                    val dataHashMap = HashMap<String, String>()
                    dataHashMap.put("addr", getValueFromKey(element, "addr"))
                    dataHashMap.put("clCd", getValueFromKey(element, "clCd"))
                    dataHashMap.put("clCdNm", getValueFromKey(element, "clCdNm"))
                    dataHashMap.put("drTotCnt", getValueFromKey(element, "drTotCnt"))
                    dataHashMap.put("estbDd", getValueFromKey(element, "estbDd"))
                    dataHashMap.put("gdrCnt", getValueFromKey(element, "gdrCnt"))
                    dataHashMap.put("hospUrl", getValueFromKey(element, "hospUrl"))
                    dataHashMap.put("intnCnt", getValueFromKey(element, "intnCnt"))
                    dataHashMap.put("postNo", getValueFromKey(element, "postNo"))
                    dataHashMap.put("resdntCnt", getValueFromKey(element, "resdntCnt"))
                    dataHashMap.put("sdrCnt", getValueFromKey(element, "sdrCnt"))
                    dataHashMap.put("sgguCd", getValueFromKey(element, "sgguCd"))
                    dataHashMap.put("sgguCdNm", getValueFromKey(element, "sgguCdNm"))
                    dataHashMap.put("sidoCd", getValueFromKey(element, "sidoCd"))
                    dataHashMap.put("sidoCdNm", getValueFromKey(element, "sidoCdNm"))
                    dataHashMap.put("telno", getValueFromKey(element, "telno"))
                    dataHashMap.put("XPos", getValueFromKey(element, "XPos"))
                    dataHashMap.put("YPos", getValueFromKey(element, "YPos"))
                    dataHashMap.put("yadmNm", getValueFromKey(element, "yadmNm"))
                    dataHashMap.put("ykiho", getValueFromKey(element, "ykiho"))

                    itemList.add(dataHashMap)
                }

                //위도 경도 값을 숫자로 바꾸고 장소의 이름 값 저장
                for (n in 0 until itemList.size){
                    var position = toNum(itemList[n].get("XPos").toString(), itemList[n].get("YPos").toString()) //x, y좌표를 배열에 추가

                    positions.add(position)
                    place = itemList[n].get("yadmNm").toString()
                }

                for(n in 0 until positions.size){
                    result = result+"["+positions[n][0]+", "+positions[n][1]+"] "
                    targetLatitude = positions[n][1].toDouble()
                    targetLongitude = positions[n][0].toDouble()
                }
                lanch2.countDown()
            }

            //실패한 경우
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                val body = e.message
                resultTxt.text = body
                lanch2.countDown()
            }
        })
        lanch2.await()

        //DB에 위치 정보를 업데이트
        setTargetLocation(targetLatitude, targetLongitude, place)


        //위도 경도 넘겨주기
        return result


    }

    private fun getRequestUrl_p(serviceKey: String,
                                name: String) : Request {

        var url = "http://apis.data.go.kr/B551182/pharmacyInfoService/getParmacyBasisList"
        var httpUrl = url.toHttpUrlOrNull()
            ?.newBuilder()
            ?.addEncodedQueryParameter("serviceKey", serviceKey)
            ?.addEncodedQueryParameter("pageNo", "1")
            ?.addEncodedQueryParameter("numOfRows", "10")
            ?.addQueryParameter("yadmNm", name)
            ?.build()

        return Request.Builder()
            .url(httpUrl!!)
            .addHeader("Content-Type",
                "application/x-www-form-urlencoded; text/xml; charset=utf-8")
            .build()
    }
    private fun getRequestUrl_h(serviceKey: String,
                                name: String) : Request {

        var url = "http://apis.data.go.kr/B551182/hospInfoService/getHospBasisList"
        var httpUrl = url.toHttpUrlOrNull()
            ?.newBuilder()
            ?.addEncodedQueryParameter("serviceKey", serviceKey)
            ?.addEncodedQueryParameter("pageNo", "1")
            ?.addEncodedQueryParameter("numOfRows", "10")
            ?.addQueryParameter("yadmNm", name)
            ?.build()

        return Request.Builder()
            .url(httpUrl!!)
            .addHeader("Content-Type",
                "application/x-www-form-urlencoded; text/xml; charset=utf-8")
            .build()
    }



    private fun getValueFromKey(element: Element, key:String):String{
        return element.getElementsByTagName(key).item(0).firstChild.nodeValue
    }

    //위도 경도 데이터를 숫자 배열로 변경
    private fun toNum(x:String, y:String): Array<Float>{
        val xPos = x.toFloat()
        val yPos = y.toFloat()
        val position = arrayOf(xPos,yPos)
        return position
    }

    // 위치 정보를 얻기 위한 각종 초기화
    private fun locationInit() {
        fusedLocationProviderClient = FusedLocationProviderClient(activity!!)

        locationCallback = MyLocationCallBack()

        locationRequest = LocationRequest()
        // GPS 우선
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        // 업데이트 인터벌
        // 위치 정보가 없을 때는 업데이트 안 함
        // 상황에 따라 짧아질 수 있음, 정확하지 않음
        // 다른 앱에서 짧은 인터벌로 위치 정보를 요청하면 짧아질 수 있음
        locationRequest.interval = 10000
        // 정확함. 이것보다 짧은 업데이트는 하지 않음
        locationRequest.fastestInterval = 5000
    }

    /**
     * 사용 가능한 맵을 조작합니다.
     * 지도가 사용될 준비가 되면 이 콜백이 호출됩니다.
     * 여기서 마커나 선을 추가하거나 청취자를 추가하거나 카메라를 이동할 수 있습니다.
     * 호주 시드니 근처에 표식을 추가하고 있습니다.
     * Google Play 서비스가 기기에 설치되어 있지 않으면 사용자에게 설치하라는 메시지가 표시됩니다.
     * SupportMapFragment 안에 있습니다. 이 메소드는 한 번만 호출됩니다.
     * Google Play 서비스가 설치되고 앱으로 돌아 옵니다.
     */

    override fun onMapReady(p0: GoogleMap?) {

        // 권한 요청
        permissionCheck(cancel = {
            showPermissionInfoDialog()
        }, ok = {
            mMap.isMyLocationEnabled = true
        })
    }

    override fun onResume() {
        super.onResume()

        // 권한 요청 ⑨
        permissionCheck(cancel = {
            // 위치 정보가 필요한 이유 다이얼로그 표시 ⑩
            showPermissionInfoDialog()
        }, ok = {
            // 현재 위치를 주기적으로 요청 (권한이 필요한 부분) ⑪
            addLocationListener()
        })
    }

    private fun addLocationListener() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
            locationCallback,
            null)
    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(context).apply {
            setTitle("권한이 필요한 이유")
            setMessage("현재 위치 정보를 얻기 위해서는 위치 권한이 필요합니다")
            setPositiveButton("Yes") { dialog, which ->
                // 권한 요청
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_ACCESS_FINE_LOCATION
                )
            }
            setNegativeButton("Cancel", null)
        }.show()
    }

    override fun onPause() {
        super.onPause()
        // ①
        removeLocationListener()
    }

    private fun removeLocationListener() {
        // 현재 위치 요청을 삭제 ②
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty()
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // 권한 허용됨
                    addLocationListener()
                } else {
                    // 권한 거부
                    Toast.makeText(context!!,"권한 거부 됨",Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun permissionCheck(cancel: () -> Unit, ok: () -> Unit) {
        // 위치 권한이 있는지 검사
        if (ContextCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 허용되지 않음
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 이전에 권한을 한 번 거부한 적이 있는 경우에 실행할 함수
                cancel()
            } else {
                // 권한 요청
                ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_ACCESS_FINE_LOCATION)
            }
        } else {
            // 권한을 수락 했을 때 실행할 함수
            ok()
        }
    }

    inner class MyLocationCallBack : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val location = locationResult?.lastLocation

            location?.run {
                // 14 level로 확대하며 현재 위치로 카메라 이동
                val latLng = LatLng(latitude, longitude)

                textView2.text = "나의 위도: $latitude, 나의 경도: $longitude"
                myLatitude = latitude
                myLongitude = longitude
            }

        }
    }

    // 반경 m 이내의 latitude, longitude degree 차이 계산
    // 반경 m이내의 latitude degree 차이
    public fun LatitudeInDifference(diff: Int): Double {
        val earthRadius: Int = 6371000 // 지구의 반지름. 단위 m
        return (diff*360.0) / (2*Math.PI*earthRadius)
    }

    // 반경 m이내의 longitude degree 차이
    public fun LongitudeInDifference(latitude:Double, diff:Int): Double{
        val earthRadius: Int = 6371000 // 지구의 반지름. 단위 m
        var result:Double = (diff*360.0)/(2*Math.PI*earthRadius*Math.cos(Math.toRadians(latitude)))

        return (result)
    }

    // 실시간으로 내가 반경 안에 있는지 확인
    private fun calculation(){
        if ((targetLatitude- diffLatitude)<= myLatitude
            && myLatitude <= (targetLatitude+ diffLatitude)
            && (targetLongitude- diffLongitude) <= myLongitude
            && myLongitude<=(targetLongitude+ diffLongitude)){
            Toast.makeText(context, "target 가까이에 왔습니다.", Toast.LENGTH_LONG).show()
        } else{
            Toast.makeText(context, "target 반경 내에 있지 않습니다.", Toast.LENGTH_LONG).show()
        }

    }

    //DB에 장소의 위도, 경도 정보 업데이트
    private fun setTargetLocation(latitude: Double, longitude: Double, place:String){
        var map = mutableMapOf<String, Any>()
        map["latitude"] = latitude
        map["longitude"] = longitude
        map["place"] = place
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("$email")?.document("$name")?.update(map)
            ?.addOnCompleteListener { task ->
                if(!task.isSuccessful){
                    textView.text = task.exception?.message.toString()
                }
            }

    }
}

