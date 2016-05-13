package com.szbb.pro.entity.login;

import com.szbb.pro.entity.base.BaseBean;

/**
 * Created by ChanZeeBm on 2015/12/26.
 */
public class AuthBean extends BaseBean {

    /**
     * auth :
     * ADAPMAw2V2RRMQAyB2VQYVRlUWUFYwcwVT1QYwZmA2MBPg5kBmFTMFdoUjBXbQY4BTcLOAA1AmcDOFZhAmADZAAODz4MPQ==
     * is_complete_info : 1
     * is_check : 0
     */

    private String auth;
    private int is_complete_info;
    private int is_check;
    private String userId;

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public int getIs_complete_info() {
        return is_complete_info;
    }

    public void setIs_complete_info(int is_complete_info) {
        this.is_complete_info = is_complete_info;
    }

    public int getIs_check() {
        return is_check;
    }

    public void setIs_check(int is_check) {
        this.is_check = is_check;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
