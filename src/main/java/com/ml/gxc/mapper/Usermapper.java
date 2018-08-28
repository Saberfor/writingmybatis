package com.ml.gxc.mapper;

import com.ml.gxc.bean.User;

import java.util.List;

public interface Usermapper {

    User selectUserById(String id);

    List<User> selectAllUser();
}
