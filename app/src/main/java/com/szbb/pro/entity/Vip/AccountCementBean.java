package com.szbb.pro.entity.Vip;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.szbb.pro.entity.Base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/2/26.
 */
public class AccountCementBean extends BaseBean {

    /**
     * type : 1
     */

    private CondEntity cond;
    /**
     * annid : 1
     * title : 标题标题
     * thumb : http://appbaba.jios.org:8081图片地址
     * summary : 简介
     * addtime : 1970-01-01 08:00
     * url : http://appbaba.jios.org:8081/ApiPages/announcement/id/1
     */

    private List<ListEntity> list;

    public CondEntity getCond() {
        return cond;
    }

    public void setCond(CondEntity cond) {
        this.cond = cond;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class CondEntity {
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public static class ListEntity extends BaseObservable {
        private String annid;
        private String title;
        private String thumb;
        private String summary;
        private String addtime;
        private String url;

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

        @Bindable
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
