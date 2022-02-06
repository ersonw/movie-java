package com.telebott.moviejava.control;

import com.telebott.moviejava.entity.RequestData;
import com.telebott.moviejava.entity.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserControl {
    @PostMapping("/login")
    public ResultData test(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        System.out.println(requestData.getUid());
        return data;
    }
}
