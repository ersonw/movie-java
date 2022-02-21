package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.OnlineOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineOrderDao extends JpaRepository<OnlineOrder, Integer>, CrudRepository<OnlineOrder, Integer> {
    Page<OnlineOrder> findAllByUid(long uid, Pageable pageable);
}
