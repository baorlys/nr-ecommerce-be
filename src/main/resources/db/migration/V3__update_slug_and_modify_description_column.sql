-- Add new columns
ALTER TABLE products ADD COLUMN short_description VARCHAR(255);
ALTER TABLE products ADD COLUMN slug VARCHAR(255);
ALTER TABLE categories ADD COLUMN slug VARCHAR(255);

-- Change column type (description to TEXT)
ALTER TABLE products ALTER COLUMN description TYPE TEXT;

-- Make slug and name NOT NULL
ALTER TABLE products ALTER COLUMN slug SET NOT NULL;
ALTER TABLE categories ALTER COLUMN slug SET NOT NULL;
ALTER TABLE categories ALTER COLUMN name SET NOT NULL;

-- Add unique constraints
ALTER TABLE categories ADD CONSTRAINT uc_categories_name UNIQUE (name);
ALTER TABLE categories ADD CONSTRAINT uc_categories_slug UNIQUE (slug);
ALTER TABLE products ADD CONSTRAINT uc_products_slug UNIQUE (slug);
