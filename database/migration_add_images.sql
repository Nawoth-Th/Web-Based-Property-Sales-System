-- Migration script to add image columns to properties table
-- Run this script if you have an existing database without image columns

USE property_sales_db;

-- Add image columns to properties table
ALTER TABLE properties 
ADD COLUMN main_image VARCHAR(500) AFTER description,
ADD COLUMN images TEXT AFTER main_image;

-- Update existing properties with placeholder images (optional)
-- UPDATE properties SET main_image = '/uploads/properties/placeholder.jpg' WHERE main_image IS NULL;
