/*
 Navicat Premium Dump SQL

 Source Server         : pt_sofco
 Source Server Type    : PostgreSQL
 Source Server Version : 170008 (170008)
 Source Host           : ep-cool-frost-a1zyckp4-pooler.ap-southeast-1.aws.neon.tech:5432
 Source Catalog        : neondb
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 170008 (170008)
 File Encoding         : 65001

 Date: 12/04/2026 17:09:52
*/


-- ----------------------------
-- Type structure for user_role
-- ----------------------------
DROP TYPE IF EXISTS "public"."user_role";
CREATE TYPE "public"."user_role" AS ENUM (
  'SUPER ADMIN',
  'ADMIN',
  'MODERATOR',
  'USER'
);

-- ----------------------------
-- Type structure for userrole
-- ----------------------------
DROP TYPE IF EXISTS "public"."userrole";
CREATE TYPE "public"."userrole" AS ENUM (
  'ADMIN',
  'MODERATOR',
  'SUPER_ADMIN',
  'USER'
);

-- ----------------------------
-- Table structure for attendances
-- ----------------------------
DROP TABLE IF EXISTS "public"."attendances";
CREATE TABLE "public"."attendances" (
  "id" uuid NOT NULL DEFAULT uuid_generate_v4(),
  "user_id" uuid NOT NULL,
  "username" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "attendance_date" date NOT NULL DEFAULT CURRENT_DATE,
  "attendance_time_in" time(6) NOT NULL DEFAULT CURRENT_TIME,
  "attendance_time_in_latitude" numeric(11,8) NOT NULL,
  "attendance_time_in_longitude" numeric(11,8) NOT NULL,
  "attendance_time_out" time(6),
  "attendance_time_out_latitude" numeric(11,8),
  "attendance_time_out_longitude" numeric(11,8),
  "photo_url" varchar(255) COLLATE "pg_catalog"."default",
  "is_valid" bool DEFAULT true,
  "created_at" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Records of attendances
-- ----------------------------
INSERT INTO "public"."attendances" VALUES ('7f21ad6f-7d5e-4010-b17c-5e0ba7216fdb', '8692e5d0-e9cd-4700-8967-48fbffcd9b01', 'tester', '2026-04-11', '13:53:59.864', -0.47817150, 117.16548390, NULL, NULL, NULL, 'https://firebasestorage.googleapis.com/v0/b/fir-novendra-portfolio.firebasestorage.app/o/gaji.id%2Fattendance%2F3232a8e8-edbd-485b-affc-dcb376685dd2-CAP1769659119979086275.jpg?alt=media', 't', '2026-04-12 05:53:59.898096+00', '2026-04-12 05:53:59.898096+00');
INSERT INTO "public"."attendances" VALUES ('7f21ad6f-7d5e-4010-b17c-5e0ba7116fdb', '8692e5d0-e9cd-4700-8967-48fbffcd9b01', 'tester', '2026-04-10', '13:53:59.864', -0.47817150, 117.16548390, NULL, NULL, NULL, 'https://firebasestorage.googleapis.com/v0/b/fir-novendra-portfolio.firebasestorage.app/o/gaji.id%2Fattendance%2F3232a8e8-edbd-485b-affc-dcb376685dd2-CAP1769659119979086275.jpg?alt=media', 't', '2026-04-12 05:53:59.898096+00', '2026-04-12 05:57:39.930687+00');
INSERT INTO "public"."attendances" VALUES ('d477daec-4de7-4c88-af11-98b0b11d8590', '8692e5d0-e9cd-4700-8967-48fbffcd9b01', 'tester', '2026-04-12', '14:07:09.171', -0.47812080, 117.16534000, NULL, NULL, NULL, 'https://firebasestorage.googleapis.com/v0/b/fir-novendra-portfolio.firebasestorage.app/o/gaji.id%2Fattendance%2F9220ba8c-7e0c-4f02-8f9d-42afba96128d-CAP4407080868019408245.jpg?alt=media', 't', '2026-04-12 06:07:09.172457+00', '2026-04-12 06:07:09.172457+00');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS "public"."users";
CREATE TABLE "public"."users" (
  "id" uuid NOT NULL DEFAULT uuid_generate_v4(),
  "username" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "email" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "password_hash" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "full_name" varchar(100) COLLATE "pg_catalog"."default",
  "role" "public"."user_role" NOT NULL DEFAULT 'USER'::user_role,
  "is_active" bool NOT NULL DEFAULT true,
  "created_at" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO "public"."users" VALUES ('8692e5d0-e9cd-4700-8967-48fbffcd9b01', 'tester', 'tester@example.com', '$2a$10$N6EE.3CfPKJqylP/C0V/QeENODqIqD2b.ipT035SiI/9TnzKN.55e', 'User123', 'USER', 't', '2026-04-10 08:14:08.964592+00', '2026-04-12 05:54:22.327272+00');

-- ----------------------------
-- Function structure for update_updated_at_column
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."update_updated_at_column"();
CREATE FUNCTION "public"."update_updated_at_column"()
  RETURNS "pg_catalog"."trigger" AS $BODY$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

-- ----------------------------
-- Function structure for uuid_generate_v1
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."uuid_generate_v1"();
CREATE FUNCTION "public"."uuid_generate_v1"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_generate_v1'
  LANGUAGE c VOLATILE STRICT
  COST 1;

-- ----------------------------
-- Function structure for uuid_generate_v1mc
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."uuid_generate_v1mc"();
CREATE FUNCTION "public"."uuid_generate_v1mc"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_generate_v1mc'
  LANGUAGE c VOLATILE STRICT
  COST 1;

-- ----------------------------
-- Function structure for uuid_generate_v3
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."uuid_generate_v3"("namespace" uuid, "name" text);
CREATE FUNCTION "public"."uuid_generate_v3"("namespace" uuid, "name" text)
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_generate_v3'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for uuid_generate_v4
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."uuid_generate_v4"();
CREATE FUNCTION "public"."uuid_generate_v4"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_generate_v4'
  LANGUAGE c VOLATILE STRICT
  COST 1;

-- ----------------------------
-- Function structure for uuid_generate_v5
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."uuid_generate_v5"("namespace" uuid, "name" text);
CREATE FUNCTION "public"."uuid_generate_v5"("namespace" uuid, "name" text)
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_generate_v5'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for uuid_nil
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."uuid_nil"();
CREATE FUNCTION "public"."uuid_nil"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_nil'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for uuid_ns_dns
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."uuid_ns_dns"();
CREATE FUNCTION "public"."uuid_ns_dns"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_ns_dns'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for uuid_ns_oid
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."uuid_ns_oid"();
CREATE FUNCTION "public"."uuid_ns_oid"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_ns_oid'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for uuid_ns_url
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."uuid_ns_url"();
CREATE FUNCTION "public"."uuid_ns_url"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_ns_url'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Function structure for uuid_ns_x500
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."uuid_ns_x500"();
CREATE FUNCTION "public"."uuid_ns_x500"()
  RETURNS "pg_catalog"."uuid" AS '$libdir/uuid-ossp', 'uuid_ns_x500'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;

-- ----------------------------
-- Indexes structure for table attendances
-- ----------------------------
CREATE INDEX "idx_attendances_date" ON "public"."attendances" USING btree (
  "attendance_date" "pg_catalog"."date_ops" ASC NULLS LAST
);
CREATE INDEX "idx_attendances_user_id" ON "public"."attendances" USING btree (
  "user_id" "pg_catalog"."uuid_ops" ASC NULLS LAST
);

-- ----------------------------
-- Triggers structure for table attendances
-- ----------------------------
CREATE TRIGGER "trg_attendances_updated_at" BEFORE UPDATE ON "public"."attendances"
FOR EACH ROW
EXECUTE PROCEDURE "public"."update_updated_at_column"();

-- ----------------------------
-- Primary Key structure for table attendances
-- ----------------------------
ALTER TABLE "public"."attendances" ADD CONSTRAINT "attendances_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table users
-- ----------------------------
CREATE INDEX "idx_users_username" ON "public"."users" USING btree (
  "username" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Triggers structure for table users
-- ----------------------------
CREATE TRIGGER "trg_users_updated_at" BEFORE UPDATE ON "public"."users"
FOR EACH ROW
EXECUTE PROCEDURE "public"."update_updated_at_column"();

-- ----------------------------
-- Uniques structure for table users
-- ----------------------------
ALTER TABLE "public"."users" ADD CONSTRAINT "users_username_key" UNIQUE ("username");
ALTER TABLE "public"."users" ADD CONSTRAINT "users_email_key" UNIQUE ("email");

-- ----------------------------
-- Primary Key structure for table users
-- ----------------------------
ALTER TABLE "public"."users" ADD CONSTRAINT "users_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table attendances
-- ----------------------------
ALTER TABLE "public"."attendances" ADD CONSTRAINT "attendances_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "public"."users" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;
