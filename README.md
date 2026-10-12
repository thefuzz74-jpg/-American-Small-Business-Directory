# American Small Business Directory

Android-first directory for independently owned, U.S.-owned small businesses.

## Listing policy
- Independently owned and operated in the United States.
- U.S. ownership and control verified using business records and disclosures.
- No foreign parent-company ownership or control.
- Owner birthplace is not an eligibility criterion.
- Listings require manual administrator approval and an active **$1.99/month** subscription.
- Do not publish submitted verification documents or sensitive personal data.

## Planned features
- Search businesses by name, category, city, state and ZIP.
- Address, map view, directions, contact details and business photos.
- Owner application, document submission and administrative review.
- Subscription checkout, webhook-based status updates and automatic listing suspension when unpaid.
- Fraud reports, review audit trail, and periodic reverification.

## Architecture
Android app (Kotlin/Jetpack Compose), backend authentication and database, protected document storage, server-side payment integration, and a separate admin review interface.

**Status:** Project specification initialized. Android source code, payment integration, and APK workflow have not yet been implemented. Never treat a client-side payment flag as proof of payment.
