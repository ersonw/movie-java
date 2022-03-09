package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoFavorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoFavoritesDao extends JpaRepository<VideoFavorites, Integer>, CrudRepository<VideoFavorites, Integer> {
    VideoFavorites findAllById(long id);
    VideoFavorites findAllByUidAndVid(long uid, long vid);
}
