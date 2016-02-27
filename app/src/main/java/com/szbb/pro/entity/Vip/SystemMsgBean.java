package com.szbb.pro.entity.Vip;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.szbb.pro.entity.Base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/2/25.
 */
public class SystemMsgBean extends BaseBean {

    /**
     * annid : 1
     * title : 标题标题
     * thumb : http://192.168.200.69:8081图片地址
     * summary : 简介
     * addtime : 1970-01-01 08:00
     */

    private List<ListEntity> list;

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class ListEntity extends BaseObservable {
        private String annid;
        private String title;
        private String thumb;
        private String summary;
        private String addtime;

        @Bindable
        public String getAnnid() {
            return annid;
        }

        public void setAnnid(String annid) {
            this.annid = annid;
        }

        @Bindable
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Bindable
        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        @Bindable
        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        @Bindable
        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}
