package com.xmx.androidframeworkbase.common.user;

import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by The_onE on 2017/3/15.
 */

public class UserData {
    // 保存原云对象，用于更新数据
    public AVObject object;

    public String objectId; // ID
    public String username; // 用户名
    public String nickname; // 昵称
    public List<String> subscribing; // 消息推送所订阅频道列表

    /**
     * 将LeanCloud对象转化为实体
     *
     * @param o 云端用户数据LeanCloud对象
     * @return 转化后的实体
     */
    static public UserData convert(AVObject o) {
        UserData data = new UserData();
        data.object = o;

        data.objectId = o.getObjectId();
        data.username = o.getString("username");
        data.nickname = o.getString("nickname");
        data.subscribing = o.getList("subscribing");

        return data;
    }
}
