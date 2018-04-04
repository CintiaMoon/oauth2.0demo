CREATE TABLE USERS (
  USERNAME VARCHAR2(45 BYTE) NOT NULL
, PASSWORD VARCHAR2(45 BYTE) NOT NULL
, ENABLED NUMBER(*, 0) DEFAULT 1 NOT NULL
, CONSTRAINT USERS_PK PRIMARY KEY (USERNAME)
);

CREATE TABLE USER_ROLES (
  USER_ROLE_ID NUMBER(11, 0) NOT NULL
, USERNAME VARCHAR2(45 BYTE) NOT NULL
, ROLE VARCHAR2(45 BYTE) NOT NULL
, CONSTRAINT USER_ROLES_PK PRIMARY KEY (USER_ROLE_ID)
);

ALTER TABLE USER_ROLES
ADD CONSTRAINT UNI_USERNAME_ROLE UNIQUE (USERNAME, ROLE);

ALTER TABLE USER_ROLES
ADD CONSTRAINT USER_ROLES_FK1 FOREIGN KEY
(USERNAME) REFERENCES USERS (USERNAME) ENABLE;

CREATE SEQUENCE user_role_sequence;

create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);

create table oauth_client_token (
  token_id VARCHAR(256),
  token RAW(2000),
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256)
);

create table oauth_access_token (
  token_id VARCHAR(256),
  token RAW(2000),
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication LONG RAW,
  refresh_token VARCHAR(256)
);

create table oauth_refresh_token (
  token_id VARCHAR(256),
  token RAW(2000),
  authentication LONG RAW
);

create table oauth_code (
  code VARCHAR(256), authentication LONG RAW
);

create table oauth_approvals (
	userId VARCHAR(256),
	clientId VARCHAR(256),
	scope VARCHAR(256),
	status VARCHAR(10),
	expiresAt TIMESTAMP,
	lastModifiedAt TIMESTAMP
);

create table ClientDetails (
  appId VARCHAR(256) PRIMARY KEY,
  resourceIds VARCHAR(256),
  appSecret VARCHAR(256),
  scope VARCHAR(256),
  grantTypes VARCHAR(256),
  redirectUrl VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additionalInformation VARCHAR(4096),
  autoApproveScopes VARCHAR(256)
);