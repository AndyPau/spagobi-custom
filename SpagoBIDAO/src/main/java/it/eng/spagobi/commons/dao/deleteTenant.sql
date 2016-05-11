DELETE FROM SBI_ARTIFACTS_VERSIONS WHERE ORGANIZATION = ?;
DELETE FROM SBI_ARTIFACTS WHERE ORGANIZATION = ?;
DELETE FROM SBI_META_MODELS_VERSIONS WHERE ORGANIZATION = ?;
DELETE FROM SBI_META_MODELS WHERE ORGANIZATION = ?;
DELETE FROM SBI_EXT_ROLES_CATEGORY WHERE EXT_ROLE_ID IN (SELECT t.EXT_ROLE_ID FROM SBI_EXT_ROLES t WHERE t.ORGANIZATION = ?);
DELETE FROM SBI_KPI_COMMENTS WHERE ORGANIZATION = ?;
DELETE FROM SBI_ORG_UNIT_GRANT_NODES WHERE ORGANIZATION = ?;
DELETE FROM SBI_ORG_UNIT_GRANT WHERE ORGANIZATION = ?;
DELETE FROM SBI_ORG_UNIT_NODES WHERE ORGANIZATION = ?;
DELETE FROM SBI_ORG_UNIT_HIERARCHIES WHERE ORGANIZATION = ?;
DELETE FROM SBI_ORG_UNIT WHERE ORGANIZATION = ?;
DELETE FROM SBI_DATA_SET WHERE ORGANIZATION = ?;
DELETE FROM SBI_OBJECTS_RATING WHERE ORGANIZATION = ?;
DELETE FROM SBI_REMEMBER_ME WHERE ORGANIZATION = ?;
DELETE FROM SBI_DOSSIER_PRES WHERE ORGANIZATION = ?;
DELETE FROM SBI_DOSSIER_BIN_TEMP WHERE ORGANIZATION = ?;
DELETE FROM SBI_DOSSIER_TEMP WHERE ORGANIZATION = ?;
DELETE FROM SBI_AUDIT WHERE ORGANIZATION = ?;
DELETE FROM SBI_EVENTS_ROLES WHERE ROLE_ID IN (SELECT t.EXT_ROLE_ID FROM SBI_EXT_ROLES t WHERE t.ORGANIZATION = ?);
DELETE FROM SBI_EVENTS_LOG WHERE ORGANIZATION = ?;
DELETE FROM SBI_EVENTS WHERE ORGANIZATION = ?;
DELETE FROM SBI_SUBREPORTS WHERE ORGANIZATION = ?;
DELETE FROM SBI_OBJ_PARUSE WHERE ORGANIZATION = ?;
DELETE FROM SBI_OBJ_PARVIEW WHERE ORGANIZATION = ?;
DELETE FROM SBI_PARUSE_CK WHERE ORGANIZATION = ?;
DELETE FROM SBI_PARUSE_DET WHERE ORGANIZATION = ?;
DELETE FROM SBI_PARUSE WHERE ORGANIZATION = ?;
DELETE FROM SBI_FUNC_ROLE WHERE ORGANIZATION = ?;
DELETE FROM SBI_OBJ_FUNC WHERE ORGANIZATION = ?;
-- As long as DELETE ORDER BY instruction is not safe for null values we set all parent keys to null
-- in order to allow deletion without ordering issues
UPDATE SBI_FUNCTIONS SET PARENT_FUNCT_ID = NULL WHERE ORGANIZATION = ?;
DELETE FROM SBI_FUNCTIONS WHERE ORGANIZATION = ?;
DELETE FROM SBI_CHECKS WHERE ORGANIZATION = ?;
DELETE FROM SBI_OBJ_PAR WHERE ORGANIZATION = ?;
DELETE FROM SBI_PARAMETERS WHERE ORGANIZATION = ?;
DELETE FROM SBI_OBJECT_NOTES WHERE ORGANIZATION = ?;
DELETE FROM SBI_OBJECT_TEMPLATES WHERE ORGANIZATION = ?;
DELETE FROM SBI_OBJ_METACONTENTS WHERE ORGANIZATION = ?;
DELETE FROM SBI_SUBOBJECTS WHERE ORGANIZATION = ?;
DELETE FROM SBI_SNAPSHOTS WHERE ORGANIZATION = ?;
DELETE FROM SBI_OBJ_STATE WHERE ORGANIZATION = ?;
DELETE FROM SBI_EXT_USER_ROLES WHERE EXT_ROLE_ID IN (SELECT t.EXT_ROLE_ID FROM SBI_EXT_ROLES t WHERE t.ORGANIZATION = ?);
DELETE FROM SBI_LOV  WHERE ORGANIZATION = ?;
DELETE FROM SBI_GEO_MAP_FEATURES WHERE ORGANIZATION = ?;
DELETE FROM SBI_GEO_FEATURES WHERE ORGANIZATION = ?;
DELETE FROM SBI_GEO_MAPS WHERE ORGANIZATION = ?;
DELETE FROM SBI_GEO_LAYERS  WHERE ORGANIZATION = ?;
DELETE FROM SBI_VIEWPOINTS WHERE ORGANIZATION = ?;
DELETE FROM SBI_MENU_ROLE WHERE ORGANIZATION = ?;
DELETE FROM SBI_MENU WHERE ORGANIZATION = ?;
DELETE FROM SBI_DIST_LIST_OBJECTS WHERE ORGANIZATION = ?;
DELETE FROM SBI_DIST_LIST_USER WHERE ORGANIZATION = ?;
DELETE FROM SBI_DIST_LIST WHERE ORGANIZATION = ?;
DELETE FROM SBI_KPI_VALUE WHERE ORGANIZATION = ?;
DELETE FROM SBI_KPI_MODEL_RESOURCES WHERE RESOURCE_ID IN (SELECT t.RESOURCE_ID FROM SBI_RESOURCES t WHERE t.ORGANIZATION = ?);
DELETE FROM SBI_RESOURCES WHERE ORGANIZATION = ?;
DELETE FROM SBI_ALARM_DISTRIBUTION WHERE ALARM_CONTACT_ID IN (select t.ALARM_CONTACT_ID from SBI_ALARM_CONTACT t WHERE t.ORGANIZATION = ?);
DELETE FROM SBI_ALARM_CONTACT WHERE ORGANIZATION = ?;
DELETE FROM SBI_ALARM_EVENT WHERE ORGANIZATION = ?;
DELETE FROM SBI_ALARM WHERE ORGANIZATION = ?;
DELETE FROM SBI_KPI_INSTANCE_HISTORY WHERE ORGANIZATION = ?;
-- As long as DELETE ORDER BY instruction is not safe for null values we set all parent keys to null
-- in order to allow deletion without ordering issues
UPDATE SBI_KPI_MODEL_INST SET KPI_MODEL_INST_PARENT = NULL WHERE ORGANIZATION = ?;
DELETE FROM SBI_KPI_MODEL_INST WHERE ORGANIZATION = ?;
DELETE FROM SBI_KPI_INST_PERIOD WHERE ORGANIZATION = ?;
DELETE FROM SBI_KPI_INSTANCE WHERE ORGANIZATION = ?;
DELETE FROM SBI_KPI_PERIODICITY WHERE ORGANIZATION = ?;
-- As long as DELETE ORDER BY instruction is not safe for null values we set all parent keys to null
-- in order to allow deletion without ordering issues
UPDATE SBI_KPI_MODEL SET KPI_PARENT_MODEL_ID = NULL WHERE ORGANIZATION = ?;
DELETE FROM SBI_KPI_MODEL WHERE ORGANIZATION = ?;
DELETE FROM SBI_MEASURE_UNIT WHERE ORGANIZATION = ?;
DELETE FROM SBI_KPI_DOCUMENTS WHERE ORGANIZATION = ?; 
DELETE FROM SBI_KPI_ERROR WHERE ORGANIZATION = ?; 
DELETE FROM SBI_KPI_REL WHERE ORGANIZATION = ?; 
DELETE FROM SBI_KPI_ROLE WHERE ORGANIZATION = ?;
DELETE FROM SBI_KPI WHERE ORGANIZATION = ?;
DELETE FROM SBI_THRESHOLD_VALUE WHERE ORGANIZATION = ?;
DELETE FROM SBI_THRESHOLD WHERE ORGANIZATION = ?;
DELETE FROM SBI_EXT_ROLES WHERE ORGANIZATION = ?;
DELETE FROM SBI_OBJ_METADATA WHERE ORGANIZATION = ?;
DELETE FROM SBI_OBJECTS WHERE ORGANIZATION = ?;
DELETE FROM SBI_BINARY_CONTENTS WHERE ORGANIZATION = ?;
DELETE FROM SBI_UDP_VALUE WHERE ORGANIZATION = ?;
DELETE FROM SBI_UDP WHERE ORGANIZATION = ?;
DELETE FROM SBI_GOAL WHERE ORGANIZATION = ?;  
DELETE FROM SBI_GOAL_HIERARCHY WHERE ORGANIZATION = ?;  
DELETE FROM SBI_GOAL_KPI WHERE ORGANIZATION = ?;
DELETE FROM SBI_USER_ATTRIBUTES WHERE ATTRIBUTE_ID IN (SELECT t.ATTRIBUTE_ID FROM SBI_ATTRIBUTE t WHERE t.ORGANIZATION = ?);
DELETE FROM SBI_ATTRIBUTE WHERE ORGANIZATION = ?;  
DELETE FROM SBI_USER WHERE ORGANIZATION = ?; 
DELETE FROM SBI_I18N_MESSAGES WHERE ORGANIZATION = ?;
DELETE FROM SBI_COMMUNITY WHERE ORGANIZATION = ?;
DELETE FROM SBI_COMMUNITY_USERS WHERE ORGANIZATION = ?;
DELETE FROM SBI_AUTHORIZATIONS_ROLES WHERE ORGANIZATION = ?;
DELETE FROM SBI_AUTHORIZATIONS WHERE ORGANIZATION = ?;
DELETE FROM SBI_ORGANIZATION_ENGINE WHERE ORGANIZATION = ?;
DELETE FROM SBI_ORGANIZATION_DATASOURCE WHERE ORGANIZATION = ?;
DELETE FROM SBI_ORGANIZATIONS WHERE NAME = ?;