# American Small Business Directory

Android-first directory for independently owned, U.S.-owned small businesses.

## Listing policy
- Independently owned and operated in the United States.
- U.S. ownership and control verified using business records and disclosures.
- No foreign parent-company ownership or control.
- Owner birthplace is not an eligibility criterion.
- Listings require manual administrator approval and an active **$1.99/month** subscription.
- Do not publish submitted verification documents or sensitive personal data.

## Advertising pricing
- **Front-page Business Spotlight:** $1.99 per month or $9.99 per year (annual special; save $13.89 versus 12 monthly payments).
- This is the **Spotlight advertisement price**, separate from the standard business listing subscription.
- Spotlight includes a business photo, description, and link to its approved business profile.
- Spotlight submissions and photos require administrator approval before publication.
- Billing and paid placement must be verified server-side; pricing in the preview is not an active checkout.

## Planned features
- Search businesses by name, category, city, state and ZIP.
- Address, map view, directions, contact details and business photos.
- Owner application, document submission and administrative review.
- Subscription checkout, webhook-based status updates and automatic listing suspension when unpaid.
- Fraud reports, review audit trail, and periodic reverification.

## Architecture
Android app (Kotlin/Jetpack Compose), backend authentication and database, protected document storage, server-side payment integration, and a separate admin review interface.

**Status:** An initial Android WebView prototype and GitHub Actions debug APK build workflow have been added. The prototype displays the Business Spotlight preview; production directory features, authentication, secure photo uploads, admin approval, and payment integration remain unimplemented. The APK build has not been verified as successful. Never treat a client-side payment flag as proof of payment.
