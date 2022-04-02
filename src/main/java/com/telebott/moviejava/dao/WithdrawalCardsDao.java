package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.WithdrawalCards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawalCardsDao extends JpaRepository<WithdrawalCards, Integer>, CrudRepository<WithdrawalCards, Integer> {
    WithdrawalCards findAllById(long id);
    WithdrawalCards findAllByIdAndUid(long id, long uid);
    List<WithdrawalCards> findAllByUid(long uid);
    long countAllByCode(String code);
    WithdrawalCards findAllByCode(String code);

    WithdrawalCards findAllByCodeAndName(String code, String name);
}
