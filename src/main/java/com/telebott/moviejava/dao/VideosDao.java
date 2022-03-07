package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideosDao extends JpaRepository<Videos, Integer>, CrudRepository<Videos, Integer> {
    Videos findAllById(long id);
    Videos findAllByTitle(String title);
    Videos findAllByShareId(String id);
}
