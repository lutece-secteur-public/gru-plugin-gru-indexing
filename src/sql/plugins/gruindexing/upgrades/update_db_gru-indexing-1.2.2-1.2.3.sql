--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'GRUSTORAGE_INDEXING';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('GRUSTORAGE_INDEXING','grustorageelastic.adminFeature.indexer.name',1,'jsp/admin/plugins/grustorageelastic/index/IndexElements.jsp','grustorageelastic.indexer.description',0,'grustorageelastic',NULL,NULL,NULL,0);

--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'GRUSTORAGE_INDEXING' AND id_user = 1;
INSERT INTO core_user_right VALUES ('GRUSTORAGE_INDEXING',1);
DELETE FROM core_user_right WHERE id_right = 'GRUSTORAGE_INDEXING' AND id_user = 2;
INSERT INTO core_user_right VALUES ('GRUSTORAGE_INDEXING',2);
