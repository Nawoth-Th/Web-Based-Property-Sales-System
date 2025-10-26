// API Base URL
const API_BASE = "/api"

// Utility function for making API calls
async function apiCall(endpoint, options = {}) {
    const url = `${API_BASE}${endpoint}`
    const config = {
        headers: {
            "Content-Type": "application/json",
            ...options.headers,
        },
        ...options,
    }

    console.log("[v0] Making API call to:", url)
    console.log("[v0] Request config:", config)

    try {
        const response = await fetch(url, config)

        console.log("[v0] Response status:", response.status)
        console.log("[v0] Response ok:", response.ok)

        if (!response.ok) {
            const errorText = await response.text()
            console.error("[v0] API error response:", errorText)
            throw new Error(errorText || `HTTP error! status: ${response.status}`)
        }

        const contentType = response.headers.get("content-type")
        if (contentType && contentType.includes("application/json")) {
            const jsonData = await response.json()
            console.log("[v0] Response data:", jsonData)
            return jsonData
        } else {
            const textData = await response.text()
            console.log("[v0] Response text:", textData)
            return textData
        }
    } catch (error) {
        console.error("[v0] API call failed:", error)
        throw error
    }
}

// User Management Functions
async function registerUser(userData) {
    return await apiCall("/users/register", {
        method: "POST",
        body: JSON.stringify(userData),
    })
}

async function loginUser(username, password) {
    console.log("[v0] Attempting login for username:", username)

    try {
        const result = await apiCall("/users/login", {
            method: "POST",
            body: JSON.stringify({ username, password }),
        })

        console.log("[v0] Login successful, user data:", result)
        return result
    } catch (error) {
        console.error("[v0] Login failed:", error.message)
        throw error
    }
}

async function fetchUserById(userId) {
    return await apiCall(`/users/${userId}`)
}

// Property Management Functions
async function fetchAllProperties(filters = {}) {
    console.log("[v0] Fetching properties with filters:", filters)
    const params = new URLSearchParams()

    Object.keys(filters).forEach((key) => {
        if (filters[key] !== null && filters[key] !== undefined && filters[key] !== "") {
            params.append(key, filters[key])
        }
    })

    const queryString = params.toString()
    const endpoint = queryString ? `/properties?${queryString}` : "/properties"

    console.log("[v0] Fetching from endpoint:", endpoint)

    try {
        const result = await apiCall(endpoint)
        console.log("[v0] Properties fetched successfully:", result.length, "properties")
        return result
    } catch (error) {
        console.error("[v0] Failed to fetch properties:", error)
        throw error
    }
}

async function fetchPropertyById(propertyId) {
    console.log("[v0] Fetching property by ID:", propertyId)
    const result = await apiCall(`/properties/${propertyId}`)
    console.log("[v0] Property fetched:", result)
    return result
}

async function createProperty(propertyData) {
    return await apiCall("/properties", {
        method: "POST",
        body: JSON.stringify(propertyData),
    })
}

async function updateProperty(propertyId, propertyData) {
    return await apiCall(`/properties/${propertyId}`, {
        method: "PUT",
        body: JSON.stringify(propertyData),
    })
}

async function deleteProperty(propertyId) {
    return await apiCall(`/properties/${propertyId}`, {
        method: "DELETE",
    })
}

async function fetchPropertiesBySeller(sellerId) {
    console.log("[v0] Fetching properties for seller:", sellerId)
    const result = await apiCall(`/properties/seller/${sellerId}`)
    console.log("[v0] Seller properties fetched:", result)
    return result
}

async function fetchAllProperties() {
    console.log("[v0] Fetching all properties")
    const result = await apiCall("/properties")
    console.log("[v0] All properties fetched:", result)
    return result
}

// Booking Management Functions
async function createBooking(bookingData) {
    return await apiCall("/bookings", {
        method: "POST",
        body: JSON.stringify(bookingData),
    })
}

async function fetchAllBookings() {
    return await apiCall("/bookings")
}

async function fetchBookingsByBuyer(buyerId) {
    return await apiCall(`/bookings/buyer/${buyerId}`)
}

async function fetchBookingsByProperty(propertyId) {
    return await apiCall(`/bookings/property/${propertyId}`)
}

async function updateBookingStatus(bookingId, status) {
    return await apiCall(`/bookings/${bookingId}/status`, {
        method: "PUT",
        body: JSON.stringify({ status }),
    })
}

async function deleteBooking(bookingId) {
    return await apiCall(`/bookings/${bookingId}`, {
        method: "DELETE",
    })
}

