package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.EditorRecommends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorRecommendsDao extends JpaRepository<EditorRecommends, Integer>, CrudRepository<EditorRecommends, Integer> {
    EditorRecommends findAllById(long id);
}
