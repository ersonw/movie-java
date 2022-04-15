package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.*;
import com.telebott.moviejava.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.script.Compilable;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private ActorMeasurementsDao actorMeasurementsDao;
    @Autowired
    private VideoActorsDao videoActorsDao;
    @Autowired
    private VideoCollectsDao videoCollectsDao;
    @Autowired
    private VideosService videosService;
    @Autowired
    private SystemConfigService systemConfigService;

    public JSONObject getFeatureds() {
        List<VideoFeatureds> featuredsList = videoFeaturedsDao.findAllByStatus(1);
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        for (VideoFeatureds featured: featuredsList) {
            JSONObject data = getFeaturedRecordsIndex(featured);
            if (data != null && !data.isEmpty()) array.add(data);
        }
        object.put("list",array);
        return object;
    }

    private JSONObject getFeaturedRecordsIndex(VideoFeatureds featured) {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        object.put("id", featured.getId());
        object.put("title",featured.getTitle());
        List<VideoFeaturedRecords> videoFeaturedRecordsList = videoFeaturedRecordsDao.findAllByFid(featured.getId());
        for (int i = 0; i < 4; i++){
            if (i < videoFeaturedRecordsList.size()){
                VideoFeaturedRecords record = videoFeaturedRecordsList.get(i);
                getRecordVideo(array, record);
            }
        }
        object.put("videos",array);
        if (array.isEmpty()) return null;
        return object;
    }

    private void getRecordVideo(JSONArray array, VideoFeaturedRecords record) {
        Videos video = videosDao.findAllById(record.getVid());
        if (video != null){
            JSONObject data = new JSONObject();
            data.put("id",video.getId());
            data.put("title",video.getTitle());
            data.put("image",videosService.getPicThumbUrl(video.getPicThumb()));
            data.put("play",videoPlayDao.countAllByVid(video.getId())+video.getPlay());
            data.put("recommendations", videoRecommendsDao.countAllByVid(video.getId())+video.getRecommends());
            array.add(data);
        }
    }

    private JSONObject getFeaturedRecords(VideoFeatureds featured) {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        object.put("id", featured.getId());
        object.put("title",featured.getTitle());
        List<VideoFeaturedRecords> videoFeaturedRecordsList = videoFeaturedRecordsDao.findAllByFid(featured.getId());
        for (VideoFeaturedRecords record: videoFeaturedRecordsList) {
            getRecordVideo(array, record);
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
            if (page < 1) page=1;
            page--;
            List<Videos> videosList = new ArrayList<>();
            if (type == 0){
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
                videosList = handlerPlayAndRemommends(getVideoList(videoFeaturedRecordsPage));
            }else {
                List<VideoFeaturedRecords> recordsList = new ArrayList<>();
                if (tagId > 0){
                    VideoFeatureds featureds = videoFeaturedsDao.findAllById(tagId);
                    if (featureds != null){
                        recordsList = videoFeaturedRecordsDao.findHotsByTag(featureds.getId(),page,20);
                    }else {
                        recordsList = videoFeaturedRecordsDao.findHots(page,20);
                    }

                }else {
                    recordsList = videoFeaturedRecordsDao.findHots(page,20);
                }
                if (recordsList.size() < 20){
                    object.put("total", page++);
                }else {
                    object.put("total", page+2);
                }
                for (VideoFeaturedRecords records: recordsList) {
                    Videos video = videosDao.findAllById(records.getVid());
                    if (video != null) videosList.add(video);
                }
                videosList = handlerPlayAndRemommends(videosList);
            }
            array = getVideoList(videosList);
        }
        object.put("list",array);
        return object;
    }

    private List<Videos> getVideoListSort(List<Videos> videosList) {
        Videos[] videos = videosList.toArray(new Videos[0]);
        Arrays.sort(videos);
        return Arrays.asList(videos);
    }
    private List<Videos> handlerPlayAndRemommends(List<Videos> videos){
        for (int i=0;i< videos.size();i++){
            if (videos.get(i).getPlay() == 0){
                videos.get(i).setPlay(videoPlayDao.countAllByVid(videos.get(i).getId()));
            }
            if (videos.get(i).getRecommends() == 0){
                videos.get(i).setRecommends(videoRecommendsDao.countAllByVid(videos.get(i).getId()));
            }
        }
        return videos;
    }
    private JSONArray getVideoList(List<Videos> videosList) {
        JSONArray array = new JSONArray();
        for (Videos video: videosList) {
            JSONObject object = new JSONObject();
            object.put("id",video.getId());
            object.put("title",video.getTitle());
            object.put("image",videosService.getPicThumbUrl(video.getPicThumb()));
//            object.put("play",video.getPlay());
//            object.put("remommends", video.getRecommends());
            long cardinality = 0;
            String cardinalityStr = systemConfigService.getValueByKey("cardinalityPlay");
            if (StringUtils.isNotEmpty(cardinalityStr)){
                cardinality = Long.parseLong(cardinalityStr);
            }
            if (video.getPlay() > 0) {
                object.put("play", video.getPlay()+cardinality);
            } else {
                object.put("play", videoPlayDao.countAllByVid(video.getId())+cardinality);
            }
            cardinality = 0;
            cardinalityStr = systemConfigService.getValueByKey("cardinalityRecommend");
            if (StringUtils.isNotEmpty(cardinalityStr)){
                cardinality = Long.parseLong(cardinalityStr);
            }
            if (video.getRecommends() > 0) {
                object.put("remommends", video.getRecommends()+cardinality);
            } else {
                object.put("remommends", videoRecommendsDao.countAllByVid(video.getId())+cardinality);
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

    public JSONObject getMeasurementTags() {
        List<ActorMeasurements> measurements = actorMeasurementsDao.findAllByStatus(1);
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for (ActorMeasurements measurement: measurements) {
            JSONObject data = new JSONObject();
            data.put("id", measurement.getId());
            data.put("title", measurement.getTitle());
            array.add(data);
        }
        object.put("list",array);
        return object;
    }

    public JSONObject getActorLists(String d) {
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
            if (page < 1) page=1;
            page--;
            long total = 1;
            List<VideoActors> actors = new ArrayList<>();
            if (type == 0){
                Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "id"));
                Page<VideoActors> videoActorsPage;
                if (tagId > 0){
                    ActorMeasurements measurements = actorMeasurementsDao.findAllById(tagId);
                    if (measurements != null){
                        videoActorsPage = videoActorsDao.findAllByMeasurements(measurements.getId(), pageable);
                    }else {
                        videoActorsPage = videoActorsDao.findAll(pageable);
                    }
                }else {
                    videoActorsPage = videoActorsDao.findAll(pageable);
                }
                object.put("total",videoActorsPage.getTotalPages());
                actors = videoActorsPage.getContent();
            }else if (type == 1){
                if (tagId>0){
                    ActorMeasurements measurements = actorMeasurementsDao.findAllById(tagId);
                    if (measurements != null){
                         actors = videoActorsDao.getAllByPlays(tagId,page,20);
                        total = (videoActorsDao.countAllByMeasurements(tagId) / 20);
                    }else {
                         actors = videoActorsDao.getAllByPlays(page,20);
                        total = (videoActorsDao.count() / 20);
                    }
                }else {
                    total = (videoActorsDao.count() / 20);
                     actors = videoActorsDao.getAllByPlays(page,20);
                }
                if (total < 1){
                    total = 1;
                }
                object.put("total",total);
            }else if (type == 2){
                if (tagId>0) {
                    ActorMeasurements measurements = actorMeasurementsDao.findAllById(tagId);
                    if (measurements != null) {
                        actors = videoActorsDao.getAllByWorks(tagId,page,20);
                        total = (videoActorsDao.countAllByMeasurements(tagId) / 20);
                    }else {
                        total = (videoActorsDao.count() / 20);
                        actors = videoActorsDao.getAllByWorks(page,20);
                    }
                }else {
                    total = (videoActorsDao.count() / 20);
                    actors = videoActorsDao.getAllByWorks(page,20);
                }
                if (total < 1){
                    total = 1;
                }
                object.put("total",total);
            }
            for (VideoActors actor: actors) {
                array.add(getActor(actor));
            }
        }
        object.put("list",array);
        return object;
    }
    private JSONObject getActor(VideoActors actors){
        JSONObject object = new JSONObject();
        if (actors != null){
            object.put("id",actors.getId());
            object.put("name",actors.getName());
            object.put("avatar",actors.getAvatar());
            object.put("collects",videoCollectsDao.countAllByAid(actors.getId()));
            object.put("work", videosDao.countAllByActor(actors.getId()));
            ActorMeasurements measurements = actorMeasurementsDao.findAllById(actors.getMeasurements());
            if (measurements != null){
                object.put("measurements", measurements.getTitle());
            }else {
                object.put("measurements", "?罩杯");
            }
        }
        return object;
    }
}
