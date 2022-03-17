package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.UserPosts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostsDao extends JpaRepository<UserPosts, Integer>, CrudRepository<UserPosts, Integer> {
    Page<UserPosts> findAllByUid(long uid, Pageable pageable);
    UserPosts findAllById(long id);
    long countAllByUid(long uid);
    Page<UserPosts> findAllByUidAndStatus(long uid, int status, Pageable pageable);
}
