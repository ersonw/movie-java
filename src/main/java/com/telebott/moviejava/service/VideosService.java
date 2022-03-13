package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.telebott.moviejava.dao.*;
import com.telebott.moviejava.entity.*;
import com.telebott.moviejava.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideosService {
    static int randomIndex = 0;
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
    @Autowired
    private SearchTagsDao searchTagsDao;
    @Autowired
    private VideoPlayDao videoPlayDao;
    @Autowired
    private VideoOrdersDao videoOrdersDao;
    @Autowired
    private VideoFavoritesDao videoFavoritesDao;
    @Autowired
    private DiamondRecordsDao diamondRecordsDao;
    @Autowired
    private RecommendLikesDao recommendLikesDao;
    @Autowired
    private ActorMeasurementsDao actorMeasurementsDao;

    public void handlerYzm(YzmData yzmData) {
        Videos videos = videosDao.findAllByShareId(yzmData.getShareid());
        if (videos == null){
            videos = new Videos();
            videos.setVodTimeAdd(System.currentTimeMillis());
        }else {
            videos.setVodTimeUpdate(System.currentTimeMillis());
            videos.setShareId(yzmData.getShareid());
        }
        videos.setTitle(yzmData.getTitle());
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

    public JSONObject gethotTags() {
        List<SearchTags> tags = searchTagsDao.getHots();
        JSONObject object = new JSONObject();
        List<String> list = new ArrayList<>();
        for (SearchTags tag: tags) {
            list.add(tag.getContext());
        }
        object.put("list",list);
        return object;
    }

    public JSONObject search(String data, Users user) {
        JSONObject objectData = JSONObject.parseObject(data);
        if (objectData != null && objectData.get("type") != null && objectData.get("text") != null){
            SearchTags searchTags = new SearchTags();
            searchTags.setAddTime(System.currentTimeMillis());
            searchTags.setContext(objectData.get("text").toString());
            searchTags.setUid(user.getId());
            searchTagsDao.saveAndFlush(searchTags);
            int page = 1;
            if (objectData.get("page") != null && StringUtils.isNotEmpty(objectData.get("page").toString())) page=Integer.parseInt(objectData.get("page").toString());
            page--;
            Page<Videos> videosPage;
            String sTitle = ZhConverterUtil.convertToSimple(objectData.get("text").toString());
            String tTitle = ZhConverterUtil.convertToTraditional(objectData.get("text").toString());
            Pageable pageable = PageRequest.of(page, 30, Sort.by(Sort.Direction.DESC, "id"));
            switch (Integer.parseInt(objectData.get("type").toString())){
                case 0:
                    videosPage = videosDao.findByAv(sTitle,tTitle,pageable);
                    return getVideoList(videosPage);
                default:
                    break;
            }
        }
        return null;
    }

    private JSONObject getVideoList(Page<Videos> videosPage) {
        JSONArray array = new JSONArray();
        for (int i=0;i< videosPage.getContent().size();i++){
            JSONObject item = new JSONObject();
            Videos video = videosPage.getContent().get(i);
            item.put("title",video.getTitle());
            item.put("id",video.getId());
            item.put("image",video.getPicThumb());
            item.put("number",video.getNumbers());
            if (video.getPlay() > 0) {
                item.put("play",video.getPlay());
            }else {
                item.put("play",videoPlayDao.countAllByVid(video.getId()));
            }
            if (video.getRecommends() > 0){
                item.put("remommends",video.getRecommends());
            }else {
                item.put("remommends",videoRecommendsDao.countAllByVid(video.getId()));
            }
//            item.put("account",video);
            array.add(item);
        }
        JSONObject object = new JSONObject();
        object.put("list",array);
        return object;
    }

    public JSONObject player(String data, Users user) {
        JSONObject objectData = JSONObject.parseObject(data);
        if (objectData != null && objectData.get("id") != null){
            Videos videos = videosDao.findAllByIdAndStatus(Long.parseLong(objectData.get("id").toString()),1);
            if (videos != null){
                VideoPlay videoPlay = new VideoPlay();
                videoPlay.setVid(videos.getId());
                videoPlay.setUid(user.getId());
                videoPlay.setAddTime(System.currentTimeMillis());
                videoPlayDao.saveAndFlush(videoPlay);
                JSONObject info = getplayerObject(videos, user);
                JSONObject object = new JSONObject();
                object.put("verify",true);
                VideoFavorites favorites = videoFavoritesDao.findAllByUidAndVid(user.getId(),videos.getId());
                if (favorites != null){
                    info.put("favorite",true);
                }
                if (videos.getDiamond() > 0){
                    VideoOrders orders = videoOrdersDao.findAllByUidAndVid(user.getId(),videos.getId());
                    if (orders == null){
                        info.put("playUrl","");
                        info.put("downloadUrl","");
                        object.put("verify",false);
                    }
                }else {
                    if (user.getExpired() < System.currentTimeMillis()){
                        info.put("playUrl","");
                        info.put("downloadUrl","");
                        object.put("verify",false);
                    }
                }
                object.put("info", info);
                return object;
            }
        }
        return (JSONObject) (new JSONObject()).put("error", true);
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
    private String getPlayUrl(String data){
        List<VideoPlayUrl> videoPlayUrls = JSONArray.parseArray(data,VideoPlayUrl.class);
        if (videoPlayUrls == null){
            return null;
        }
        return videoPlayUrls.get(videoPlayUrls.size()-1).getUrl();
    }
    private JSONObject getplayerObject(Videos videos, Users user) {
        JSONObject object = new JSONObject();
        object.put("favorite",false);
        object.put("id",videos.getId());
        object.put("title",videos.getTitle());
        object.put("duration",videos.getVodDuration());
        JSONObject actor = getActor(videoActorsDao.findAllById(videos.getActor()));
        VideoCollects collects = videoCollectsDao.findAllByUidAndAid(user.getId(),videos.getActor());
        if (collects != null){
            actor.put("collect",true);
        }
        object.put("actor",actor);
        object.put("pic",videos.getPicThumb());
        object.put("tag",videos.getVodTag());
        object.put("diamond",videos.getDiamond());
        object.put("downloadUrl",videos.getVodDownUrl());
        object.put("playUrl",getPlayUrl(videos.getVodPlayUrl()));
        if (videos.getPlay() > 0) {
            object.put("play",videos.getPlay());
        }else {
            object.put("play",videoPlayDao.countAllByVid(videos.getId()));
        }
        if (videos.getRecommends() > 0){
            object.put("recommendations",videos.getRecommends());
        }else {
            object.put("recommendations",videoRecommendsDao.countAllByVid(videos.getId()));
        }
        return object;
    }

    public JSONObject getRandom() {
        Pageable pageable = PageRequest.of(randomIndex, 20, Sort.by(Sort.Direction.DESC, "id"));
        Page<Videos> videosPage = videosDao.findAllByStatus(1,pageable);
        if (videosPage.getTotalPages() > (randomIndex+1)){
            randomIndex++;
        }else {
            randomIndex = 0;
        }
        return getVideoList(videosPage);
    }

    public JSONObject favorite(String d, Users user) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify", false);
        if (data != null && data.get("id") != null){
            Videos videos = videosDao.findAllById(Long.parseLong(data.get("id").toString()));
            if (videos != null){
                VideoFavorites favorites = videoFavoritesDao.findAllByUidAndVid(user.getId(),videos.getId());
                if (favorites == null){
                    favorites = new VideoFavorites();
                    favorites.setVid(videos.getId());
                    favorites.setUid(user.getId());
                    favorites.setAddTime(System.currentTimeMillis());
                    videoFavoritesDao.saveAndFlush(favorites);
                }else {
                    videoFavoritesDao.delete(favorites);
                }
                object.put("verify", true);
            }
        }
        return object;
    }
    public JSONObject likeComment(String d, Users user) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify", false);
        if (data != null && data.get("id") != null){
            VideoRecommends recommends = videoRecommendsDao.findAllById(Long.parseLong(data.get("id").toString()));
            if (recommends != null){
                RecommendLikes recommendLikes = recommendLikesDao.findAllByUidAndRid(user.getId(),recommends.getId());
                if (recommendLikes == null){
                    recommendLikes = new RecommendLikes();
                    recommendLikes.setRid(recommends.getId());
                    recommendLikes.setUid(user.getId());
                    recommendLikes.setAddTime(System.currentTimeMillis());
                    recommendLikesDao.saveAndFlush(recommendLikes);
                }else {
                    recommendLikesDao.delete(recommendLikes);
                }
                object.put("verify", true);
            }
        }
        return object;
    }

    public JSONObject buy(String d, Users user) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify", false);
        if (data != null && data.get("id") != null) {
            Videos videos = videosDao.findAllById(Long.parseLong(data.get("id").toString()));
            if (videos != null) {
                if (videos.getDiamond() > 0){
                    if (user.getDiamond() < videos.getDiamond()){
                        object.put("msg", "钻石余额不足，请先充值!");
                    }else {
                        object.put("verify", true);
                        VideoOrders orders = videoOrdersDao.findAllByUidAndVid(user.getId(),videos.getId());
                        if (orders == null){
                            DiamondRecords records = new DiamondRecords();
                            records.setCtime(System.currentTimeMillis());
                            records.setUid(user.getId());
                            records.setReason("购买了付费影片《"+videos.getTitle()+"》");
                            records.setDiamond(-(videos.getDiamond()));
                            diamondRecordsDao.saveAndFlush(records);
                            orders = new VideoOrders();
                            orders.setAddTime(System.currentTimeMillis());
                            orders.setVid(videos.getId());
                            orders.setStatus(1);
                            orders.setUid(user.getId());
                            videoOrdersDao.saveAndFlush(orders);
                            user.setDiamond(user.getDiamond()-videos.getDiamond());
                            userService._saveAndPush(user);
                        }
                    }
                }else {
                    object.put("msg", "该影片为会员影片，开通会员免费观看!");
                }
            }
        }
        return object;
    }

    public JSONObject recommend(String d, Users user) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify", false);
        if (data != null && data.get("id") != null && data.get("reason") != null) {
            if (data.get("reason").toString().length() < 101){
                Videos videos = videosDao.findAllById(Long.parseLong(data.get("id").toString()));
                if (videos != null) {
                    VideoRecommends videoRecommends = videoRecommendsDao.findAllByUidAndVid(user.getId(),videos.getId());
                    if (videoRecommends == null){
                        object.put("verify", true);
                        videoRecommends = new VideoRecommends();
                        videoRecommends.setVid(videos.getId());
                        videoRecommends.setReason(data.get("reason").toString());
                        videoRecommends.setUid(user.getId());
                        videoRecommends.setStatus(1);
                        videoRecommends.setAddTime(System.currentTimeMillis());
                        videoRecommendsDao.saveAndFlush(videoRecommends);
                    }else {
                        object.put("msg", "不可重复推荐!");
                    }
                }else {
                    object.put("msg", "系统错误!");
                }
            }else {
                object.put("msg", "推荐理由超出字数限制!");
            }
        }
        return object;
    }
    private JSONArray getRecommends(long vid, int page, long _uid){
        JSONArray array = new JSONArray();
        page--;
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "id"));
        Page<VideoRecommends> videoRecommendsPage = videoRecommendsDao.findAllByVidAndStatus(vid,1,pageable);
        for (VideoRecommends recommend: videoRecommendsPage.getContent()) {
            Users users = userService._getById(recommend.getUid());
            if (users != null){
                JSONObject object = new JSONObject();
                object.put("likes",recommendLikesDao.countAllByRid(recommend.getId()));
                object.put("id",recommend.getId());
                object.put("context",recommend.getReason());
                object.put("isFirst",_isFirst(recommend.getVid(),recommend.getUid()));
                object.put("isLike",false);
                RecommendLikes likes = recommendLikesDao.findAllByUidAndRid(_uid,recommend.getId());
                if (likes != null){
                    object.put("isLike",true);
                }
                object.put("avatar",users.getAvatar());
                object.put("nickname",users.getNickname());
                object.put("uid",users.getId());
                array.add(object);
            }
        }
        return array;
    }
    private boolean _isFirst(long vid, long uid){
        VideoRecommends recommends = videoRecommendsDao.getFirst(vid);
        if (recommends != null){
            if (recommends.getUid() == uid) return true;
        }
        return false;
    }
    private JSONObject getVideoLists(Page<VideoRecommends> videoRecommendsPage, Users user){
        JSONObject data =new JSONObject();
        JSONArray array = new JSONArray();
        data.put("total",videoRecommendsPage.getTotalPages());
        for (int i=0; i < videoRecommendsPage.getContent().size();i++){
            VideoRecommends recommends = videoRecommendsPage.getContent().get(i);
            Videos videos = videosDao.findAllById(recommends.getVid());
            if (videos != null){
                JSONObject object = new JSONObject();
                object.put("comments",getRecommends(recommends.getVid(),1,user.getId()));
                object.put("id",recommends.getId());
                object.put("image",videos.getPicThumb());
                object.put("vid",videos.getId());
                object.put("recommends",videoRecommendsDao.countAllByVid(recommends.getVid()));
                array.add(object);
            }
        }
        data.put("list",array);
        return data;
    }
    public JSONObject recommends(String d, Users user) {
        int page = 1;
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        if (data.get("page") != null) page = Integer.parseInt(data.get("page").toString());
        page--;
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "id"));
        if (data.get("type") == null || data.get("type").toString().equals("today")){
            Page<VideoRecommends> videoRecommendsPage = videoRecommendsDao.getAllByDateTime(TimeUtil.getTodayZero(), pageable);
            object = getVideoLists(videoRecommendsPage,user);
        }else if (data.get("type").toString().equals("week")){
            Page<VideoRecommends> videoRecommendsPage = videoRecommendsDao.getAllByDateTime(TimeUtil.getBeforeDaysZero(7), pageable);
            object = getVideoLists(videoRecommendsPage,user);
        }else if (data.get("type").toString().equals("all")){
            Page<VideoRecommends> videoRecommendsPage = videoRecommendsDao.getAllByAll(pageable);
            object = getVideoLists(videoRecommendsPage,user);
        }
        return object;
    }
}
