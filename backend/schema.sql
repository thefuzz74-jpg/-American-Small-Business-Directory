-- Initial PostgreSQL data model. Apply only after configuring authenticated
-- backend, secret management, payment webhooks and secure document storage.
create extension if not exists pgcrypto;
create table if not exists businesses (
 id uuid primary key default gen_random_uuid(),
 owner_user_id uuid not null,
 legal_name text not null,
 display_name text not null,
 category text not null,
 description text not null default '',
 street_address text not null,
 city text not null,
 state text not null,
 zip_code text not null,
 phone text,
 website text,
 created_at timestamptz not null default now()
);
create table if not exists verification_reviews (
 business_id uuid primary key references businesses(id) on delete cascade,
 status text not null default 'pending' check(status in ('pending','needs_information','approved','rejected','suspended')),
 reviewed_by uuid,
 reviewed_at timestamptz,
 review_notes text,
 updated_at timestamptz not null default now()
);
create table if not exists subscriptions (
 business_id uuid primary key references businesses(id) on delete cascade,
 payment_provider_customer_id text,
 payment_provider_subscription_id text unique,
 status text not null default 'inactive' check(status in ('inactive','active','past_due','canceled')),
 current_period_end timestamptz,
 updated_at timestamptz not null default now()
);
create table if not exists verification_documents (
 id uuid primary key default gen_random_uuid(),
 business_id uuid not null references businesses(id) on delete cascade,
 document_type text not null,
 secure_object_key text not null,
 uploaded_at timestamptz not null default now()
);
create table if not exists business_promotions (
 id uuid primary key default gen_random_uuid(),
 business_id uuid not null references businesses(id) on delete cascade,
 title text not null,
 description text not null default '',
 starts_at timestamptz,
 ends_at timestamptz
);
create table if not exists review_audit (
 id uuid primary key default gen_random_uuid(),
 business_id uuid not null references businesses(id) on delete cascade,
 actor_user_id uuid not null,
 action text not null,
 recorded_at timestamptz not null default now()
);
-- Do not grant table access to anonymous clients.
-- Application server must enforce ownership, admin role, and payment webhook authorization.
-- Public directory uses a read-only view with no private ownership or document fields.
create or replace view public_directory as
 select b.id, b.display_name, b.category, b.description, b.street_address,
        b.city, b.state, b.zip_code, b.phone, b.website
 from businesses b
 join verification_reviews v on v.business_id = b.id
 join subscriptions s on s.business_id = b.id
 where v.status = 'approved' and s.status = 'active'
 and s.current_period_end > now();