// Inquiry Management Functions
async function fetchAllInquiries() {
    return await apiCall("/inquiries")
}

async function fetchInquiryById(inquiryId) {
    return await apiCall(`/inquiries/${inquiryId}`)
}

async function createInquiry(inquiryData) {
    return await apiCall("/inquiries", {
        method: "POST",
        body: JSON.stringify(inquiryData),
    })
}

async function updateInquiry(inquiryId, inquiryData) {
    return await apiCall(`/inquiries/${inquiryId}`, {
        method: "PUT",
        body: JSON.stringify(inquiryData),
    })
}

async function updateInquiryStatus(inquiryId, status) {
    return await apiCall(`/inquiries/${inquiryId}/status`, {
        method: "PATCH",
        body: JSON.stringify({ status }),
    })
}

async function fetchInquiriesByProperty(propertyId) {
    return await apiCall(`/inquiries/property/${propertyId}`)
}

async function fetchInquiriesBySender(senderId) {
    return await apiCall(`/inquiries/sender/${senderId}`)
}

async function fetchInquiriesByStatus(status) {
    return await apiCall(`/inquiries/status/${status}`)
}

async function fetchOpenInquiries() {
    return await apiCall("/inquiries/open")
}

async function archiveOldInquiries() {
    return await apiCall("/inquiries/archive-old", {
        method: "POST",
    })
}

async function deleteInquiry(inquiryId) {
    return await apiCall(`/inquiries/${inquiryId}`, {
        method: "DELETE",
    })
}

// Offer Management Functions
async function fetchAllOffers() {
    return await apiCall("/offers")
}

async function fetchOfferById(offerId) {
    return await apiCall(`/offers/${offerId}`)
}

async function createOffer(offerData) {
    return await apiCall("/offers", {
        method: "POST",
        body: JSON.stringify(offerData),
    })
}

async function updateOffer(offerId, offerData) {
    return await apiCall(`/offers/${offerId}`, {
        method: "PUT",
        body: JSON.stringify(offerData),
    })
}

async function updateOfferStatus(offerId, status) {
    return await apiCall(`/offers/${offerId}/status`, {
        method: "PATCH",
        body: JSON.stringify({ status }),
    })
}

async function acceptOffer(offerId) {
    return await apiCall(`/offers/${offerId}/accept`, {
        method: "POST",
    })
}

async function counterOffer(offerId, counterOfferData) {
    return await apiCall(`/offers/${offerId}/counter`, {
        method: "POST",
        body: JSON.stringify(counterOfferData),
    })
}

async function fetchOffersByProperty(propertyId) {
    return await apiCall(`/offers/property/${propertyId}`)
}

async function fetchOffersByBuyer(buyerId) {
    return await apiCall(`/offers/buyer/${buyerId}`)
}

async function fetchOffersByStatus(status) {
    return await apiCall(`/offers/status/${status}`)
}

async function fetchPendingOffers() {
    return await apiCall("/offers/pending")
}

async function expireOldOffers() {
    return await apiCall("/offers/expire-old", {
        method: "POST",
    })
}

async function deleteOffer(offerId) {
    return await apiCall(`/offers/${offerId}`, {
        method: "DELETE",
    })
}

// Rental Agreement Management Functions
async function fetchAllRentalAgreements() {
    return await apiCall("/rental-agreements")
}

async function fetchRentalAgreementById(agreementId) {
    return await apiCall(`/rental-agreements/${agreementId}`)
}

async function createRentalAgreement(agreementData) {
    return await apiCall("/rental-agreements", {
        method: "POST",
        body: JSON.stringify(agreementData),
    })
}

async function updateRentalAgreement(agreementId, agreementData) {
    return await apiCall(`/rental-agreements/${agreementId}`, {
        method: "PUT",
        body: JSON.stringify(agreementData),
    })
}

async function updateRentalAgreementStatus(agreementId, status) {
    return await apiCall(`/rental-agreements/${agreementId}/status`, {
        method: "PATCH",
        body: JSON.stringify({ status }),
    })
}

async function extendRentalAgreement(agreementId, additionalMonths) {
    return await apiCall(`/rental-agreements/${agreementId}/extend`, {
        method: "PATCH",
        body: JSON.stringify({ additionalMonths }),
    })
}

async function fetchRentalAgreementsByProperty(propertyId) {
    return await apiCall(`/rental-agreements/property/${propertyId}`)
}

async function fetchRentalAgreementsByTenant(tenantId) {
    return await apiCall(`/rental-agreements/tenant/${tenantId}`)
}

