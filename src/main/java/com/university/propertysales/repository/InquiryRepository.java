package com.university.propertysales.repository;

import com.university.propertysales.entity.Inquiry;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByProperty(Property property);
    List<Inquiry> findBySender(User sender);
    List<Inquiry> findByStatus(Inquiry.InquiryStatus status);
    
    @Query("SELECT i FROM Inquiry i LEFT JOIN FETCH i.property LEFT JOIN FETCH i.sender")
    List<Inquiry> findAllWithRelationships();
    
    @Query("SELECT i FROM Inquiry i LEFT JOIN FETCH i.property LEFT JOIN FETCH i.sender WHERE i.id = :id")
    Inquiry findByIdWithRelationships(@Param("id") Long id);
}
