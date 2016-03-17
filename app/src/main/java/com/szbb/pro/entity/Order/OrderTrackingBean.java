package com.szbb.pro.entity.Order;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.szbb.pro.R;
import com.szbb.pro.entity.Base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/3/10.
 */
public class OrderTrackingBean extends BaseBean {


    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private String sn;
        private String addtime;
        private String distribute_time;

        /**
         * title : 操作记录第 1条
         * addtime : 2016-03-05 16:30
         */

        private List<ProcessListEntity> process_list;

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getDistribute_time() {
            return distribute_time;
        }

        public void setDistribute_time(String distribute_time) {
            this.distribute_time = distribute_time;
        }

        public List<ProcessListEntity> getProcess_list() {
            return process_list;
        }

        public void setProcess_list(List<ProcessListEntity> process_list) {
            this.process_list = process_list;
        }


        public static class ProcessListEntity extends BaseObservable {
            private String title;
            private String addtime;
            private String pos;//记录位置 如果是第一条 则把字体变成蓝色

            @BindingAdapter(value = {"app:bindTrackColor"})
            public static void bindTextColor(TextView textView, String pos) {
                if (pos.equals("0")) {
                    textView.setTextColor(textView.getResources().getColor(R.color.theme_primary));
                } else {
                    textView.setTextColor(textView.getResources().getColor(R.color
                            .color_text_gravy));
                }
            }

            @Bindable
            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            @Bindable
            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            @Bindable
            public String getPos() {
                return pos;
            }

            public void setPos(String pos) {
                this.pos = pos;
            }
        }
    }
}
