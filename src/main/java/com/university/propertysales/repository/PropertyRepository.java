package com.university.propertysales.repository;

import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByStatus(Property.PropertyStatus status);
    List<Property> findByType(Property.PropertyType type);
    List<Property> findBySeller(User seller);
    List<Property> findByLocationContainingIgnoreCase(String location);
    Optional<Property> findByTitle(String title);
    
    @Query("SELECT p FROM Property p WHERE p.status = :status AND p.type = :type")
    List<Property> findByStatusAndType(@Param("status") Property.PropertyStatus status, 
                                     @Param("type") Property.PropertyType type);
    
    @Query("SELECT p FROM Property p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Property> findByPriceBetween(@Param("minPrice") BigDecimal minPrice, 
                                    @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT p FROM Property p WHERE p.rentAmount BETWEEN :minRent AND :maxRent")
    List<Property> findByRentAmountBetween(@Param("minRent") BigDecimal minRent, 
                                         @Param("maxRent") BigDecimal maxRent);
    
    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.seller WHERE p.seller = :seller")
    List<Property> findBySellerWithRelationships(@Param("seller") User seller);
    
    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.seller WHERE p.seller.id = :sellerId")
    List<Property> findBySellerIdWithRelationships(@Param("sellerId") Long sellerId);
    
    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.seller")
    List<Property> findAllWithRelationships();
    
    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.seller WHERE p.id = :id")
    Property findByIdWithRelationships(@Param("id") Long id);
    
    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.seller WHERE p.type = :type")
    List<Property> findByTypeWithRelationships(@Param("type") Property.PropertyType type);
    
    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.seller WHERE p.status = :status")
    List<Property> findByStatusWithRelationships(@Param("status") Property.PropertyStatus status);
    
    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.seller WHERE p.status = :status AND p.type = :type")
    List<Property> findByStatusAndTypeWithRelationships(@Param("status") Property.PropertyStatus status, 
                                                       @Param("type") Property.PropertyType type);
    
    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.seller WHERE p.seller.id = :sellerId AND p.type = :type")
    List<Property> findBySellerIdAndTypeWithRelationships(@Param("sellerId") Long sellerId, 
                                                         @Param("type") Property.PropertyType type);
    
    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.seller WHERE p.seller.id = :sellerId AND p.type = :type AND p.status = :status")
    List<Property> findBySellerIdAndTypeAndStatusWithRelationships(@Param("sellerId") Long sellerId, 
                                                                  @Param("type") Property.PropertyType type,
                                                                  @Param("status") Property.PropertyStatus status);
    
    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.seller WHERE p.type = :type AND p.status = :status")
    List<Property> findByTypeAndStatusWithRelationships(@Param("type") Property.PropertyType type,
                                                       @Param("status") Property.PropertyStatus status);
    
    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.seller WHERE p.seller.id = :sellerId AND p.status = :status")
    List<Property> findBySellerIdAndStatusWithRelationships(@Param("sellerId") Long sellerId,
                                                           @Param("status") Property.PropertyStatus status);
}