async function fetchRentalAgreementsByLandlord(landlordId) {
    return await apiCall(`/rental-agreements/landlord/${landlordId}`)
}

async function fetchRentalAgreementsByStatus(status) {
    return await apiCall(`/rental-agreements/status/${status}`)
}

async function fetchExpiringAgreements(days = 30) {
    return await apiCall(`/rental-agreements/expiring?days=${days}`)
}

async function deleteRentalAgreement(agreementId) {
    return await apiCall(`/rental-agreements/${agreementId}`, {
        method: "DELETE",
    })
}

// UI Helper Functions
function formatPrice(price, type) {
    if (!price) return "N/A"
    const formatted = new Intl.NumberFormat("en-LK", {
        style: "currency",
        currency: "LKR",
        minimumFractionDigits: 0,
        maximumFractionDigits: 0,
    }).format(price)

    return type === "RENT" ? `${formatted}/month` : formatted
}

function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString("en-US", {
        year: "numeric",
        month: "long",
        day: "numeric",
    })
}

function getStatusClass(status) {
    const statusMap = {
        AVAILABLE: "status-available",
        SOLD: "status-sold",
        RENTED: "status-rented",
        PENDING: "status-pending",
        CONFIRMED: "status-confirmed",
        CANCELLED: "status-cancelled",
    }
    return statusMap[status] || "status-default"
}

function formatRentalStatus(status) {
    const statusMap = {
        ACTIVE: "Active",
        EXPIRED: "Expired",
        TERMINATED: "Terminated",
    }
    return statusMap[status] || status
}

function formatBookingStatus(status) {
    const statusMap = {
        PENDING: "Pending",
        CONFIRMED: "Confirmed",
        CANCELLED: "Cancelled",
    }
    return statusMap[status] || status
}

function formatInquiryStatus(status) {
    const statusMap = {
        OPEN: "Open",
        RESOLVED: "Resolved",
        ARCHIVED: "Archived",
    }
    return statusMap[status] || status
}

function formatOfferStatus(status) {
    const statusMap = {
        PENDING: "Pending",
        ACCEPTED: "Accepted",
        REJECTED: "Rejected",
        COUNTERED: "Countered",
        EXPIRED: "Expired",
    }
    return statusMap[status] || status
}

function getInquiryStatusClass(status) {
    const statusMap = {
        OPEN: "status-pending",
        RESOLVED: "status-confirmed",
        ARCHIVED: "status-cancelled",
    }
    return statusMap[status] || "status-default"
}

function getOfferStatusClass(status) {
    const statusMap = {
        PENDING: "status-pending",
        ACCEPTED: "status-confirmed",
        REJECTED: "status-cancelled",
        COUNTERED: "status-warning",
        EXPIRED: "status-cancelled",
    }
    return statusMap[status] || "status-default"
}

// Property Display Functions
function createPropertyCard(property) {
    const price = property.type === "SALE" ? formatPrice(property.price) : formatPrice(property.rentAmount, "RENT")

    return `
        <div class="property-card">
            ${property.mainImage ? 
                `<img src="${property.mainImage}" alt="${property.title}" class="property-image" onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                 <div class="property-image-placeholder" style="display:none;"><i class="fas fa-home"></i></div>` : 
                `<div class="property-image-placeholder"><i class="fas fa-home"></i></div>`
            }
            <div class="property-content">
                <h3 class="property-title">${property.title || "Untitled Property"}</h3>
                <p class="property-location"><i class="fas fa-map-marker-alt"></i> ${property.location || "Location not specified"}</p>
                <div class="property-price">${price}</div>
                <span class="property-type">${property.type || "N/A"}</span>
                <span class="status-badge ${getStatusClass(property.status)}">${property.status || "UNKNOWN"}</span>
                <p class="property-description">${property.description || "No description available"}</p>
                <div class="property-actions">
                    <a href="/property.html?id=${property.id}" class="btn btn-primary">View Details</a>
                    <button class="btn btn-outline" onclick="quickContact(${property.id})">Quick Contact</button>
                </div>
            </div>
        </div>
    `
}

function displayProperties(properties, containerId = "propertiesGrid") {
    console.log("[v0] Displaying", properties.length, "properties")
    const container = document.getElementById(containerId)

    if (!container) {
        console.error("[v0] Container not found:", containerId)
        return
    }

    if (!properties || properties.length === 0) {
        container.innerHTML = '<div class="text-center"><p>No properties found matching your criteria.</p></div>'
        return
    }

    container.innerHTML = properties.map(createPropertyCard).join("")
    console.log("[v0] Properties displayed successfully")
}

