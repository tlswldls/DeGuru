package org.techtown.guru2project.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.fragment_location.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.techtown.guru2project.R
import org.w3c.dom.Element
import java.io.IOException
import java.util.concurrent.CountDownLatch
import javax.xml.parsers.DocumentBuilderFactory


/**
 * A simple [Fragment] subclass.
 */

private const val REQUEST_ACCESS_FINE_LOCATION = 1000

class LocationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchBtn.setOnClickListener{
            //val positions: List<Float>
            if(enSearch.text.isNotEmpty()){
                try{
                    resultTxt.text = getData(serviceKey, enSearch.text.toString())
                }catch(e: Exception){
                    resultTxt.text = e.message
                }

            }
        }

    }

    private fun getData(serviceKey: String, name: String): String{
        var positions = mutableListOf<Array<Float>>()
        val request_p = getRequestUrl_p(serviceKey, name)
        val request_h = getRequestUrl_h(serviceKey, name)
        val client = OkHttpClient()
        val itemList: ArrayList<HashMap<String, String>> = ArrayList()

        var result:String=""

        /*

         */
        var lanch1 = CountDownLatch(1)
        client.newCall(request_p).enqueue(object: okhttp3.Callback{
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                val body = e.message
                lanch1.countDown()
                //Toast.makeText(this, body, Toast.LENGTH_LONG).show()
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

                //위도 경도 값을 숫자로 바꾸기
                for (n in 0 until itemList.size){
                    var position = toNum(itemList[n].get("XPos").toString(), itemList[n].get("YPos").toString()) //x, y좌표를 배열에 추가

                    positions.add(position)
                }

                for(n in 0 until positions.size){
                        result = result+"["+positions[n][0]+", "+positions[n][1]+"] "
                }
                lanch2.countDown()
                    //resultTxt.text = result
            }

            //실패한 경우
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                val body = e.message
                resultTxt.text = body
                lanch2.countDown()
            }
        })
        lanch2.await()

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

    private fun toNum(x:String, y:String): Array<Float>{
        val xPos = x.toFloat()
        val yPos = y.toFloat()
        val position = arrayOf(xPos,yPos)
        return position
    }

    /* 나의 GPS 받아오기 */

}