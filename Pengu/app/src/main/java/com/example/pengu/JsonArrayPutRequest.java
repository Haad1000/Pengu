package com.example.pengu;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import org.json.JSONArray;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;

// after put i called it exist the app change it after.
public class JsonArrayPutRequest extends Request<JSONArray>{
    private final Response.Listener<JSONArray> listener;
    private final JSONArray requestBody;

    public JsonArrayPutRequest(String url, JSONArray requestBody, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(Request.Method.PUT, url, errorListener);
        this.listener = listener;
        this.requestBody = requestBody;
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        listener.onResponse(response);
    }

    @Override
    public byte[] getBody() {
        return requestBody == null ? null : requestBody.toString().getBytes();
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}


