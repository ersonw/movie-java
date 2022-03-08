package com.telebott.moviejava.control;

import com.telebott.moviejava.entity.RequestData;
import com.telebott.moviejava.entity.ResultData;
import com.telebott.moviejava.service.VideosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/video")
public class VideoControl {
    @Autowired
    private VideosService videosService;
    @GetMapping("/hotTags")
    public ResultData hotTags(){
//        @ModelAttribute RequestData requestData
//        System.out.println(requestData.getUser());
        ResultData data = new ResultData();
        data.setData(videosService.gethotTags());
        return data;
    }
}
