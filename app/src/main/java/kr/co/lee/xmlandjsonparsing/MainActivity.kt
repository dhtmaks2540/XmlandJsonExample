package kr.co.lee.xmlandjsonparsing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Xml
import android.widget.Button
import android.widget.TextView
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    lateinit var pullBtn: Button
    lateinit var jsonBtn: Button
    lateinit var resultView: TextView
    lateinit var cityView: TextView
    lateinit var temperatureView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pullBtn = findViewById(R.id.pull_btn)
        jsonBtn = findViewById(R.id.json_btn)
        resultView = findViewById(R.id.result_title)
        cityView = findViewById(R.id.city_view)
        temperatureView = findViewById(R.id.temperature_view)

        pullBtn.setOnClickListener {
            pullParsing()
        }

        jsonBtn.setOnClickListener {
            jsonParsing()
        }
    }

    // Pull Parsing 메서드
    private fun pullParsing() {
        resultView.text = "Pull Parsing Result"

        try {
            // XmlPullParser 객체 생성
            val parser: XmlPullParser = Xml.newPullParser()
            // assets 폴더안에 있는 XML 파일을 열어 InputStream 객체 생성
            val inputStream: InputStream = assets.open("test.xml")
            // inputStream 지정
            parser.setInput(inputStream, null)

            // getEventType() 메서드를 사용해 이벤트 타입을 얻어오는데
            // 처음으로 호출되므로 START_DOCUMENT가 호출
            var eventType = parser.eventType
            var done = false

            // eventType이 End_Document가 아니고 done이 false일 경우 반복
            while(eventType != XmlPullParser.END_DOCUMENT && !done) {
                // 이름을 저장하기 위한 변수
                var name: String? = null
                // eventType이 START_TAG인 경우
                if(eventType == XmlPullParser.START_TAG) {
                    // getName() 메서드를 사용해 현재 요소(태그)의 이름을 반환
                    name = parser.name
                    // 태그의 이름이 city인 경우
                    if(name == "city") {
                        // getAttributeValue를 사용해 첫 번째 매개변수 및
                        // 두 번째 매개변수로 식별된 특성 값 획득
                        cityView.text = parser.getAttributeValue(null, "name")
                    } else if(name == "temperature") {
                        temperatureView.text = parser.getAttributeValue(null, "value")
                    }
                }
                // 다음 이벤트로 넘기기
                eventType = parser.next()
            }
        } catch(e: Exception) {

        }
    }

    private fun jsonParsing() {
        resultView.text = "JSON Parsing Result"
        // 파일 읽기
        var json: String? = null

        try {
            val inputStream = assets.open("test.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, charset("UTF-8"))

            val jsonRoot = JSONObject(json)

            cityView.text = jsonRoot.getString("name")
            val main = jsonRoot.getJSONObject("main")
            temperatureView.text = main.getString("temp")
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }
}