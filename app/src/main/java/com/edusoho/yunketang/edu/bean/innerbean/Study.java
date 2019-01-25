package com.edusoho.yunketang.edu.bean.innerbean;

import com.edusoho.yunketang.bean.Cover;
import com.edusoho.yunketang.bean.Price;

import java.math.BigDecimal;

/**
 * Created by DF on 2017/5/11.
 */

public class Study {
    public int id;
    public int courseType; // 1、上元在线 2、上元会计
    public String type;
    public String title;
    public String summary;
    public Cover cover;
    public String about;
    public int studentNum;
    public String discount;
    public Price minCoursePrice2;
    public Price maxCoursePrice2;

    public String getJoinCount() {
        return studentNum + "人参与";
    }

    public String getPayMoney() {
        if (new BigDecimal(discount).compareTo(new BigDecimal(10)) == 0) {
            return "已付" + maxCoursePrice2.getPriceWithUnit();
        } else {
            return "已付" + minCoursePrice2.getPriceWithUnit();
        }
    }
}
