package au.edu.sydney.uni.fogcomputing.clientnode;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private Button uploadBtn;
    private TextView display;

    private RequestQueue queue;
    private String url = "http://10.66.31.47:8080/handler";
    private int LONG_SOCKET_TIMEOUT_MS = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        display = (TextView) findViewById(R.id.display);
        uploadBtn = (Button) findViewById(R.id.button);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Context context = getApplicationContext();
//                CharSequence text = "uploaded!";
                final int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
                view.setEnabled(false);

                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                display.setText(response);
                                Toast toast = Toast.makeText(context, "responded: "+ response, duration);
                                toast.show();
                                view.setEnabled(true);
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                display.setText(error.getMessage());
                            }
                        }
                );
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        LONG_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
                queue.add(stringRequest);

            }
        });
    }
}
