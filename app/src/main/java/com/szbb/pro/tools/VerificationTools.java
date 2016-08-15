package com.szbb.pro.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;

/**
 * Created by ChanZeeBm on 2015/10/15.
 */
public class VerificationTools {

    public static boolean verifyPhone(Context context, @NonNull View view, @NonNull String phone) {
        if (!phone.matches(AppKeyMap.PHONE_REGEX)) {
            AppTools.showNormalSnackBar(view, context.getString(R.string.invalid_user));
        }
        return phone.matches(AppKeyMap.PHONE_REGEX);
    }

    public static boolean verifyPwd(Context context, @NonNull View view, @NonNull String... pwd) {
        if (pwd.length == 0) {
            return false;
        }
        if (pwd.length == 1) {
            boolean length = pwd[0].length() >= 6 && pwd[0].length() <= 20;
            if (!length) {
                AppTools.showNormalSnackBar(view, context.getString(R.string.invalid_pwd_length));
                return false;
            }
            return pwd[0].length() >= 6 && pwd[0].length() <= 20 && pwd[0].matches(AppKeyMap
                    .PWD_REGEX);
        } else {
            return verifyPwd(context, view, pwd[0], pwd[1]);
        }
    }

    private static boolean verifyPwd(Context context, @NonNull View view, @NonNull String pwd,
                                     @NonNull
                                     String confirm) {
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(confirm)) {
            AppTools.showNormalSnackBar(view, context.getString(R.string.pwd_not_null));
            return false;
        }
        if (!(pwd.length() >= 6 && pwd.length() <= 16)) {
            AppTools.showNormalSnackBar(view, context.getString(R.string.invalid_pwd_length));
            return false;
        }
        if (!pwd.equals(confirm)) {
            AppTools.showNormalSnackBar(view, context.getString(R.string.pwd_not_match));
            return false;
        }
        if (!(pwd.matches(AppKeyMap.PWD_REGEX)) || !(confirm.matches(AppKeyMap.PWD_REGEX))) {
            AppTools.showNormalSnackBar(view, context.getString(R.string.invalid_pwd));
            return false;
        }
        return true;
    }

    public static boolean verifyCitizenID(Context context, @NonNull View view, @NonNull String
            citizen) {
        if (!citizen.matches(AppKeyMap.CITIZEN_ID_REGEX)) {
            AppTools.showNormalSnackBar(view, context.getString(R.string.invalid_citizenID));
        }
        return citizen.matches(AppKeyMap.CITIZEN_ID_REGEX);
    }

    public static boolean verifyChineseName(Context context, @NonNull View view, @NonNull String
            citizen) {
        if (!citizen.matches(AppKeyMap.CHINESE_PEOPLE_REGEX)) {
            AppTools.showNormalSnackBar(view, context.getString(R.string.invalid_name));
        }
        return citizen.matches(AppKeyMap.CHINESE_PEOPLE_REGEX);
    }

    public static boolean verifyVerificationCode(Context context, @NonNull View view, @NonNull
    String verifyCode) {
        if (verifyCode.length() < 6) {
            AppTools.showNormalSnackBar(view, context.getString(R.string.invalid_code));
            return false;
        }
        return true;
    }

    public static boolean verifyZipCode(Context context, @NonNull View view, @NonNull
    String zipCode) {

        return true;
    }

}
