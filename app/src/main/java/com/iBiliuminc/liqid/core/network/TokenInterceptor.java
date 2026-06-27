package com.iBiliuminc.liqid.core.network;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    private final SharedPreferences prefs;

    public TokenInterceptor(Context context) {
        this.prefs = context.getSharedPreferences("liqid_prefs", Context.MODE_PRIVATE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = prefs.getString("auth_token", null);
        if (token != null) {
            Request request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(request);
        }
        return chain.proceed(chain.request());
    }
}
