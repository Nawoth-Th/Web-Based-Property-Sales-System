# Email Configuration Setup

This document explains how to configure the email notification system for the Property Management System.

## Prerequisites

1. **Gmail Account** (recommended for development)
2. **App Password** (not regular password)

## Gmail Setup

### Step 1: Enable 2-Factor Authentication
1. Go to your Google Account settings
2. Navigate to Security
3. Enable 2-Step Verification

### Step 2: Generate App Password
1. Go to Google Account settings
2. Navigate to Security
3. Under "2-Step Verification", click "App passwords"
4. Select "Mail" and "Other (custom name)"
5. Enter "PropertyHub" as the name
6. Copy the generated 16-character password

## Environment Variables

Set the following environment variables:

### Windows (Command Prompt)
```cmd
set MAIL_USERNAME=your-email@gmail.com
set MAIL_PASSWORD=your-16-character-app-password
```

### Windows (PowerShell)
```powershell
$env:MAIL_USERNAME="your-email@gmail.com"
$env:MAIL_PASSWORD="your-16-character-app-password"
```

### Linux/Mac
```bash
export MAIL_USERNAME="your-email@gmail.com"
export MAIL_PASSWORD="your-16-character-app-password"
```

## Application Properties

The email configuration is already set in `application.properties`:

```properties
# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME:}
spring.mail.password=${MAIL_PASSWORD:}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# Email Settings
# Using Gmail address as sender (no custom domain needed for university project)
```

## Email Notifications

The system sends email notifications for:

### Rental Agreements
- **Creation**: Notifies tenant and landlord when agreement is created
- **Status Updates**: Notifies when agreement status changes (ACTIVE, TERMINATED, EXPIRED)

### Bookings
- **Creation**: Notifies buyer and seller when booking is created
- **Status Updates**: Notifies when booking status changes (PENDING, CONFIRMED, CANCELLED)

### Inquiries
- **Creation**: Notifies sender and property owner when inquiry is submitted
- **Status Updates**: Notifies when inquiry status changes (OPEN, RESOLVED, ARCHIVED)

### Offers
- **Creation**: Notifies buyer and seller when offer is submitted
- **Status Updates**: Notifies when offer status changes (PENDING, ACCEPTED, REJECTED, EXPIRED)

## Testing Email Configuration

1. Start the application
2. Create a new rental agreement, booking, inquiry, or offer
3. Check the console logs for email sending status
4. Verify emails are received in the respective email accounts

## Troubleshooting

### Common Issues

1. **Authentication Failed**
   - Ensure you're using App Password, not regular password
   - Verify 2-Factor Authentication is enabled

2. **Connection Timeout**
   - Check internet connection
   - Verify Gmail SMTP settings

3. **Emails Not Sent**
   - Check console logs for error messages
   - Verify email addresses are valid
   - Ensure environment variables are set correctly

### Debug Mode

To enable detailed email logging, add to `application.properties`:

```properties
logging.level.org.springframework.mail=DEBUG
```

## Security Notes

- Never commit email credentials to version control
- Use environment variables for sensitive information
- For university projects, Gmail is sufficient and doesn't require a custom domain
- App passwords are more secure than regular passwords for applications
