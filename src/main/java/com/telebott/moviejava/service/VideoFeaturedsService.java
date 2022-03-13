package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.*;
import com.telebott.moviejava.entity.VideoFeaturedRecords;
import com.telebott.moviejava.entity.VideoFeatureds;
import com.telebott.moviejava.entity.Videos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoFeaturedsService {
    @Autowired
    private VideoFeaturedRecordsDao videoFeaturedRecordsDao;
    @Autowired
    private VideoFeaturedsDao videoFeaturedsDao;
    @Autowired
    private VideosDao videosDao;
    @Autowired
    private VideoPlayDao videoPlayDao;
    @Autowired
    private VideoRecommendsDao videoRecommendsDao;
    public JSONObject getFeatureds() {
        List<VideoFeatureds> featuredsList = videoFeaturedsDao.findAllByStatus(1);
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        for (VideoFeatureds featured: featuredsList) {
            JSONObject data = getFeaturedRecords(featured);
            if (data != null && !data.isEmpty()) array.add(data);
        }
        object.put("list",array);
        return object;
    }

    private JSONObject getFeaturedRecords(VideoFeatureds featured) {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        object.put("id", featured.getId());
        object.put("title",featured.getTitle());
        List<VideoFeaturedRecords> videoFeaturedRecordsList = videoFeaturedRecordsDao.findAllByFid(featured.getId());
        for (VideoFeaturedRecords record: videoFeaturedRecordsList) {
            Videos video = videosDao.findAllById(record.getVid());
            if (video != null){
                JSONObject data = new JSONObject();
                data.put("id",video.getId());
                data.put("title",video.getTitle());
                data.put("image",video.getPicThumb());
                if (video.getPlay() > 0){
                    data.put("play",video.getPlay());
                }else {
                    data.put("play",videoPlayDao.countAllByVid(video.getId()));
                }
                if (video.getRecommends() > 0){
                    data.put("recommendations", video.getRecommends());
                }else {
                    data.put("recommendations", videoRecommendsDao.countAllByVid(video.getId()));
                }
                array.add(data);
            }
        }
        object.put("videos",array);
        if (array.isEmpty()) return null;
        return object;
    }

    public JSONObject getFeaturedTags() {
        List<VideoFeatureds> videoFeaturedsList = videoFeaturedsDao.findAllByStatus(1);
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for (VideoFeatureds featured: videoFeaturedsList) {
            JSONObject data = new JSONObject();
            data.put("id",featured.getId());
            data.put("title",featured.getTitle());
            array.add(data);
        }
        object.put("list",array);
        return object;
    }

    public JSONObject getFeaturedLists(String d) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        int page = 1;
        int type = 0;
        long tagId = 0;
        if (data != null){
            if (data.get("page") != null) page = Integer.parseInt(data.get("page").toString());
            if (data.get("type") != null) type = Integer.parseInt(data.get("type").toString());
            if (data.get("tag") != null) tagId = Long.parseLong(data.get("tag").toString());
            page--;
            Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "id"));
            Page<VideoFeaturedRecords> videoFeaturedRecordsPage;
            if (tagId > 0){
                VideoFeatureds featureds = videoFeaturedsDao.findAllById(tagId);
                if (featureds != null){
                    videoFeaturedRecordsPage = videoFeaturedRecordsDao.findAllByFid(featureds.getId(), pageable);
                }else {
                    videoFeaturedRecordsPage = videoFeaturedRecordsDao.findAll(pageable);
                }
            }else {
                videoFeaturedRecordsPage = videoFeaturedRecordsDao.findAll(pageable);
            }
            object.put("total",videoFeaturedRecordsPage.getTotalPages());
            List<Videos> videosList = getVideoList(videoFeaturedRecordsPage);
            if (type == 1){
                videosList = getVideoListSort(videosList);
            }
            array = getVideoList(videosList);
        }
        object.put("list",array);
        return object;
    }

    private List<Videos> getVideoListSort(List<Videos> videosList) {

        return videosList;
    }
    private JSONArray getVideoList(List<Videos> videosList) {
        JSONArray array = new JSONArray();
        for (Videos video: videosList) {
            JSONObject object = new JSONObject();
            object.put("id",video.getId());
            object.put("title",video.getTitle());
            object.put("image",video.getPicThumb());
            if (video.getPlay() > 0){
                object.put("play",video.getPlay());
            }else {
                object.put("play",videoPlayDao.countAllByVid(video.getId()));
            }
            if (video.getRecommends() > 0){
                object.put("remommends", video.getRecommends());
            }else {
                object.put("remommends", videoRecommendsDao.countAllByVid(video.getId()));
            }
            array.add(object);
        }
        return array;
    }

    private List<Videos> getVideoList(Page<VideoFeaturedRecords> videoFeaturedRecordsPage){
        List<Videos> videosList = new ArrayList<>();
        for (VideoFeaturedRecords record: videoFeaturedRecordsPage.getContent()) {
            Videos videos = videosDao.findAllById(record.getVid());
            if (videos != null) videosList.add(videos);
        }
        return videosList;
    }
}
