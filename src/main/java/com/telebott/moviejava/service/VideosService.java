package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.*;
import com.telebott.moviejava.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideosService {
    @Autowired
    private UserService userService;
    @Autowired
    private VideosDao videosDao;
    @Autowired
    private VideoCommentsDao videoCommentsDao;
    @Autowired
    private VideoCollectsDao videoCollectsDao;
    @Autowired
    private VideoLikesDao videoLikesDao;
    @Autowired
    private VideoRecommendsDao videoRecommendsDao;
    @Autowired
    private VideoActorsDao videoActorsDao;
    @Autowired
    private VideoCategoryDao videoCategoryDao;

    public void handlerYzm(YzmData yzmData) {
        Videos videos = videosDao.findAllByShareId(yzmData.getTitle());
        if (videos != null) return;
        videos = new Videos();
        videos.setTitle(yzmData.getTitle());
        videos.setVodTimeAdd(System.currentTimeMillis());
        videos.setShareId(yzmData.getShareid());
        videos.setVodContent(yzmData.getTitle());
        videos.setStatus(1);
        if (StringUtils.isNotEmpty(yzmData.getCategory())){
            VideoCategory category = videoCategoryDao.findAllById(Long.parseLong(yzmData.getCategory()));
            if (category != null){
                videos.setVodClass(category.getId());
            }
        }
        if (yzmData.getMetadata() != null){
            videos.setVodDuration(yzmData.getMetadata().getTime());
        }
        if (yzmData.getOutput() != null){
            String picDomain = yzmData.getDomain();
            if (StringUtils.isNotEmpty(yzmData.getPicdomain())) picDomain = yzmData.getPicdomain();
            videos.setPicThumb(picDomain + yzmData.getOutput().getPic1());
            if (StringUtils.isNotEmpty(yzmData.getOutput().getGif())) {
                String gif = yzmData.getOutput().getGif();
                videos.setGifThumb(yzmData.getDomain()+gif.replaceAll(yzmData.getOutdir(),""));
            }
            if (yzmData.getOutput().getVideo() != null){
                List<VideoData> videoDataList = yzmData.getOutput().getVideo();
                List<VideoPlayUrl> playUrls = new ArrayList<>();
                for (VideoData data: videoDataList) {
                    VideoPlayUrl playUrl = new VideoPlayUrl();
                    playUrl.setResolution(data.getResolution());
                    playUrl.setSize(data.getLength());
                    playUrl.setUrl(yzmData.getDomain()+yzmData.getRpath()+"/"+data.getBitrate()+"kb/hls/index.m3u8");
                    playUrls.add(playUrl);
                }
                videos.setVodPlayUrl(JSONArray.toJSONString(playUrls));
            }
        }
        String downloadDomain = yzmData.getDomain();
        if (StringUtils.isNotEmpty(yzmData.getMp4domain())) downloadDomain = yzmData.getMp4domain();
        videos.setVodDownUrl(downloadDomain+yzmData.getRpath()+"/mp4/"+yzmData.getPath()+".mp4");
        videosDao.saveAndFlush(videos);
    }
}
