package com.softeng.quickcash;

import android.content.Context;
import android.content.Intent;

public class NewActivity {

    public static void goToActivity(Context context, Class<?> targetClass){
        Intent intent = new Intent(context, targetClass);
        context.startActivity(intent);
    }
}
