-- Spotlight submissions must never be public before administrator approval.
-- All reads and writes must be protected by server-side authorization.
create table if not exists spotlight_submissions (
 id uuid primary key default gen_random_uuid(),
 business_id uuid not null references businesses(id) on delete cascade,
 submitted_by uuid not null,
 title text not null,
 spotlight_type text not null check (spotlight_type in ('product','service','sale','event')),
 description text not null,
 price_text text,
 image_object_key text,
 status text not null default 'pending' check (status in ('pending','approved','rejected','needs_changes')),
 submitted_at timestamptz not null default now(),
 reviewed_by uuid,
 reviewed_at timestamptz,
 review_notes text
);
-- New edits should create a new pending submission; don't overwrite approved records.
-- Media must be stored privately until approved and scanned.
create or replace view public_spotlights as
 select p.id, p.business_id, p.title, p.spotlight_type, p.description,
        p.price_text, p.image_object_key, b.display_name, b.city, b.state
 from spotlight_submissions p
 join businesses b on b.id = p.business_id
 join verification_reviews v on v.business_id = b.id
 join subscriptions s on s.business_id = b.id
 where p.status = 'approved' and v.status = 'approved'
   and s.status = 'active' and s.current_period_end > now();
