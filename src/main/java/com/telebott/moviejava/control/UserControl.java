package com.telebott.moviejava.control;

import com.telebott.moviejava.entity.RequestData;
import com.telebott.moviejava.entity.ResultData;
import com.telebott.moviejava.entity.Users;
import com.telebott.moviejava.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserControl {
    @Autowired
    private UserService userService;
    @PostMapping("/info")
    public ResultData getInfo(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users users = new Users();
        if (requestData.getUser() != null){
            users = requestData.getUser();
        }else if (StringUtils.isNotEmpty(requestData.getIdentifier())){
            users = userService.loginByIdentifier(requestData.getIdentifier());
        }
        data.setData(userService.getResult(users));
        return data;
    }

}
