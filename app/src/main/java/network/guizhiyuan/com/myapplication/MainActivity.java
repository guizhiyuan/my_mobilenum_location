package network.guizhiyuan.com.myapplication;

import android.accounts.NetworkErrorException;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText;
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.text_view);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    Handler handler = new Handler();

    @Override
    public void onClick(View v) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    String url = "http://apis.baidu.com/showapi_open_bus/mobile/find";
                    String num = editText.getText().toString().trim();
                    url = url + "?num=" + num;
                    URL mURL = new URL(url);
                    conn = (HttpURLConnection) mURL.openConnection();
                    conn.setRequestProperty("apikey", "baac70abb76e42be97ac60f6080f68e8");
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(10000);
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {

                        InputStream is = conn.getInputStream();
                        final String response = getStringFromInputStream(is);
                        Log.i("MobileActivity", response);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(response);
                            }
                        });
                    } else {
                        throw new NetworkErrorException("response status is" + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }


    private static String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        String state = os.toString();
        is.close();
        return state;
    }
}