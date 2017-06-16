--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'ES_INDEXATION';
DELETE FROM core_admin_right WHERE id_right = 'GRUSTORAGE_INDEXING';

--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'GRUSTORAGE_INDEXING' AND id_user = 1;
DELETE FROM core_user_right WHERE id_right = 'GRUSTORAGE_INDEXING' AND id_user = 2;