package com.university.propertysales.service;

import com.university.propertysales.entity.RentalAgreement;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import com.university.propertysales.repository.RentalAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RentalAgreementService {
    
    @Autowired
    private RentalAgreementRepository rentalAgreementRepository;

    public RentalAgreement createRentalAgreement(RentalAgreement agreement) {
        // Check if property already has an active rental agreement
        List<RentalAgreement> activeAgreements = rentalAgreementRepository
                .findByProperty(agreement.getProperty())
                .stream()
                .filter(a -> a.getStatus() == RentalAgreement.AgreementStatus.ACTIVE)
                .toList();
        
        if (!activeAgreements.isEmpty()) {
            throw new RuntimeException("Property already has an active rental agreement");
        }
        
        return rentalAgreementRepository.save(agreement);
    }

    public Optional<RentalAgreement> getRentalAgreementById(Long id) {
        return rentalAgreementRepository.findById(id);
    }

    public List<RentalAgreement> getAllRentalAgreements() {
        return rentalAgreementRepository.findAllWithRelationships();
    }

    public List<RentalAgreement> getRentalAgreementsByProperty(Property property) {
        return rentalAgreementRepository.findByProperty(property);
    }

    public List<RentalAgreement> getRentalAgreementsByTenant(User tenant) {
        return rentalAgreementRepository.findByTenant(tenant);
    }

    public List<RentalAgreement> getRentalAgreementsByLandlord(User landlord) {
        return rentalAgreementRepository.findByLandlord(landlord);
    }

    public List<RentalAgreement> getRentalAgreementsByStatus(RentalAgreement.AgreementStatus status) {
        return rentalAgreementRepository.findByStatus(status);
    }

    public RentalAgreement updateRentalAgreement(Long id, RentalAgreement agreementDetails) {
        RentalAgreement agreement = rentalAgreementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental agreement not found"));
        
        agreement.setRent(agreementDetails.getRent());
        agreement.setDurationMonths(agreementDetails.getDurationMonths());
        agreement.setStartDate(agreementDetails.getStartDate());
        agreement.setStatus(agreementDetails.getStatus());
        
        return rentalAgreementRepository.save(agreement);
    }

    public RentalAgreement updateAgreementStatus(Long id, RentalAgreement.AgreementStatus status) {
        RentalAgreement agreement = rentalAgreementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental agreement not found"));
        
        agreement.setStatus(status);
        return rentalAgreementRepository.save(agreement);
    }

    public void deleteRentalAgreement(Long id) {
        if (!rentalAgreementRepository.existsById(id)) {
            throw new RuntimeException("Rental agreement not found");
        }
        rentalAgreementRepository.deleteById(id);
    }

    public RentalAgreement extendRentalAgreement(Long id, Integer additionalMonths) {
        RentalAgreement agreement = rentalAgreementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental agreement not found"));
        
        agreement.setDurationMonths(agreement.getDurationMonths() + additionalMonths);
        return rentalAgreementRepository.save(agreement);
    }

    public List<RentalAgreement> getExpiringAgreements(LocalDate beforeDate) {
        return rentalAgreementRepository.findAll()
                .stream()
                .filter(agreement -> {
                    LocalDate endDate = agreement.getStartDate().plusMonths(agreement.getDurationMonths());
                    return endDate.isBefore(beforeDate) && 
                           agreement.getStatus() == RentalAgreement.AgreementStatus.ACTIVE;
                })
                .toList();
    }
}
