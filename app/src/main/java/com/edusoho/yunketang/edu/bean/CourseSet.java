package com.edusoho.yunketang.edu.bean;

import com.edusoho.yunketang.bean.Cover;
import com.edusoho.yunketang.bean.Price;
import com.edusoho.yunketang.edu.bean.innerbean.Teacher;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JesseHuang on 2017/3/27.
 */

public class CourseSet implements Serializable {

    public int    id;
    public String type;
    public String title;
    public String subtitle;
    public String status;
    public String summary;
    public Cover cover;
    public float  rating;
    public int    studentNum;
    public int    discountId;
    public float  discount;
    public String parentId;
    public String locked;
    public float  maxOriginPrice;
    public float  minOriginPrice;
    public float  maxCoursePrice;
    public float  minCoursePrice;

    /**
     * 打折前最大价格
     */
    public Price maxOriginPrice2;

    /**
     * 打折前最小价格
     */
    public Price  minOriginPrice2;

    /**
     * 打折后最大价格
     */
    public Price  maxCoursePrice2;

    /**
     * 打折后最小价格
     */
    public Price  minCoursePrice2;

    public String        createdTime;
    public String        updatedTime;
    public String[]      audiences;
    public List<Teacher> teachers;
}
