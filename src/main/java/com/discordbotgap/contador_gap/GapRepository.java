package com.discordbotgap.contador_gap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.discordbotgap.contador_gap.model.GapUser;

import java.util.List;

public interface GapRepository extends JpaRepository<GapUser, String> {
    // Soma todos os gaps do servidor
    @Query("SELECT SUM(g.totalGaps) FROM GapUser g")
    Long sumAllGaps();

    // Traz os 5 mais tomadores de gap
    List<GapUser> findTop5ByOrderByTotalGapsDesc();

    // Traz apenas los 3 maiores
    List<GapUser> findTop3ByOrderByTotalGapsDesc();
}
