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
        ResultData data = new ResultData();
        data.setData(videosService.gethotTags());
        return data;
    }
    @GetMapping("/random")
    public ResultData random(){
        ResultData data = new ResultData();
        data.setData(videosService.getRandom());
        return data;
    }
    @GetMapping("/search")
    public ResultData search(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.search(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/player")
    public ResultData player(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.player(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/favorite")
    public ResultData favorite(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.favorite(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/likeComment")
    public ResultData likeComment(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.likeComment(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/buy")
    public ResultData buy(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.buy(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/recommend")
    public ResultData recommend(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.recommend(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/recommends")
    public ResultData recommends(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.recommends(requestData.getData(),requestData.getUser()));
        return data;
    }
}
