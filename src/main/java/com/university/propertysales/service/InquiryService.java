package com.university.propertysales.service;

import com.university.propertysales.entity.Inquiry;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import com.university.propertysales.repository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InquiryService {
    
    @Autowired
    private InquiryRepository inquiryRepository;

    public Inquiry createInquiry(Inquiry inquiry) {
        return inquiryRepository.save(inquiry);
    }

    public Optional<Inquiry> getInquiryById(Long id) {
        return inquiryRepository.findById(id);
    }

    public List<Inquiry> getAllInquiries() {
        return inquiryRepository.findAllWithRelationships();
    }

    public List<Inquiry> getInquiriesByProperty(Property property) {
        return inquiryRepository.findByProperty(property);
    }

    public List<Inquiry> getInquiriesBySender(User sender) {
        return inquiryRepository.findBySender(sender);
    }

    public List<Inquiry> getInquiriesByStatus(Inquiry.InquiryStatus status) {
        return inquiryRepository.findByStatus(status);
    }

    public Inquiry updateInquiry(Long id, Inquiry inquiryDetails) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
        
        inquiry.setMessage(inquiryDetails.getMessage());
        inquiry.setStatus(inquiryDetails.getStatus());
        
        return inquiryRepository.save(inquiry);
    }

    public Inquiry updateInquiryStatus(Long id, Inquiry.InquiryStatus status) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
        
        inquiry.setStatus(status);
        return inquiryRepository.save(inquiry);
    }

    public void deleteInquiry(Long id) {
        if (!inquiryRepository.existsById(id)) {
            throw new RuntimeException("Inquiry not found");
        }
        inquiryRepository.deleteById(id);
    }

    public List<Inquiry> getOpenInquiries() {
        return inquiryRepository.findByStatus(Inquiry.InquiryStatus.OPEN);
    }

    public void archiveOldInquiries() {
        // Auto-archive inquiries older than 30 days that are resolved
        List<Inquiry> resolvedInquiries = inquiryRepository.findByStatus(Inquiry.InquiryStatus.RESOLVED);
        
        resolvedInquiries.stream()
                .filter(inquiry -> inquiry.getUpdatedAt().isBefore(
                    java.time.LocalDateTime.now().minusDays(30)))
                .forEach(inquiry -> {
                    inquiry.setStatus(Inquiry.InquiryStatus.ARCHIVED);
                    inquiryRepository.save(inquiry);
                });
    }
}
