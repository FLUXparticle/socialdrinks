package com.example.socialdrinks.app.repository;

import com.example.socialdrinks.app.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface BarTransactionRepository extends JpaRepository<BarTransaction, Long> {

    List<BarTransaction> findByBarIdOrderByTimestampDesc(Long barId);

}
