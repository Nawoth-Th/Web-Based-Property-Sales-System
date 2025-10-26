package com.university.propertysales.repository;

import com.university.propertysales.entity.RentalAgreement;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RentalAgreementRepository extends JpaRepository<RentalAgreement, Long> {
    List<RentalAgreement> findByProperty(Property property);
    List<RentalAgreement> findByTenant(User tenant);
    List<RentalAgreement> findByLandlord(User landlord);
    List<RentalAgreement> findByStatus(RentalAgreement.AgreementStatus status);
    
    @Query("SELECT r FROM RentalAgreement r LEFT JOIN FETCH r.property LEFT JOIN FETCH r.tenant LEFT JOIN FETCH r.landlord")
    List<RentalAgreement> findAllWithRelationships();
    
    @Query("SELECT r FROM RentalAgreement r LEFT JOIN FETCH r.property LEFT JOIN FETCH r.tenant LEFT JOIN FETCH r.landlord WHERE r.id = :id")
    RentalAgreement findByIdWithRelationships(@Param("id") Long id);
}
