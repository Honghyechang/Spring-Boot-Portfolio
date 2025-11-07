
INSERT INTO member(name, email, password) VALUES ('홍혜창','HyechangHong@spring.ac.kr','$2a$12$QWIo01qrkw4CuQdj/xZ.meJpuBB02UOxCplTXTdUc424f/aBbPU32');
INSERT INTO member(name, email, password) VALUES ('윤서준','SeojunYoon@spring.ac.kr','$2a$12$QWIo01qrkw4CuQdj/xZ.meJpuBB02UOxCplTXTdUc424f/aBbPU32');
INSERT INTO member(name, email, password) VALUES ('김우현','WoohyunKim@spring.ac.kr','$2a$12$QWIo01qrkw4CuQdj/xZ.meJpuBB02UOxCplTXTdUc424f/aBbPU32');
INSERT INTO member(name, email, password) VALUES ('손흥민','Sonny@spring.ac.kr','$2a$12$QWIo01qrkw4CuQdj/xZ.meJpuBB02UOxCplTXTdUc424f/aBbPU32');



INSERT INTO authority(authority,member_id) VALUES('ROLE_ADMIN',2);


INSERT INTO article(title,description,created,updated,member_id) VALUES ('첫 번째 게시글 제목','첫 번째 게시글 본문',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO article(title,description,created,updated,member_id) VALUES ('두 번째 게시글 제목','두 번째 게시글 본문',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,2);
INSERT INTO article(title,description,created,updated,member_id) VALUES ('세 번째 게시글 제목','세 번째 게시글 본문',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,3);
INSERT INTO article(title,description,created,updated,member_id) VALUES ('네 번째 게시글 제목','네 번째 게시글 본문',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,4);