package com.university.propertysales.controller;

import com.university.propertysales.dto.PropertyCreateDTO;
import com.university.propertysales.dto.PropertyDTO;
import com.university.propertysales.dto.PropertyUpdateDTO;
import com.university.propertysales.entity.Property;
import com.university.propertysales.entity.User;
import com.university.propertysales.mapper.PropertyMapper;
import com.university.propertysales.service.PropertyService;
import com.university.propertysales.service.UserService;
import com.university.propertysales.repository.PropertyRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private PropertyRepository propertyRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAllProperties(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) BigDecimal minRent,
            @RequestParam(required = false) BigDecimal maxRent) {

        List<Property> properties;

        if (type != null && status != null) {
            Property.PropertyType propertyType = Property.PropertyType.valueOf(type.toUpperCase());
            Property.PropertyStatus propertyStatus = Property.PropertyStatus.valueOf(status.toUpperCase());
            properties = propertyService.getAvailablePropertiesByType(propertyType);
        } else if (type != null) {
            Property.PropertyType propertyType = Property.PropertyType.valueOf(type.toUpperCase());
            properties = propertyService.getPropertiesByType(propertyType);
        } else if (status != null && status.equalsIgnoreCase("AVAILABLE")) {
            properties = propertyService.getAvailableProperties();
        } else if (location != null) {
            properties = propertyService.searchPropertiesByLocation(location);
        } else if (minPrice != null && maxPrice != null) {
            properties = propertyService.getPropertiesByPriceRange(minPrice, maxPrice);
        } else if (minRent != null && maxRent != null) {
            properties = propertyService.getPropertiesByRentRange(minRent, maxRent);
        } else {
            properties = propertyService.getAllProperties();
        }

        List<PropertyDTO> propertyDTOs = properties.stream()
                .map(propertyMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(propertyDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPropertyById(@PathVariable Long id) {
        Optional<Property> property = propertyService.getPropertyById(id);
        if (property.isPresent()) {
            PropertyDTO propertyDTO = propertyMapper.toDTO(property.get());
            return ResponseEntity.ok(propertyDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createProperty(@Valid @RequestBody PropertyCreateDTO propertyCreateDTO) {
        try {
            Optional<User> seller = userService.getUserById(propertyCreateDTO.getSellerId());
            if (!seller.isPresent()) {
                return ResponseEntity.badRequest().body("Seller not found");
            }

            Property property = propertyMapper.toEntity(propertyCreateDTO, seller.get());
            Property createdProperty = propertyService.createProperty(property);
            PropertyDTO propertyDTO = propertyMapper.toDTO(createdProperty);

            return ResponseEntity.status(HttpStatus.CREATED).body(propertyDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProperty(@PathVariable Long id, @Valid @RequestBody PropertyUpdateDTO propertyUpdateDTO) {
        try {
            Property property = propertyService.getPropertyById(id)
                    .orElseThrow(() -> new RuntimeException("Property not found"));

            propertyMapper.updateEntityFromDTO(propertyUpdateDTO, property);

            Property updatedProperty = propertyService.updateProperty(id, property);
            PropertyDTO propertyDTO = propertyMapper.toDTO(updatedProperty);

            return ResponseEntity.ok(propertyDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/update-with-images")
    public ResponseEntity<?> updatePropertyWithImages(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("location") String location,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "rentAmount", required = false) BigDecimal rentAmount,
            @RequestParam("description") String description,
            @RequestParam(value = "files", required = false) MultipartFile[] files) {
        
        try {
            Property property = propertyService.getPropertyById(id)
                    .orElseThrow(() -> new RuntimeException("Property not found"));

            property.setTitle(title);
            property.setType(Property.PropertyType.valueOf(type.toUpperCase()));
            property.setLocation(location);
            property.setDescription(description);
            
            if (price != null) {
                property.setPrice(price);
                property.setRentAmount(null);
            } else if (rentAmount != null) {
                property.setRentAmount(rentAmount);
                property.setPrice(null);
            }

            // Handle new image uploads if provided
            if (files != null && files.length > 0) {
                List<String> uploadedFiles = new ArrayList<>();
                
                try {
                    // Create upload directory if it doesn't exist
                    Path uploadPath = Paths.get(uploadDir, "properties");
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    for (MultipartFile file : files) {
                        if (file.isEmpty()) continue;
                        
                        // Validate file type
                        String contentType = file.getContentType();
                        if (contentType == null || !contentType.startsWith("image/")) {
                            continue; // Skip invalid files
                        }
                        
                        // Generate unique filename
                        String originalFilename = file.getOriginalFilename();
                        String extension = originalFilename != null ? 
                            originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
                        String filename = UUID.randomUUID().toString() + extension;
                        
                        // Save file
                        Path filePath = uploadPath.resolve(filename);
                        Files.copy(file.getInputStream(), filePath);
                        
                        // Add to uploaded files list
                        uploadedFiles.add("/uploads/properties/" + filename);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload images: " + e.getMessage(), e);
                }
                
                if (!uploadedFiles.isEmpty()) {
                    // Keep existing images and add new ones
                    List<String> allImages = new ArrayList<>();
                    
                    // Add existing images if any
                    if (property.getImages() != null && !property.getImages().isEmpty()) {
                        String[] existingImages = property.getImages().split(",");
                        for (String existingImage : existingImages) {
                            if (existingImage.trim().startsWith("/uploads/properties/")) {
                                allImages.add(existingImage.trim());
                            }
                        }
                    }
                    
                    // Add new images
                    allImages.addAll(uploadedFiles);
                    
                    // Update images
                    property.setImages(String.join(",", allImages));
                    
                    // Set first image as main if no main image exists
                    if (property.getMainImage() == null || property.getMainImage().isEmpty()) {
                        property.setMainImage(uploadedFiles.get(0));
                    }
                }
            }

            Property updatedProperty = propertyService.updateProperty(id, property);
            PropertyDTO propertyDTO = propertyMapper.toDTO(updatedProperty);

            return ResponseEntity.ok(propertyDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id) {
        try {
            propertyService.deleteProperty(id);
            return ResponseEntity.ok().body("Property deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updatePropertyStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        try {
            Property.PropertyStatus status = Property.PropertyStatus.valueOf(request.getStatus().toUpperCase());
            Property updatedProperty = propertyService.updatePropertyStatus(id, status);
            PropertyDTO propertyDTO = propertyMapper.toDTO(updatedProperty);
            return ResponseEntity.ok(propertyDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getPropertiesBySeller(@PathVariable Long sellerId) {
        try {
            // Use the repository directly to avoid potential lazy loading issues
            List<Property> properties = propertyRepository.findBySellerIdWithRelationships(sellerId);
            List<PropertyDTO> propertyDTOs = properties.stream()
                    .map(propertyMapper::toDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(propertyDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/seller/{sellerId}/available")
    public ResponseEntity<?> getAvailablePropertiesBySeller(@PathVariable Long sellerId) {
        try {
            // Fetch only available properties for the seller
            List<Property> properties = propertyRepository.findBySellerIdAndStatusWithRelationships(
                sellerId, Property.PropertyStatus.AVAILABLE);
            List<PropertyDTO> propertyDTOs = properties.stream()
                    .map(propertyMapper::toDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(propertyDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/seller/{sellerId}/rental")
    public ResponseEntity<?> getRentalPropertiesBySeller(@PathVariable Long sellerId) {
        try {
            // Fetch only available rental properties for the seller
            List<Property> properties = propertyRepository.findBySellerIdAndTypeAndStatusWithRelationships(
                sellerId, Property.PropertyType.RENT, Property.PropertyStatus.AVAILABLE);
            List<PropertyDTO> propertyDTOs = properties.stream()
                    .map(propertyMapper::toDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(propertyDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/rental")
    public ResponseEntity<?> getAllRentalProperties() {
        try {
            // Fetch all available rental properties
            List<Property> properties = propertyRepository.findByTypeAndStatusWithRelationships(
                Property.PropertyType.RENT, Property.PropertyStatus.AVAILABLE);
            List<PropertyDTO> propertyDTOs = properties.stream()
                    .map(propertyMapper::toDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(propertyDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImages(@RequestParam("files") MultipartFile[] files) {
        try {
            List<String> uploadedFiles = new ArrayList<>();
            
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir, "properties");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                
                // Validate file type
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.badRequest().body("Only image files are allowed");
                }
                
                // Generate unique filename
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename != null ? 
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
                String filename = UUID.randomUUID().toString() + extension;
                
                // Save file
                Path filePath = uploadPath.resolve(filename);
                Files.copy(file.getInputStream(), filePath);
                
                // Add to uploaded files list
                uploadedFiles.add("/uploads/properties/" + filename);
            }
            
            return ResponseEntity.ok(Map.of("uploadedFiles", uploadedFiles));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error uploading files: " + e.getMessage());
        }
    }

    @PostMapping("/create-with-images")
    public ResponseEntity<?> createPropertyWithImages(
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("location") String location,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "rentAmount", required = false) BigDecimal rentAmount,
            @RequestParam("description") String description,
            @RequestParam("sellerId") Long sellerId,
            @RequestParam(value = "files", required = false) MultipartFile[] files) {
        
        try {
            Optional<User> seller = userService.getUserById(sellerId);
            if (!seller.isPresent()) {
                return ResponseEntity.badRequest().body("Seller not found");
            }

            Property property = new Property();
            property.setTitle(title);
            property.setType(Property.PropertyType.valueOf(type.toUpperCase()));
            property.setLocation(location);
            property.setPrice(price);
            property.setRentAmount(rentAmount);
            property.setDescription(description);
            property.setSeller(seller.get());

            // Handle image uploads
            if (files != null && files.length > 0) {
                List<String> uploadedFiles = new ArrayList<>();
                
                try {
                    // Create upload directory if it doesn't exist
                    Path uploadPath = Paths.get(uploadDir, "properties");
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    for (MultipartFile file : files) {
                        if (file.isEmpty()) continue;
                        
                        // Validate file type
                        String contentType = file.getContentType();
                        if (contentType == null || !contentType.startsWith("image/")) {
                            continue; // Skip invalid files
                        }
                        
                        // Generate unique filename
                        String originalFilename = file.getOriginalFilename();
                        String extension = originalFilename != null ? 
                            originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
                        String filename = UUID.randomUUID().toString() + extension;
                        
                        // Save file
                        Path filePath = uploadPath.resolve(filename);
                        Files.copy(file.getInputStream(), filePath);
                        
                        // Add to uploaded files list
                        uploadedFiles.add("/uploads/properties/" + filename);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload images: " + e.getMessage(), e);
                }
                
                if (!uploadedFiles.isEmpty()) {
                    property.setMainImage(uploadedFiles.get(0)); // First image as main
                    property.setImages(String.join(",", uploadedFiles)); // All images as comma-separated
                }
            }

            Property createdProperty = propertyService.createProperty(property);
            PropertyDTO propertyDTO = propertyMapper.toDTO(createdProperty);

            return ResponseEntity.status(HttpStatus.CREATED).body(propertyDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Inner class for status update request
    public static class StatusUpdateRequest {
        private String status;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
