package com.university.propertysales.repository;

import com.university.propertysales.entity.Offer;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByProperty(Property property);
    List<Offer> findByBuyer(User buyer);
    List<Offer> findByStatus(Offer.OfferStatus status);
    
    @Query("SELECT o FROM Offer o LEFT JOIN FETCH o.property LEFT JOIN FETCH o.buyer")
    List<Offer> findAllWithRelationships();
    
    @Query("SELECT o FROM Offer o LEFT JOIN FETCH o.property LEFT JOIN FETCH o.buyer WHERE o.id = :id")
    Offer findByIdWithRelationships(@Param("id") Long id);
}
