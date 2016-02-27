package com.szbb.pro.tools;

import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/10/17.
 */
public class UITools {
    private static List<FragmentActivity> activities = new ArrayList<>();

    public static void removeAllActivities() {
        for (FragmentActivity activity : activities) {
            if (!activity.isFinishing())
                activity.finish();
            activities.remove(activity);
        }
        activities.clear();
    }

    public static void removeSingleActivity(FragmentActivity fragmentActivity) {
        fragmentActivity.finish();
        for (FragmentActivity activity : activities) {
            if (activities.contains(fragmentActivity)) {
                LogTools.w("finishedActivity:" + activity.getClass().getSimpleName());
                activities.remove(activity);
                break;
            }
        }
    }

    public static void addActivity(FragmentActivity fragmentActivity) {
        if (!activities.contains(fragmentActivity))
            activities.add(fragmentActivity);
    }
}
