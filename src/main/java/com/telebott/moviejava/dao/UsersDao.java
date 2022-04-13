package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDao extends JpaRepository<Users, Long>, CrudRepository<Users, Long> {
    Users findAllById(long id);
    Users findAllByIdentifier(String id);
    Users findAllByUid(String uid);
    Users findAllByPhone(String phone);
    Users findAllByEmail(String email);
    Users findAllByInvite(String invite);
    Page<Users> findAllByNicknameLikeAndStatus(String nickname,int status, Pageable pageable);
    long countAllBySuperior(long uid);
    Page<Users> findAllBySuperior(long uid, Pageable pageable);
}
