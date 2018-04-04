INSERT INTO users(username,password,enabled) VALUES ('mkyong','123456', 1);
INSERT INTO users(username,password,enabled) VALUES ('alex','123456', 1);
INSERT INTO users(username,password,enabled) VALUES ('cici','123456', 1);

INSERT INTO user_roles (user_role_id,username, role) VALUES (user_role_sequence.nextval,'mkyong', 'USER');
INSERT INTO user_roles (user_role_id,username, role) VALUES (user_role_sequence.nextval,'mkyong', 'ADMIN');
INSERT INTO user_roles (user_role_id,username, role) VALUES (user_role_sequence.nextval,'alex', 'USER');
INSERT INTO user_roles (user_role_id,username, role) VALUES (user_role_sequence.nextval,'cici', 'USER');

insert into oauth_client_details ( client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove ) values ( 'ssoclient', '', 'secret', 'read', 'password,client_credentials,authorization_code', '', '', 36000, 36000, '', 'true' );
insert into oauth_client_details ( client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove ) values ( 'apiclient', '', 'secret', 'read', 'password,refresh_token', '', '', 36000, 36000, '', 'true' );
