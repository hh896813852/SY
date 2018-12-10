package com.edusoho.yunketang.bean.lesson;

import java.util.List;

/**
 * Created by DF on 2017/2/28.
 */

public class TeachLesson {

    private List<ResourcesBean> resources;

    public List<ResourcesBean> getResources() {
        return resources;
    }

    public void setResources(List<ResourcesBean> resources) {
        this.resources = resources;
    }

    public static class ResourcesBean {
        private int id;
        private String title;
        private String type;
        private String smallPicture;
        private String studentNum;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSmallPicture() {
            return smallPicture;
        }

        public void setSmallPicture(String smallPicture) {
            this.smallPicture = smallPicture;
        }

        public String getStudentNum() {
            return studentNum;
        }

        public void setStudentNum(String studentNum) {
            this.studentNum = studentNum;
        }
    }
}
