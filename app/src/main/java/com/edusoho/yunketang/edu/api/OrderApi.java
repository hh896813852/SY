package com.edusoho.yunketang.edu.api;

import com.google.gson.JsonObject;
import com.edusoho.yunketang.edu.bean.DataPageResult;
import com.edusoho.yunketang.edu.bean.OrderInfo;
import com.edusoho.yunketang.edu.bean.OrderListItem;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface OrderApi {
    @FormUrlEncoded
    @POST("order_info")
    Observable<OrderInfo> postOrderInfo(@Field("targetType") String type, @Field("targetId") int id);

    @FormUrlEncoded
    @POST("order_info")
    Observable<OrderInfo> postVipOrderInfo(@FieldMap Map<String, String> map);

    /**
     * @param map couponCode,coinPayAmount,targetType,targetId
     */
    @FormUrlEncoded
    @POST("orders")
    Observable<JsonObject> createOrder(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("pay_center")
    Observable<JsonObject> goPay(@Field("orderId") String id, @Field("targetType") String type,
                                 @Field("payment") String payWay);

    @GET("me/orders")
    Observable<DataPageResult<OrderListItem>> getOrders(@Query("offset") int offset, @Query("limit") int limit);

    @FormUrlEncoded
    @POST("trades")
    Observable<JsonObject> payOrderByCoin(@Field("gateway") String gateway, @Field("type") String type,
                                          @Field("orderSn") String orderSn,
                                          @Field("coinAmount") String coinAmount,
                                          @Field("payPassword") String payPassword);

    @FormUrlEncoded
    @POST("trades")
    Observable<JsonObject> payOrderByRMB(@Field("gateway") String gateway, @Field("type") String type,
                                         @Field("orderSn") String orderSn, @Field("appPay") String appPay);
}
