package com.szbb.pro.entity.login;

import com.szbb.pro.entity.base.BaseBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2015/12/26.
 */
public class AreaListBean extends BaseBean {

    private List<ListEntity> list;

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String id;
        private String name;
        private int hasChild;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setHasChild(int hasChild) {
            this.hasChild = hasChild;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getHasChild() {
            return hasChild;
        }

        public boolean isHasChild() {
            return hasChild == 1;
        }
    }
}
