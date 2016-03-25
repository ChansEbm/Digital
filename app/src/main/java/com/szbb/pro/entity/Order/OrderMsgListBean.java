package com.szbb.pro.entity.Order;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.net.Uri;

import com.bm.library.PhotoView;
import com.squareup.picasso.Picasso;
import com.szbb.pro.entity.Base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2016/3/17.
 */
public class OrderMsgListBean extends BaseBean {

    /**
     * message_id : 68
     * role : user
     * nickname : Herbie
     * addtime : 2016-02-25 10:44
     * content : 我想问先可以发货了吗
     * type : 1
     * img : helloworld.jpg
     * img_width : 200
     * img_height : 100
     */

    private List<ListEntity> list;

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class ListEntity extends BaseObservable {
        private String message_id;
        private String role;
        private String nickname;
        private String addtime;
        private String content;
        private String type;
        private String img;
        private String img_width;
        private String img_height;

        @BindingAdapter(value = {"app:photoSrc"})
        public static void setSrc(PhotoView photoView, String src) {
            if (photoView == null)
                return;
            Picasso.with(photoView.getContext()).load(Uri.parse(src)).resize(100, 100).into
                    (photoView);
        }

        @Bindable
        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        @Bindable
        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        @Bindable
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Bindable
        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        @Bindable
        public String getImg_width() {
            return img_width;
        }

        public void setImg_width(String img_width) {
            this.img_width = img_width;
        }

        @Bindable
        public String getImg_height() {
            return img_height;
        }

        public void setImg_height(String img_height) {
            this.img_height = img_height;
        }
    }
}
