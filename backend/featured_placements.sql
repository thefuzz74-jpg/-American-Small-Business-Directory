-- Optional paid front-page placements.
-- Only server-verified payments and administrator approvals may activate placement.
create table if not exists featured_placements (
 id uuid primary key default gen_random_uuid(),
 business_id uuid not null references businesses(id) on delete cascade,
 spotlight_submission_id uuid references spotlight_submissions(id),
 headline text not null,
 description text not null default '',
 image_object_key text,
 review_status text not null default 'pending' check(review_status in ('pending','approved','rejected','needs_changes')),
 payment_status text not null default 'unpaid' check(payment_status in ('unpaid','paid','refunded')),
 price_cents integer not null default 499 check (price_cents = 499),
 duration_days integer not null default 30 check (duration_days = 30),
 currency text not null default 'usd' check (currency = 'usd'),
 payment_provider_reference text unique,
 starts_at timestamptz,
 ends_at timestamptz,
 reviewed_by uuid,
 reviewed_at timestamptz,
 created_at timestamptz not null default now()
);
-- $4.99 USD purchases one 30-day placement, not a recurring subscription.
-- Start and end dates must be assigned by the trusted backend only after payment and approval.
-- Never trust a client-set payment status or admin approval.
create or replace view public_featured_placements as
 select f.id, f.business_id, f.headline, f.description, f.image_object_key,
        f.starts_at, f.ends_at, b.display_name, b.city, b.state, b.category
 from featured_placements f
 join businesses b on b.id = f.business_id
 join verification_reviews v on v.business_id = b.id
 join subscriptions s on s.business_id = b.id
 where f.review_status = 'approved' and f.payment_status = 'paid'
   and f.starts_at <= now() and f.ends_at > now()
   and v.status = 'approved' and s.status = 'active'
   and s.current_period_end > now();
