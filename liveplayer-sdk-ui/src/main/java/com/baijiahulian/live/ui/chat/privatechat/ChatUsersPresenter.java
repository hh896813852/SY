package com.baijiahulian.live.ui.chat.privatechat;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.live.ui.utils.RxUtils;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.models.imodels.IUserModel;
import com.baijiahulian.livecore.utils.LPBackPressureBufferedSubscriber;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by yangjingming on 2018/1/16.
 */

public class ChatUsersPresenter implements ChatUsersContract.Presenter{

    private ChatUsersContract.View view;
    private LiveRoomRouterListener routerListener;
    private Subscription subscriptionOfUserCountChange, subscriptionOfUserDataChange;
    private boolean isLoading = false;
    private List<IUserModel> iChatUserModels;
    public ChatUsersPresenter(ChatUsersContract.View view) {
        this.view = view;
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        routerListener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {
        subscriptionOfUserCountChange = routerListener.getLiveRoom()
                .getObservableOfUserNumberChange()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Integer>() {
                    @Override
                    public void call(Integer integer) {
                    }
                });
        subscriptionOfUserDataChange = routerListener.getLiveRoom()
                .getOnlineUserVM()
                .getObservableOfOnlineUser()
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPBackPressureBufferedSubscriber<List<IUserModel>>() {
                    @Override
                    public void call(List<IUserModel> iUserModels) {
                        // iUserModels == null   no more data
                        onChatUsersChanged();
                        if (isLoading)
                            isLoading = false;
                        if (!isPrivateChatUserAvailable()){
                            routerListener.onPrivateChatUserChange(null);
                            view.showPrivateChatLabel(null);
                        }
                        view.notifyDataChanged();
                    }
                });
        view.notifyDataChanged();
    }

    @Override
    public void unSubscribe() {
        RxUtils.unSubscribe(subscriptionOfUserCountChange);
        RxUtils.unSubscribe(subscriptionOfUserDataChange);
    }

    @Override
    public void destroy() {
        view = null;
        routerListener = null;
    }


    private void onChatUsersChanged() {
        if (routerListener.isTeacherOrAssistant()) {
            iChatUserModels = new ArrayList<>();
            int size = routerListener.getLiveRoom().getOnlineUserVM().getUserCount();
            for (int i = 0; i < size; i++) {
                IUserModel iUserModel = routerListener.getLiveRoom().getOnlineUserVM().getUser(i);
                if (routerListener.getLiveRoom().getCurrentUser() != iUserModel)
                    iChatUserModels.add(iUserModel);
            }
        } else {
            iChatUserModels = new ArrayList<>();
            int size = routerListener.getLiveRoom().getOnlineUserVM().getUserCount();
            if (routerListener.getLiveRoom().getTeacherUser() != null)
                iChatUserModels.add(routerListener.getLiveRoom().getTeacherUser());
            for (int i = 0; i < size; i++) {
                IUserModel userModel = routerListener.getLiveRoom().getOnlineUserVM().getUser(i);
                if (userModel.getType().equals(LPConstants.LPUserType.Assistant)) {
                    iChatUserModels.add(userModel);
                    break;
                }
            }
        }
        if (iChatUserModels.isEmpty()) {
            view.privateChatUserChanged(true);
        } else {
            view.privateChatUserChanged(false);
        }
    }


    private boolean isPrivateChatUserAvailable(){
        return iChatUserModels.contains(routerListener.getPrivateChatUser());
    }

    @Override
    public void chooseOneToChat(String chatName, boolean isEnter){
        view.showPrivateChatLabel(chatName);
    }

    @Override
    public void setPrivateChatUser(IUserModel iUserModel) {
        routerListener.onPrivateChatUserChange(iUserModel);
        view.notifyDataChanged();
    }

    @Override
    public IUserModel getPrivateChatUser() {
        return routerListener.getPrivateChatUser();
    }

    @Override
    public int getCount() {
        int count = iChatUserModels.size();
        return isLoading ? count + 1: count;
    }

    @Override
    public IUserModel getUser(int position) {
        if (!isLoading) {
            return iChatUserModels.get(position);
        }
        IUserModel iUserModel;
        if (iChatUserModels.size() == position) {
            iUserModel = null;
        } else {
            iUserModel = iChatUserModels.get(position);
        }
        return iUserModel;

    }

    @Override
    public void loadMore() {
        isLoading = true;
        routerListener.getLiveRoom().getOnlineUserVM().loadMoreUser();
        onChatUsersChanged();
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }
}

