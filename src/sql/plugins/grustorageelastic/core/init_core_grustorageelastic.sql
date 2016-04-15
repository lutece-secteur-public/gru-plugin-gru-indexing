--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'ES_INDEXATION';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('ES_INDEXATION','grustorageelastic.adminFeature.IndexerES.name',1,'jsp/admin/plugins/grustorageelastic/IndexerES.jsp','grustorageelastic.IndexerES.description',0,'grustorageelastic',NULL,NULL,NULL,11);