// Main page functions
async function loadProperties() {
    console.log("[v0] loadProperties() called")
    const container = document.getElementById("propertiesGrid")
    const errorElement = document.getElementById("error")
    const loadingElement = document.getElementById("loading")

    if (!container) {
        console.error("[v0] Properties grid container not found!")
        return
    }

    try {
        console.log("[v0] Showing loading state...")
        if (loadingElement) loadingElement.classList.remove("hidden")
        if (errorElement) errorElement.classList.add("hidden")

        console.log("[v0] Calling fetchAllProperties...")
        const properties = await fetchAllProperties({ status: "AVAILABLE" })

        console.log("[v0] Properties loaded successfully:", properties.length)

        if (loadingElement) loadingElement.classList.add("hidden")

        displayProperties(properties)
    } catch (error) {
        console.error("[v0] Failed to load properties:", error)
        if (loadingElement) loadingElement.classList.add("hidden")
        showError("Failed to load properties. Please try again later.")
    }
}

async function searchProperties() {
    const filters = {
        type: document.getElementById("type")?.value || "",
        location: document.getElementById("location")?.value || "",
        minPrice: document.getElementById("minPrice")?.value || "",
        maxPrice: document.getElementById("maxPrice")?.value || "",
        status: "AVAILABLE",
    }

    try {
        showLoading(true)
        const properties = await fetchAllProperties(filters)
        displayProperties(properties)
        showLoading(false)
    } catch (error) {
        console.error("Search failed:", error)
        showError("Search failed. Please try again.")
        showLoading(false)
    }
}

function showLoading(show) {
    const loadingElement = document.getElementById("loading")
    if (loadingElement) {
        loadingElement.classList.toggle("hidden", !show)
    }
}

function showError(message) {
    console.error("[v0] Showing error:", message)
    const errorElement = document.getElementById("error")
    if (errorElement) {
        errorElement.textContent = message
        errorElement.classList.remove("hidden")
    }
}

function hideError() {
    const errorElement = document.getElementById("error")
    if (errorElement) {
        errorElement.classList.add("hidden")
    }
}

function quickContact(propertyId) {
    alert(`Quick contact feature for property ${propertyId} coming soon!`)
}

// Form validation helpers
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    return re.test(email)
}

function validatePassword(password) {
    return password.length >= 6
}

function validateRequired(value) {
    return value && value.trim().length > 0
}

// Local storage helpers
function saveToLocalStorage(key, data) {
    try {
        localStorage.setItem(key, JSON.stringify(data))
    } catch (error) {
        console.error("Failed to save to localStorage:", error)
    }
}

function getFromLocalStorage(key) {
    try {
        const data = localStorage.getItem(key)
        return data ? JSON.parse(data) : null
    } catch (error) {
        console.error("Failed to get from localStorage:", error)
        return null
    }
}

function removeFromLocalStorage(key) {
    try {
        localStorage.removeItem(key)
    } catch (error) {
        console.error("Failed to remove from localStorage:", error)
    }
}

// Utility functions for rental management
function calculateRentalEndDate(startDate, durationMonths) {
    const endDate = new Date(startDate)
    endDate.setMonth(endDate.getMonth() + durationMonths)
    return endDate
}

function getDaysUntilExpiry(startDate, durationMonths) {
    const endDate = calculateRentalEndDate(startDate, durationMonths)
    const today = new Date()
    const timeDiff = endDate.getTime() - today.getTime()
    return Math.ceil(timeDiff / (1000 * 3600 * 24))
}

// Initialize common functionality
document.addEventListener("DOMContentLoaded", () => {
    // Add smooth scrolling to all anchor links
    document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
        anchor.addEventListener("click", function (e) {
            e.preventDefault()
            const target = document.querySelector(this.getAttribute("href"))
            if (target) {
                target.scrollIntoView({
                    behavior: "smooth",
                })
            }
        })
    })

    // Add loading states to all forms
    document.querySelectorAll("form").forEach((form) => {
        form.addEventListener("submit", () => {
            const submitBtn = form.querySelector('button[type="submit"]')
            if (submitBtn) {
                submitBtn.disabled = true
                submitBtn.textContent = "Loading..."

                // Re-enable after 5 seconds as fallback
                setTimeout(() => {
                    submitBtn.disabled = false
                    submitBtn.textContent = submitBtn.getAttribute("data-original-text") || "Submit"
                }, 5000)
            }
        })
    })
})
