package com.telebott.moviejava.service;

import com.telebott.moviejava.dao.*;
import com.telebott.moviejava.entity.VideoComments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
