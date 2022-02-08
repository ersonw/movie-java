package com.telebott.moviejava.control;

import com.telebott.moviejava.entity.RequestData;
import com.telebott.moviejava.entity.ResultData;
import com.telebott.moviejava.service.MoblieConfigService;
import com.telebott.moviejava.service.SystemMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiControl {
    @Autowired
    private MoblieConfigService configService;
    @Autowired
    private SystemMessageService messageService;
    @GetMapping("/getConfig")
    public ResultData getConfig(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(configService.getConfig());
        return data;
    }
    @GetMapping("/checkVersion")
    public ResultData checkVersion(){
        ResultData data = new ResultData();
        data.setData(configService.checkVersion());
        return data;
    }
    @GetMapping("/getSystemMessage")
    public ResultData getSystemMessage(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(messageService.getNewMessage());
        return data;
    }
}
