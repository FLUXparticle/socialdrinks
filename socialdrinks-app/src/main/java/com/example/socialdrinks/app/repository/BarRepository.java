package com.example.socialdrinks.app.repository;

import com.example.socialdrinks.app.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface BarRepository extends JpaRepository<Bar, Long> {

    // Hier können bei Bedarf weitere Query-Methoden definiert werden.

}
