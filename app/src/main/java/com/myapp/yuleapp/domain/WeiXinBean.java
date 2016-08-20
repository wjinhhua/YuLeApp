package com.myapp.yuleapp.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/1.23:34
 * 描述:
 **/
public class WeiXinBean {
    public int code;
    public String msg;
    public List<WeiXin> newslist = new ArrayList<>();
    public class WeiXin{
        public String ctime;
        public String description;
        public String picUrl;
        public String title;
        public String url;
    }
}
