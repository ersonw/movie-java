package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoOrders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoOrdersDao extends JpaRepository<VideoOrders, Integer>, CrudRepository<VideoOrders, Integer> {
    VideoOrders findAllById(long id);
    VideoOrders findAllByUidAndVid(long uid, long vid);
    Page<VideoOrders> findAllByUid(long uid, Pageable pageable);
    Page<VideoOrders> findAllByVid(long vid, Pageable pageable);
}
