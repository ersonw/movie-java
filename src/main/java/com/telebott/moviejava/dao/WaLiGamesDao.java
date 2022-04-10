package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.WaLiGames;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaLiGamesDao extends JpaRepository<WaLiGames, Long>, CrudRepository<WaLiGames, Long> {
    WaLiGames findAllById(long id);
    List<WaLiGames> findAllByStatus(int status);
    Page<WaLiGames> findAllByNameLike(String name, Pageable pageable);
}
