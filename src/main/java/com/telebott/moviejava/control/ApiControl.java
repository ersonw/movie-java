package com.telebott.moviejava.control;

import com.telebott.moviejava.entity.RequestData;
import com.telebott.moviejava.entity.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiControl {
    @GetMapping("/getConfig")
    public ResultData getConfig(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        return data;
    }
}
