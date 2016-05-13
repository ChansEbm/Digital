package com.szbb.pro.entity.fittings;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.szbb.pro.entity.base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2015/11/2.
 */
public class ShopCarBean extends BaseBean {

    /**
     * parts_id : 0
     * keyword :
     */

    private CondEntity cond;
    /**
     * parts_id : 14
     * parts_name : 压缩机
     */

    private List<PartsListEntity> parts_list;
    /**
     * acce_id : 107
     * acce_name : SDF
     * acce_model : hfdug3444
     * acce_thumb : /Public/uploads/pics/2015-11/563c1dec08fcf.jpg
     */

    private List<AcceListEntity> acce_list;

    public void setCond(CondEntity cond) {
        this.cond = cond;
    }

    public void setParts_list(List<PartsListEntity> parts_list) {
        this.parts_list = parts_list;
    }

    public void setAcce_list(List<AcceListEntity> acce_list) {
        this.acce_list = acce_list;
    }

    public CondEntity getCond() {
        return cond;
    }

    public List<PartsListEntity> getParts_list() {
        return parts_list;
    }

    public List<AcceListEntity> getAcce_list() {
        return acce_list;
    }

    public static class CondEntity {
        private int parts_id;
        private String keyword;

        public void setParts_id(int parts_id) {
            this.parts_id = parts_id;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public int getParts_id() {
            return parts_id;
        }

        public String getKeyword() {
            return keyword;
        }
    }

    public static class PartsListEntity {
        private String parts_id;
        private String parts_name;

        public void setParts_id(String parts_id) {
            this.parts_id = parts_id;
        }

        public void setParts_name(String parts_name) {
            this.parts_name = parts_name;
        }

        public String getParts_id() {
            return parts_id;
        }

        public String getParts_name() {
            return parts_name;
        }
    }

    public static class AcceListEntity extends BaseObservable {
        private String acce_id;
        private String acce_name;
        private String acce_model;
        private String acce_thumb;
        private boolean isExtra = false;

        public void setAcce_id(String acce_id) {
            this.acce_id = acce_id;
        }

        public void setAcce_name(String acce_name) {
            this.acce_name = acce_name;
        }

        public void setAcce_model(String acce_model) {
            this.acce_model = acce_model;
        }

        public void setAcce_thumb(String acce_thumb) {
            this.acce_thumb = acce_thumb;
        }

        public String getAcce_id() {
            return acce_id;
        }

        @Bindable
        public String getAcce_name() {
            return acce_name;
        }

        @Bindable
        public String getAcce_model() {
            return "规格 :" + acce_model;
        }

        public String getAcce_thumb() {
            return acce_thumb;
        }

        @Bindable
        public boolean isExtra() {
            return isExtra;
        }

        public void setIsExtra(boolean isExtra) {
            this.isExtra = isExtra;
        }
    }
}
