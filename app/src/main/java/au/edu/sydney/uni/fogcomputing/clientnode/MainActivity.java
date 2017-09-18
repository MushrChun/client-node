package au.edu.sydney.uni.fogcomputing.clientnode;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button uploadBtn;
    private TextView display;
    private TextView fileMonitor;

    private String url = "http://10.66.31.47:8080/task";
    private AssetManager assetManager;
    private OkHttpClient client;
    private String fileContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new OkHttpClient();
        display = (TextView) findViewById(R.id.display);
        uploadBtn = (Button) findViewById(R.id.button);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Context context = getApplicationContext();
                final int duration = Toast.LENGTH_SHORT;
                view.setEnabled(false);
                new FileUploadTask(fileContent).execute();
            }
        });

        fileContent = loadKmeansData(getApplicationContext());

        fileMonitor = (TextView) findViewById(R.id.fileMonitor);
        fileMonitor.setText(fileContent);


    }

    private String doFileUpload(String uploadContent){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "filename", RequestBody.create(MediaType.parse("plain/text"), fileContent))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.body().toString();
    }

    private String loadKmeansData(Context context){
        String fileName = "kmeans_data.txt";
        assetManager = context.getAssets();
        InputStream inputStream;
        try {
            inputStream = assetManager.open(fileName);
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String content = scanner.hasNext()? scanner.next() : "";
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class FileUploadTask extends AsyncTask<Void, Void, String> {

        private String content;

        public FileUploadTask(String content){
            this.content = content;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return doFileUpload(content);
        }

        @Override
        protected void onPostExecute(String result){
            display.setText(result);
            uploadBtn.setEnabled(true);
        }
    }

}
