package com.szbb.pro.factory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by KenChan on 16/7/8.
 */
public abstract class Mapper {

    public abstract Mapper moveToSpecialPoint(@NonNull View view, double lat, double lng);

    public abstract Mapper locate(Context context, LocationListener locationListener);


}
