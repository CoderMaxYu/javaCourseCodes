package nio;

import java.io.IOException;

import okhttp3.*;

public class OkHttpUtils {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    /**
     * okhttp get url
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    /**
     * okhttp post json
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String post(String url,String json) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void main(String[] args) throws IOException{
//        System.out.println(get("http://localhost:8801"));
        System.out.println(post("http://localhost:8801",""));
    }
}
