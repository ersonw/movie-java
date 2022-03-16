package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.UserFollows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowsDao extends JpaRepository<UserFollows, Integer>, CrudRepository<UserFollows, Integer> {
    long countAllByUid(long uid);
    long countAllByToUid(long uid);
    UserFollows findAllById(long id);
    UserFollows findAllByUidAndToUid(long uid, long toUid);
    Page<UserFollows> findAllByUid(long uid, Pageable pageable);
    Page<UserFollows> findAllByToUid(long uid, Pageable pageable);
}
