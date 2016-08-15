package com.szbb.pro.entity.vip;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.TextView;

import com.szbb.pro.R;
import com.szbb.pro.entity.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class FeedbackBean extends BaseBean {

    /**
     * id : 93
     * addtime : 2016-7-21 11:18
     * content : vkchmk
     * status : 0
     * status_msg : 未回复
     * reply : 123123
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean extends BaseObservable implements Parcelable {
        private String id;
        private String addtime;
        private String content;
        private String status;
        private String status_msg;
        private String reply = "暂无";

        @BindingAdapter(value = {"app:textColorWithinRepeatStatus"})
        public static void textColor(TextView textView, String status) {
            if (TextUtils.equals("1", status)) {
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.color_text_red));
                textView.setText("已回复");
            } else {
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.color_text_gravy));
                textView.setText("未回复");
            }
        }

        @Bindable
        public String getId() {
            StringBuilder builder = new StringBuilder(id);
            builder.insert(0, "id:");
            return builder.toString();
        }

        public void setId(String id) {
            this.id = id;
        }

        @Bindable
        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        @Bindable
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Bindable
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Bindable
        public String getStatus_msg() {
            return status_msg;
        }

        public void setStatus_msg(String status_msg) {
            this.status_msg = status_msg;
        }

        @Bindable
        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.addtime);
            dest.writeString(this.content);
            dest.writeString(this.status);
            dest.writeString(this.status_msg);
            dest.writeString(this.reply);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.id = in.readString();
            this.addtime = in.readString();
            this.content = in.readString();
            this.status = in.readString();
            this.status_msg = in.readString();
            this.reply = in.readString();
        }

        public static final Parcelable.Creator<ListBean> CREATOR = new Parcelable.Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }
}
