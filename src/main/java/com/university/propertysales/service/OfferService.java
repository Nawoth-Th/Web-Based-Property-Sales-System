package com.university.propertysales.service;

import com.university.propertysales.entity.Offer;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import com.university.propertysales.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService {
    
    @Autowired
    private OfferRepository offerRepository;

    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    public Optional<Offer> getOfferById(Long id) {
        Offer offer = offerRepository.findByIdWithRelationships(id);
        return offer != null ? Optional.of(offer) : Optional.empty();
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAllWithRelationships();
    }

    public List<Offer> getOffersByProperty(Property property) {
        return offerRepository.findByProperty(property);
    }

    public List<Offer> getOffersByBuyer(User buyer) {
        return offerRepository.findByBuyer(buyer);
    }

    public List<Offer> getOffersByStatus(Offer.OfferStatus status) {
        return offerRepository.findByStatus(status);
    }

    public Offer updateOffer(Long id, Offer offerDetails) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
        
        offer.setPrice(offerDetails.getPrice());
        offer.setTerms(offerDetails.getTerms());
        offer.setStatus(offerDetails.getStatus());
        
        return offerRepository.save(offer);
    }

    public Offer updateOfferStatus(Long id, Offer.OfferStatus status) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
        
        offer.setStatus(status);
        return offerRepository.save(offer);
    }

    public void deleteOffer(Long id) {
        if (!offerRepository.existsById(id)) {
            throw new RuntimeException("Offer not found");
        }
        offerRepository.deleteById(id);
    }

    public List<Offer> getPendingOffers() {
        return offerRepository.findByStatus(Offer.OfferStatus.PENDING);
    }

    public Offer acceptOffer(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
        
        // Set this offer as accepted
        offer.setStatus(Offer.OfferStatus.ACCEPTED);
        
        // Reject all other pending offers for the same property
        List<Offer> otherOffers = offerRepository.findByProperty(offer.getProperty())
                .stream()
                .filter(o -> !o.getId().equals(id) && o.getStatus() == Offer.OfferStatus.PENDING)
                .toList();
        
        otherOffers.forEach(o -> {
            o.setStatus(Offer.OfferStatus.REJECTED);
            offerRepository.save(o);
        });
        
        return offerRepository.save(offer);
    }

    public Offer counterOffer(Long id, Offer counterOfferDetails) {
        Offer originalOffer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
        
        // Mark original offer as countered
        originalOffer.setStatus(Offer.OfferStatus.COUNTERED);
        offerRepository.save(originalOffer);
        
        // Create new counter offer
        Offer counterOffer = new Offer();
        counterOffer.setProperty(originalOffer.getProperty());
        counterOffer.setBuyer(originalOffer.getBuyer());
        counterOffer.setPrice(counterOfferDetails.getPrice());
        counterOffer.setTerms(counterOfferDetails.getTerms());
        counterOffer.setStatus(Offer.OfferStatus.PENDING);
        
        return offerRepository.save(counterOffer);
    }

    public void expireOldOffers() {
        // Auto-expire offers older than 7 days that are still pending
        List<Offer> pendingOffers = offerRepository.findByStatus(Offer.OfferStatus.PENDING);
        
        pendingOffers.stream()
                .filter(offer -> offer.getCreatedAt().isBefore(
                    LocalDateTime.now().minusDays(7)))
                .forEach(offer -> {
                    offer.setStatus(Offer.OfferStatus.EXPIRED);
                    offerRepository.save(offer);
                });
    }
}
