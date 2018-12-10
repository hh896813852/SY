package com.edusoho.yunketang.bean;

import com.edusoho.yunketang.utils.DateUtils;
import com.edusoho.yunketang.utils.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JesseHuang on 2017/4/1.
 */

public class EvaluateData implements Serializable {
    public List<Evaluate> data;
    public Page paging;

    public class Page implements Serializable {
        public int total;
        public int offset;
        public int limit;
    }

    public class Evaluate implements Serializable {
        public int id;
        public int courseId;
        public String createdTime;
        public String updatedTime;
        public int rating;
        public User user;
        public String content;

        public String getTime() {
            return DateUtils.getPostDays(StringUtils.isEmpty(updatedTime) ? createdTime : updatedTime);
        }
    }
}
