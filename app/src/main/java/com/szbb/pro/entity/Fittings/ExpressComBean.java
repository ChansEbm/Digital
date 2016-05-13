package com.szbb.pro.entity.fittings;

import com.szbb.pro.entity.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class ExpressComBean extends BaseBean {

    /**
     * comCode : yuantong
     * comName : 圆通
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String comCode;
        private String comName;

        public String getComCode() {
            return comCode;
        }

        public void setComCode(String comCode) {
            this.comCode = comCode;
        }

        public String getComName() {
            return comName;
        }

        public void setComName(String comName) {
            this.comName = comName;
        }
    }
}
