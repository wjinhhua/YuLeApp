package com.myapp.yuleapp.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/2.11:21
 * 描述:
 **/
public class JoyImgBean {
    public int allNum;
    public int allPages;
    public List<JoyImg> contentlist = new ArrayList<>();
    public int currentPage;
    public int maxResult;
    public int ret_code;
    public class JoyImg{
        public String ct;
        public String id;
        public String img;
        public String title;
        public int type;
    }
}
