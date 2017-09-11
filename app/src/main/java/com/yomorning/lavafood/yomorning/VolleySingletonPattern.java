package com.yomorning.lavafood.yomorning;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by KAMAL OLI on 05/08/2017.
 */

public class VolleySingletonPattern {
    private static VolleySingletonPattern instance;
    private static RequestQueue requestQueue;
    private static Context context;
    private VolleySingletonPattern(Context cntx){
        context=cntx;
        requestQueue=getRequestQueue();
    }
    public static synchronized VolleySingletonPattern getInstance(Context context){
        if(instance==null){
            instance=new VolleySingletonPattern(context);
            requestQueue=getRequestQueue();
        }
        return instance;
    }
    public static RequestQueue getRequestQueue() {
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
