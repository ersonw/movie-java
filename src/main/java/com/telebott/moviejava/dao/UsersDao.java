package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDao extends JpaRepository<Users, Integer>, CrudRepository<Users, Integer> {
    Users findAllById(long id);
    Users findAllByIdentifier(String id);
    Users findAllByUid(String uid);
    Users findAllByPhone(String phone);
}
