package com.telebott.moviejava.control;

import com.telebott.moviejava.entity.RequestData;
import com.telebott.moviejava.entity.ResultData;
import com.telebott.moviejava.service.MoblieConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiControl {
    @Autowired
    private MoblieConfigService configService;
    @GetMapping("/getConfig")
    public ResultData getConfig(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(configService.getConfig());
        return data;
    }
    @GetMapping("checkVersion")
    public ResultData checkVersion(){
        ResultData data = new ResultData();
        data.setData(configService.checkVersion());
        return data;
    }
}
