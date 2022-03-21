package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.EditorRecommends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EditorRecommendsDao extends JpaRepository<EditorRecommends, Integer>, CrudRepository<EditorRecommends, Integer> {
    EditorRecommends findAllById(long id);
    @Query(value = "SELECT * FROM editor_recommends WHERE `show_time`=:date and status = 1", nativeQuery = true)
    List<EditorRecommends> findByDate(long date);
}
