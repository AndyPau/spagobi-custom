CREATE TABLE hibernate_sequences (
  SEQUENCE_NAME VARCHAR(200) NOT NULL,
  NEXT_VAL INTEGER NOT NULL,
  PRIMARY KEY (SEQUENCE_NAME)
)WITHOUT OIDS;

CREATE TABLE SBI_CHECKS (
       CHECK_ID             INTEGER NOT NULL ,
       DESCR                VARCHAR(160) NULL,
       LABEL                VARCHAR(20) NOT NULL,
       VALUE_TYPE_CD        VARCHAR(20) NOT NULL,
       VALUE_TYPE_ID        INTEGER NOT NULL,
       VALUE_1              VARCHAR(400) NULL,
       VALUE_2              VARCHAR(400) NULL,
       NAME                 VARCHAR(40) NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,    
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
       CONSTRAINT XAK1SBI_CHECKS UNIQUE (LABEL, ORGANIZATION),
       PRIMARY KEY (CHECK_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_DOMAINS (
       VALUE_ID             INTEGER NOT NULL ,
       VALUE_CD             VARCHAR(100) NULL,
       VALUE_NM             VARCHAR(40) NULL,
       DOMAIN_CD            VARCHAR(20) NULL,
       DOMAIN_NM            VARCHAR(40) NULL,
       VALUE_DS             VARCHAR(160) NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),    
       CONSTRAINT XAK1SBI_DOMAINS UNIQUE (VALUE_CD,DOMAIN_CD),
       PRIMARY KEY (VALUE_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_ENGINES (
       ENGINE_ID            INTEGER NOT NULL ,
       ENCRYPT              SMALLINT NULL,
       NAME                 VARCHAR(40) NOT NULL,
       DESCR                VARCHAR(160) NULL,
       MAIN_URL             VARCHAR(400) NULL,
       SECN_URL             VARCHAR(400) NULL,
       OBJ_UPL_DIR          VARCHAR(400) NULL,
       OBJ_USE_DIR          VARCHAR(400) NULL,
       DRIVER_NM            VARCHAR(400) NULL,
       LABEL                VARCHAR(40) NOT NULL,
       ENGINE_TYPE          INTEGER NOT NULL,
       CLASS_NM             VARCHAR(400) NULL,
       BIOBJ_TYPE           INTEGER NOT NULL,
 	   USE_DATASET          BOOLEAN DEFAULT FALSE,
 	   USE_DATASOURCE       BOOLEAN  DEFAULT FALSE,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20), 	 
       CONSTRAINT XAK1SBI_ENGINES UNIQUE  (LABEL, ORGANIZATION),
       PRIMARY KEY (ENGINE_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_EXT_ROLES (
       EXT_ROLE_ID          		INTEGER NOT NULL ,
       NAME                 		VARCHAR(100) NOT NULL,
       DESCR                		VARCHAR(160) NULL,
       CODE                 		VARCHAR(20) NULL,
       ROLE_TYPE_CD         		VARCHAR(20) NOT NULL,
       ROLE_TYPE_ID         		INTEGER NOT NULL,
       USER_IN              		VARCHAR(100) NOT NULL,
       USER_UP             			VARCHAR(100),
       USER_DE              		VARCHAR(100),
       TIME_IN              		TIMESTAMP NOT NULL,
       TIME_UP              		TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              		TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       		VARCHAR(10),
       SBI_VERSION_UP       		VARCHAR(10),
       SBI_VERSION_DE       		VARCHAR(10),
       META_VERSION         		VARCHAR(100),
       ORGANIZATION         		VARCHAR(20),       
       CONSTRAINT XAK1SBI_EXT_ROLES UNIQUE  (NAME, ORGANIZATION),
       PRIMARY KEY (EXT_ROLE_ID)   
) WITHOUT OIDS;


CREATE TABLE SBI_FUNC_ROLE (
       ROLE_ID              INTEGER NOT NULL,
       FUNCT_ID             INTEGER NOT NULL,
       STATE_CD             VARCHAR(20) NULL,
       STATE_ID             INTEGER NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),      
       PRIMARY KEY (FUNCT_ID, STATE_ID, ROLE_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_FUNCTIONS (
       FUNCT_ID             INTEGER NOT NULL ,
       FUNCT_TYPE_CD        VARCHAR(20) NOT NULL,
       PARENT_FUNCT_ID      INTEGER NULL,
       NAME                 VARCHAR(40) NULL,
       DESCR                VARCHAR(160) NULL,
       PATH                 VARCHAR(400) NULL,
       CODE                 VARCHAR(40) NOT NULL,
       PROG 				INTEGER NOT NULL,
       FUNCT_TYPE_ID        INTEGER NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),    
       CONSTRAINT XAK1SBI_FUNCTIONS UNIQUE  (CODE, ORGANIZATION),
       PRIMARY KEY (FUNCT_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_LOV (
       LOV_ID               INTEGER NOT NULL ,
       DESCR                VARCHAR(160) NULL,
       LABEL                VARCHAR(20) NOT NULL,
       INPUT_TYPE_CD        VARCHAR(20) NOT NULL,
       DEFAULT_VAL          VARCHAR(40) NULL,
       LOV_PROVIDER         TEXT NULL,
       INPUT_TYPE_ID        INTEGER NOT NULL,
       PROFILE_ATTR         VARCHAR(20) NULL,
       NAME                 VARCHAR(40) NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),   
       DATASET_ID			INTEGER,
       CONSTRAINT XAK1SBI_LOV UNIQUE  (LABEL, ORGANIZATION),
       PRIMARY KEY (LOV_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_OBJ_FUNC (
       BIOBJ_ID             INTEGER NOT NULL,
       FUNCT_ID             INTEGER NOT NULL,
       PROG                 INTEGER NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),       
       PRIMARY KEY (BIOBJ_ID, FUNCT_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_OBJ_PAR (
       OBJ_PAR_ID           INTEGER NOT NULL ,
       PAR_ID               INTEGER NOT NULL,
       BIOBJ_ID             INTEGER NOT NULL,
       LABEL                VARCHAR(40) NOT NULL,
       REQ_FL               SMALLINT NULL,
       MOD_FL               SMALLINT NULL,
       VIEW_FL              SMALLINT NULL,
       MULT_FL              SMALLINT NULL,
       PROG                 INTEGER NOT NULL,
       PARURL_NM            VARCHAR(20) NULL,
       PRIORITY             INTEGER NULL,
       COL_SPAN             INTEGER NULL,
       THICK_PERC           INTEGER NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
       PRIMARY KEY (OBJ_PAR_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_OBJ_STATE (
       BIOBJ_ID             INTEGER NOT NULL,
       STATE_ID             INTEGER NOT NULL,
       END_DT               DATE NULL,
       START_DT             DATE NOT NULL,
       NOTE                 VARCHAR(300) NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),      
       PRIMARY KEY (BIOBJ_ID, STATE_ID, START_DT)
) WITHOUT OIDS;


CREATE TABLE SBI_OBJECTS (
       BIOBJ_ID             INTEGER NOT NULL ,
       ENGINE_ID            INTEGER NOT NULL,
       DESCR                VARCHAR(400) NULL,
       LABEL                VARCHAR(100) NOT NULL,
       ENCRYPT              SMALLINT NULL,
       PATH                 VARCHAR(400) NULL,
       REL_NAME             VARCHAR(400) NULL,
       STATE_ID             INTEGER NOT NULL,
       STATE_CD             VARCHAR(20) NOT NULL,
       BIOBJ_TYPE_CD        VARCHAR(20) NOT NULL,
       BIOBJ_TYPE_ID        INTEGER NOT NULL,
       SCHED_FL             SMALLINT NULL,
       EXEC_MODE_ID         INTEGER NULL,
       STATE_CONS_ID        INTEGER NULL,
       EXEC_MODE_CD         VARCHAR(20) NULL,
       STATE_CONS_CD        VARCHAR(20) NULL,
       NAME                 VARCHAR(200) NOT NULL,
       VISIBLE              SMALLINT NOT NULL,
       UUID                 VARCHAR(40) NOT NULL,
       DATA_SOURCE_ID 		INTEGER,
       DATA_SET_ID 		    INTEGER,
       DESCR_EXT            TEXT,
       OBJECTIVE            TEXT,
       LANGUAGE             VARCHAR(45),
       CREATION_DATE        TIMESTAMP NOT NULL,
       CREATION_USER        VARCHAR(45) NOT NULL,
       KEYWORDS			    VARCHAR(250),
       REFRESH_SECONDS      INTEGER,
       PROF_VISIBILITY      VARCHAR(400) NULL,
       PREVIEW_FILE 		VARCHAR(100),
       PARAMETERS_REGION    VARCHAR(20) NULL, 
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),    
       CONSTRAINT XAK1SBI_OBJECTS UNIQUE  (LABEL, ORGANIZATION),
       PRIMARY KEY (BIOBJ_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_OBJECTS_RATING (
	   USER_ID 				VARCHAR(127) NOT NULL,
	   OBJ_ID 				INTEGER NOT NULL,
	   RATING 				INTEGER NOT NULL,
	   USER_IN              VARCHAR(100) NOT NULL,
	   USER_UP              VARCHAR(100),
	   USER_DE              VARCHAR(100),
	   TIME_IN              TIMESTAMP NOT NULL,
	   TIME_UP              TIMESTAMP NULL DEFAULT NULL,
	   TIME_DE              TIMESTAMP NULL DEFAULT NULL,
	   SBI_VERSION_IN       VARCHAR(10),
	   SBI_VERSION_UP       VARCHAR(10),
	   SBI_VERSION_DE       VARCHAR(10),
	   META_VERSION         VARCHAR(100),
	   ORGANIZATION         VARCHAR(20), 
	 PRIMARY KEY (USER_ID, OBJ_ID)
) WITHOUT OIDS; 


CREATE TABLE SBI_PARAMETERS (
       PAR_ID               INTEGER NOT NULL ,
       DESCR                VARCHAR(160) NULL,
       LENGTH               SMALLINT NOT NULL,
       LABEL                VARCHAR(20) NOT NULL,
       PAR_TYPE_CD          VARCHAR(20) NOT NULL,
       MASK                 VARCHAR(20) NULL,
       PAR_TYPE_ID          INTEGER NOT NULL,
       NAME                 VARCHAR(40) NOT NULL,
       FUNCTIONAL_FLAG		SMALLINT NOT NULL DEFAULT 1,
       TEMPORAL_FLAG		SMALLINT NOT NULL DEFAULT 0,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),   
       CONSTRAINT XAK1SBI_PARAMETERS UNIQUE  (LABEL, ORGANIZATION),
       PRIMARY KEY (PAR_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_PARUSE (
       USE_ID               INTEGER NOT NULL ,
       LOV_ID               INTEGER NULL,
       DEFAULT_LOV_ID       INTEGER NULL,
       DEFAULT_FORMULA      VARCHAR(4000) NULL,
       LABEL                VARCHAR(20) NOT NULL,
       DESCR                VARCHAR(160) NULL,
       PAR_ID               INTEGER NOT NULL,
       NAME                 VARCHAR(40) NOT NULL,
       MAN_IN               INTEGER NOT NULL,
       MAXIMIZER_ENABLED	BOOLEAN DEFAULT FALSE,
       SELECTION_TYPE  		VARCHAR(20) DEFAULT 'LIST',
       MULTIVALUE_FLAG  	INTEGER DEFAULT 0,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),    
       OPTIONS				VARCHAR(4000) NULL DEFAULT NULL,
       CONSTRAINT XAK1SBI_PARUSE UNIQUE  (PAR_ID,LABEL),
       PRIMARY KEY (USE_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_PARUSE_CK (
       CHECK_ID             INTEGER NOT NULL,
       USE_ID               INTEGER NOT NULL,
       PROG                 INTEGER NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),       
       PRIMARY KEY (USE_ID, CHECK_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_PARUSE_DET (
       EXT_ROLE_ID          INTEGER NOT NULL,
       PROG                 INTEGER NULL,
       USE_ID               INTEGER NOT NULL,
       HIDDEN_FL            SMALLINT NULL,
       DEFAULT_VAL          VARCHAR(40) NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),       
       PRIMARY KEY (USE_ID, EXT_ROLE_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_SUBREPORTS (
       MASTER_RPT_ID        INTEGER NOT NULL,
       SUB_RPT_ID           INTEGER NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),      
       PRIMARY KEY (MASTER_RPT_ID, SUB_RPT_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_OBJ_PARUSE (
		OBJ_PAR_ID          INTEGER NOT NULL,
		USE_ID              INTEGER NOT NULL,
		OBJ_PAR_FATHER_ID   INTEGER NOT NULL,
		FILTER_OPERATION    VARCHAR(20) NOT NULL,
		PROG 				INTEGER NOT NULL,
		FILTER_COLUMN       VARCHAR(30) NOT NULL,
		PRE_CONDITION 		VARCHAR(10),
	    POST_CONDITION 		VARCHAR(10),
	    LOGIC_OPERATOR 		VARCHAR(10),
        USER_IN             VARCHAR(100) NOT NULL,
        USER_UP             VARCHAR(100),
        USER_DE             VARCHAR(100),
        TIME_IN             TIMESTAMP NOT NULL,
        TIME_UP             TIMESTAMP NULL DEFAULT NULL,
        TIME_DE             TIMESTAMP NULL DEFAULT NULL,
        SBI_VERSION_IN      VARCHAR(10),
        SBI_VERSION_UP      VARCHAR(10),
        SBI_VERSION_DE      VARCHAR(10),
        META_VERSION        VARCHAR(100),
        ORGANIZATION        VARCHAR(20),    
	    PRIMARY KEY(OBJ_PAR_ID,USE_ID,OBJ_PAR_FATHER_ID,FILTER_OPERATION)
) WITHOUT OIDS;


CREATE TABLE SBI_EVENTS (
	   ID                   INTEGER NOT NULL ,
  	   USER_EVENT           VARCHAR(40) NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),  
       PRIMARY KEY(ID)
) WITHOUT OIDS;

CREATE TABLE SBI_EVENTS_LOG (
		ID                   INTEGER NOT NULL ,
		USER_EVENT           VARCHAR(40) NOT NULL,
		EVENT_DATE           TIMESTAMP DEFAULT NOW() NOT NULL,
		DESCR                TEXT NOT NULL,
		PARAMS               VARCHAR(1000),
		HANDLER 			 VARCHAR(400) NOT NULL DEFAULT 'it.eng.spagobi.events.handlers.DefaultEventPresentationHandler',
        USER_IN              VARCHAR(100) NOT NULL,
        USER_UP              VARCHAR(100),
        USER_DE              VARCHAR(100),
        TIME_IN              TIMESTAMP NOT NULL,
        TIME_UP              TIMESTAMP NULL DEFAULT NULL,
        TIME_DE              TIMESTAMP NULL DEFAULT NULL,
        SBI_VERSION_IN       VARCHAR(10),
        SBI_VERSION_UP       VARCHAR(10),
        SBI_VERSION_DE       VARCHAR(10),
        META_VERSION         VARCHAR(100),
        ORGANIZATION         VARCHAR(20), 	
        PRIMARY KEY(ID)
) WITHOUT OIDS;

CREATE TABLE SBI_EVENTS_ROLES (
       EVENT_ID            INTEGER NOT NULL,
       ROLE_ID             INTEGER NOT NULL,      
       PRIMARY KEY (EVENT_ID, ROLE_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_AUDIT ( 
		ID 					INTEGER NOT NULL ,
		USERNAME 			VARCHAR(40) NOT NULL,
		USERGROUP 			VARCHAR(100),
		DOC_REF 			INTEGER,
		DOC_ID 				INTEGER,
		DOC_LABEL 			VARCHAR(200) NOT NULL,
		DOC_NAME 			VARCHAR(200) NOT NULL,
		DOC_TYPE 			VARCHAR(20) NOT NULL,
		DOC_STATE 			VARCHAR(20) NOT NULL,
		DOC_PARAMETERS 		TEXT,
		SUBOBJ_REF			INTEGER,
		SUBOBJ_ID			INTEGER,
		SUBOBJ_NAME         VARCHAR(50),
		SUBOBJ_OWNER 	    VARCHAR(50),
		SUBOBJ_ISPUBLIC 	SMALLINT NULL,
		ENGINE_REF 			INTEGER,
		ENGINE_ID 			INTEGER,
		ENGINE_LABEL 		VARCHAR(40) NOT NULL,
		ENGINE_NAME 		VARCHAR(40) NOT NULL,
		ENGINE_TYPE 		VARCHAR(20) NOT NULL,
		ENGINE_URL 			VARCHAR(400),
		ENGINE_DRIVER 		VARCHAR(400),
		ENGINE_CLASS 		VARCHAR(400),
		REQUEST_TIME 		TIMESTAMP NOT NULL,
		EXECUTION_START 	TIMESTAMP NULL DEFAULT NULL,
		EXECUTION_END 		TIMESTAMP NULL DEFAULT NULL,
		EXECUTION_TIME		INTEGER,
		EXECUTION_STATE 	VARCHAR(20),
		ERROR				SMALLINT,
		ERROR_MESSAGE 		VARCHAR(400),
		ERROR_CODE 			VARCHAR(20),
		EXECUTION_MODALITY 	VARCHAR(40),
        USER_IN             VARCHAR(100) NOT NULL,
        USER_UP             VARCHAR(100),
        USER_DE             VARCHAR(100),
        TIME_IN             TIMESTAMP NOT NULL,
        TIME_UP             TIMESTAMP NULL DEFAULT NULL,
        TIME_DE             TIMESTAMP NULL DEFAULT NULL,
        SBI_VERSION_IN      VARCHAR(10),
        SBI_VERSION_UP      VARCHAR(10),
        SBI_VERSION_DE      VARCHAR(10),
        META_VERSION        VARCHAR(100),
        ORGANIZATION        VARCHAR(20), 		
		PRIMARY KEY (ID)
) WITHOUT OIDS;

CREATE TABLE SBI_ACTIVITY_MONITORING (
	  ID 			INTEGER NOT NULL ,
	  ACTION_TIME   TIMESTAMP,
	  USERNAME 	 	VARCHAR(40) NOT NULL,
	  USERGROUP		VARCHAR(400),
	  LOG_LEVEL 	VARCHAR(10) ,
	  ACTION_CODE 	VARCHAR(45) NOT NULL,
	  INFO 			VARCHAR(400),
	  PRIMARY KEY (ID)
) WITHOUT OIDS;

CREATE TABLE SBI_GEO_MAPS (
       MAP_ID               INTEGER NOT NULL ,       
       NAME                 VARCHAR(40) NOT NULL,
       DESCR                VARCHAR(160) NULL,
       URL					VARCHAR(400) NULL,
       FORMAT 				VARCHAR(40) NULL,       
	   BIN_ID               INTEGER NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),   
       CONSTRAINT XAK1SBI_GEO_MAPS UNIQUE  (NAME, ORGANIZATION),
       PRIMARY KEY (MAP_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_GEO_FEATURES (
       FEATURE_ID           INTEGER NOT NULL ,       
       NAME                 VARCHAR(40) NOT NULL,
       DESCR                VARCHAR(160) NULL,
       TYPE					VARCHAR(40)  NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
       CONSTRAINT XAK1SBI_GEO_FEATURES UNIQUE  (NAME, ORGANIZATION),
       PRIMARY KEY (FEATURE_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_GEO_MAP_FEATURES (
       MAP_ID               INTEGER NOT NULL,
       FEATURE_ID           INTEGER NOT NULL,
       SVG_GROUP            VARCHAR(40),
       VISIBLE_FLAG		    VARCHAR(1),
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
       PRIMARY KEY (MAP_ID, FEATURE_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_VIEWPOINTS (
		VP_ID 				 INTEGER NOT NULL ,
		BIOBJ_ID 			 INTEGER NOT NULL, 
		VP_NAME 			 VARCHAR(40) NOT NULL,
    	VP_OWNER 			 VARCHAR(40),
		VP_DESC 			 VARCHAR(160),
		VP_SCOPE 			 VARCHAR (20) NOT NULL, 
		VP_VALUE_PARAMS 	 TEXT, 
		VP_CREATION_DATE 	 TIMESTAMP NOT NULL,
        USER_IN              VARCHAR(100) NOT NULL,
        USER_UP              VARCHAR(100),
        USER_DE              VARCHAR(100),
        TIME_IN              TIMESTAMP NOT NULL,
        TIME_UP              TIMESTAMP NULL DEFAULT NULL,
        TIME_DE              TIMESTAMP NULL DEFAULT NULL,
        SBI_VERSION_IN       VARCHAR(10),
        SBI_VERSION_UP       VARCHAR(10),
        SBI_VERSION_DE       VARCHAR(10),
        META_VERSION         VARCHAR(100),
        ORGANIZATION         VARCHAR(20),   
        PRIMARY KEY (VP_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_DATA_SOURCE (
		DS_ID 				 INTEGER NOT NULL ,
		DESCR 				 VARCHAR(160), 
		LABEL	 			 VARCHAR(50) NOT NULL,
    	JNDI	 			 VARCHAR(50),
		URL_CONNECTION		 VARCHAR(500),
		USERNAME 			 VARCHAR(50), 
		PWD				 	 VARCHAR(50), 
		DRIVER			 	 VARCHAR(160),
		DIALECT_ID			 INTEGER NOT NULL,
	    MULTI_SCHEMA         BOOLEAN  NOT NULL DEFAULT '0',
	    ATTR_SCHEMA    		 VARCHAR(45) DEFAULT NULL,
	    READ_ONLY            BOOLEAN DEFAULT FALSE,
	    WRITE_DEFAULT        BOOLEAN DEFAULT FALSE,	    
        USER_IN              VARCHAR(100) NOT NULL,
        USER_UP              VARCHAR(100),
        USER_DE              VARCHAR(100),
        TIME_IN              TIMESTAMP NOT NULL,
        TIME_UP              TIMESTAMP,
        TIME_DE              TIMESTAMP,
        SBI_VERSION_IN       VARCHAR(10),
        SBI_VERSION_UP       VARCHAR(10),
        SBI_VERSION_DE       VARCHAR(10),
        META_VERSION         VARCHAR(100),
        ORGANIZATION         VARCHAR(20),   
        CONSTRAINT XAK1SBI_DATA_SOURCE UNIQUE  (LABEL, ORGANIZATION),
        PRIMARY KEY (DS_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_BINARY_CONTENTS (
	   BIN_ID 				INTEGER NOT NULL ,
	   BIN_CONTENT 			BYTEA NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
       PRIMARY KEY (BIN_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_OBJECT_TEMPLATES (
		OBJ_TEMP_ID 		 INTEGER NOT NULL ,
		BIOBJ_ID 	         INTEGER,
	    BIN_ID 	             INTEGER,
	    NAME 	             VARCHAR(50),  
	    PROG 	             INTEGER, 
	    DIMENSION            VARCHAR(20),  
		CREATION_DATE 		 DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	    CREATION_USER        VARCHAR(45) NOT NULL, 
	    ACTIVE 	             BOOLEAN,  
        USER_IN              VARCHAR(100) NOT NULL,
        USER_UP              VARCHAR(100),
        USER_DE              VARCHAR(100),
        TIME_IN              TIMESTAMP NOT NULL,
        TIME_UP              TIMESTAMP NULL DEFAULT NULL,
        TIME_DE              TIMESTAMP NULL DEFAULT NULL,
        SBI_VERSION_IN       VARCHAR(10),
        SBI_VERSION_UP       VARCHAR(10),
        SBI_VERSION_DE       VARCHAR(10),
        META_VERSION         VARCHAR(100),
        ORGANIZATION         VARCHAR(20),
        PRIMARY KEY (OBJ_TEMP_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_OBJECT_NOTES (
	   OBJ_NOTE_ID 			INTEGER NOT NULL ,
	   BIOBJ_ID 	        INTEGER,
       BIN_ID 	            INTEGER,
       EXEC_REQ 	        VARCHAR(500),
       OWNER 	            VARCHAR(50),
       ISPUBLIC 	        BOOLEAN,  
       CREATION_DATE 	    TIMESTAMP NOT NULL,  
       LAST_CHANGE_DATE     TIMESTAMP NOT NULL, 
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
       PRIMARY KEY (OBJ_NOTE_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_SUBOBJECTS (
	   SUBOBJ_ID 			INTEGER NOT NULL ,
	   BIOBJ_ID 	        INTEGER,
       BIN_ID 	            INTEGER,
       NAME 	            VARCHAR(50) NOT NULL,
       DESCRIPTION 	        VARCHAR(100), 
       OWNER 	            VARCHAR(50),
       ISPUBLIC 	        BOOLEAN,  
       CREATION_DATE 	    TIMESTAMP NOT NULL,  
       LAST_CHANGE_DATE 	TIMESTAMP NOT NULL,   
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
       PRIMARY KEY (SUBOBJ_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_SNAPSHOTS (
	   SNAP_ID 				INTEGER NOT NULL ,
	   BIOBJ_ID 	        INTEGER,
       BIN_ID 	            INTEGER,
       NAME 	            VARCHAR(100),
       DESCRIPTION 	        VARCHAR(1000),
       CREATION_DATE 	    TIMESTAMP NOT NULL,  
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
       CONTENT_TYPE 		VARCHAR(300) DEFAULT NULL,
       PRIMARY KEY (SNAP_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_USER_FUNC (
	   USER_FUNCT_ID 		INTEGER NOT NULL ,
	   NAME 	            VARCHAR(50),  
       DESCRIPTION 	        VARCHAR(100),  
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
       PRIMARY KEY (USER_FUNCT_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_ROLE_TYPE_USER_FUNC (
		ROLE_TYPE_ID 		INTEGER NOT NULL,
		USER_FUNCT_ID 	    INTEGER NOT NULL,
        PRIMARY KEY (ROLE_TYPE_ID,USER_FUNCT_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_DOSSIER_PRES (
        PRESENTATION_ID 			INTEGER NOT NULL ,
        WORKFLOW_PROCESS_ID 		BIGINT NOT NULL,
        BIOBJ_ID 					INTEGER NOT NULL,
        BIN_ID 						INTEGER NOT NULL,
        NAME 						VARCHAR(40) NOT NULL,
        PROG 						INTEGER NULL,
        CREATION_DATE 				TIMESTAMP,
        APPROVED 					SMALLINT NULL,
        USER_IN              VARCHAR(100) NOT NULL,
        USER_UP              VARCHAR(100),
        USER_DE              VARCHAR(100),
        TIME_IN              TIMESTAMP NOT NULL,
        TIME_UP              TIMESTAMP NULL DEFAULT NULL,
        TIME_DE              TIMESTAMP NULL DEFAULT NULL,
        SBI_VERSION_IN       VARCHAR(10),
        SBI_VERSION_UP       VARCHAR(10),
        SBI_VERSION_DE       VARCHAR(10),
        META_VERSION         VARCHAR(100),
        ORGANIZATION         VARCHAR(20),
        PRIMARY KEY (PRESENTATION_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_DOSSIER_TEMP (
        PART_ID 			 INTEGER NOT NULL ,
        WORKFLOW_PROCESS_ID  BIGINT NOT NULL,
        BIOBJ_ID 			 INTEGER NOT NULL,
        PAGE_ID 			 INTEGER NOT NULL,
        USER_IN              VARCHAR(100) NOT NULL,
        USER_UP              VARCHAR(100),
        USER_DE              VARCHAR(100),
        TIME_IN              TIMESTAMP NOT NULL,
        TIME_UP              TIMESTAMP NULL DEFAULT NULL,
        TIME_DE              TIMESTAMP NULL DEFAULT NULL,
        SBI_VERSION_IN       VARCHAR(10),
        SBI_VERSION_UP       VARCHAR(10),
        SBI_VERSION_DE       VARCHAR(10),
        META_VERSION         VARCHAR(100),
        ORGANIZATION         VARCHAR(20),
        PRIMARY KEY (PART_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_DOSSIER_BIN_TEMP (
       BIN_ID 				INTEGER NOT NULL ,
       PART_ID 				INTEGER NOT NULL,
       NAME 				VARCHAR(20),
       BIN_CONTENT 			BYTEA NOT NULL,
       TYPE 				VARCHAR(20) NOT NULL,
       CREATION_DATE 		TIMESTAMP,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
       PRIMARY KEY (BIN_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_DIST_LIST (
       DL_ID 				INTEGER NOT NULL ,
       NAME 				VARCHAR(40) NOT NULL,
       DESCR				VARCHAR(160),
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
       CONSTRAINT XAK1SBI_SBI_DIST_LIST UNIQUE  (NAME, ORGANIZATION), 
       PRIMARY KEY (DL_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_DIST_LIST_USER (
	   DLU_ID				INTEGER NOT NULL ,
       LIST_ID 				INTEGER NOT NULL,
       USER_ID 				VARCHAR(40) NOT NULL,
       E_MAIL				VARCHAR(70) NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
       CONSTRAINT XAK1SBI_DL_USER UNIQUE  (LIST_ID, USER_ID),
	   PRIMARY KEY (DLU_ID)       
) WITHOUT OIDS;

CREATE TABLE SBI_DIST_LIST_OBJECTS (
	   REL_ID				INTEGER NOT NULL ,
       DOC_ID 				INTEGER NOT NULL,
       DL_ID 				INTEGER NOT NULL,
       XML					VARCHAR(5000) NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
	   PRIMARY KEY (REL_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_REMEMBER_ME (
       ID               	INTEGER NOT NULL ,
       NAME 				VARCHAR(50) NOT NULL,
       DESCRIPTION      	TEXT,
       USERNAME				VARCHAR(40) NOT NULL,
       BIOBJ_ID         	INTEGER NOT NULL,
       SUBOBJ_ID        	INTEGER NULL,
       PARAMETERS       	TEXT,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
       PRIMARY KEY (ID)
) WITHOUT OIDS;


CREATE TABLE SBI_DATA_SET (
	   DS_ID 		   		  INTEGER NOT NULL ,
	   VERSION_NUM	   		  INTEGER NOT NULL,
	   ACTIVE		   		  BOOLEAN NOT NULL,
	   DESCR 		   		  VARCHAR(160), 
	   LABEL	 	   		  VARCHAR(51) NOT NULL,
	   NAME	   	   			  VARCHAR(52) NOT NULL,  
	   OBJECT_TYPE   		  VARCHAR(53),
	   DS_METADATA    		  TEXT,
	   PARAMS         		  VARCHAR(4000),
	   CATEGORY_ID    		  INTEGER,
	   TRANSFORMER_ID 		  INTEGER,
	   PIVOT_COLUMN   		  VARCHAR(54),
	   PIVOT_ROW      		  VARCHAR(55),
	   PIVOT_VALUE   		  VARCHAR(56),
	   NUM_ROWS	   		 	  BOOLEAN DEFAULT FALSE,	
	   IS_PERSISTED  		  BOOLEAN DEFAULT FALSE,
	   PERSIST_TABLE_NAME     VARCHAR(50),
	   CONFIGURATION          TEXT NULL,    	   	  
	   OWNER 				  VARCHAR(50),
	   IS_PUBLIC 			  BOOLEAN DEFAULT FALSE,
	   USER_IN                VARCHAR(100) NOT NULL,
	   USER_UP                VARCHAR(100),
	   USER_DE                VARCHAR(100),
	   TIME_IN                TIMESTAMP NOT NULL,
	   TIME_UP                TIMESTAMP NULL DEFAULT NULL,
	   TIME_DE                TIMESTAMP NULL DEFAULT NULL,
	   SBI_VERSION_IN         VARCHAR(10),
	   SBI_VERSION_UP         VARCHAR(10),
	   SBI_VERSION_DE         VARCHAR(10),
	   META_VERSION           VARCHAR(100),
	   ORGANIZATION           VARCHAR(20), 
	   SCOPE_ID 			  INTEGER NULL DEFAULT NULL,
	CONSTRAINT XAK2SBI_DATA_SET UNIQUE (LABEL, VERSION_NUM, ORGANIZATION),
     PRIMARY KEY (DS_ID, VERSION_NUM, ORGANIZATION)
)WITHOUT OIDS;

CREATE TABLE SBI_MENU (
		MENU_ID 			 INTEGER  NOT NULL ,
		NAME 				 VARCHAR(50), 
		DESCR 				 VARCHAR(2000),
		PARENT_ID 			 INTEGER DEFAULT 0, 
		BIOBJ_ID 			 INTEGER,
		VIEW_ICONS 			 BOOLEAN,
		HIDE_TOOLBAR 		 BOOLEAN, 
		HIDE_SLIDERS 		 BOOLEAN,
		STATIC_PAGE			 VARCHAR(45),
		BIOBJ_PARAMETERS 	 TEXT NULL,
		SUBOBJ_NAME 		 VARCHAR(50) NULL,
		SNAPSHOT_NAME 		 VARCHAR(100) NULL,
		SNAPSHOT_HISTORY 	 INTEGER NULL,
		FUNCTIONALITY 		 VARCHAR(50) NULL,
		INITIAL_PATH 		 VARCHAR(400) NULL,
		EXT_APP_URL 		 VARCHAR(1000) NULL,
		PROG 				 INTEGER NOT NULL DEFAULT 1,
        USER_IN              VARCHAR(100) NOT NULL,
        USER_UP              VARCHAR(100),
        USER_DE              VARCHAR(100),
        TIME_IN              TIMESTAMP NOT NULL,
        TIME_UP              TIMESTAMP NULL DEFAULT NULL,
        TIME_DE              TIMESTAMP NULL DEFAULT NULL,
        SBI_VERSION_IN       VARCHAR(10),
        SBI_VERSION_UP       VARCHAR(10),
        SBI_VERSION_DE       VARCHAR(10),
        META_VERSION         VARCHAR(100),
        ORGANIZATION         VARCHAR(20),     	
        PRIMARY KEY (MENU_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_MENU_ROLE (
       MENU_ID 				INTEGER NOT NULL, 
       EXT_ROLE_ID 			INTEGER NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),            
       PRIMARY KEY (MENU_ID, EXT_ROLE_ID)
) WITHOUT OIDS;

-- KPI DEFINITION

CREATE TABLE  SBI_KPI_ROLE  (
	   ID_KPI_ROLE  		INTEGER NOT NULL ,
	   KPI_ID  				INTEGER NOT NULL,
	   EXT_ROLE_ID  		INTEGER NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
       PRIMARY KEY ( ID_KPI_ROLE )
) WITHOUT OIDS;


CREATE TABLE SBI_KPI  (
	   KPI_ID  				INTEGER NOT NULL ,
	   ID_MEASURE_UNIT  	INTEGER,
	   DS_ID  				INTEGER,
	   THRESHOLD_ID  		INTEGER,
	   ID_KPI_PARENT  		INTEGER,
	   NAME  				VARCHAR(400) NOT NULL,
	   CODE  				VARCHAR(40),
	   METRIC  				VARCHAR(1000),
	   DESCRIPTION  		VARCHAR(1000),
	   WEIGHT  				NUMERIC,
	   IS_ADDITIVE  		CHAR(1),
	   FLG_IS_FATHER  		CHAR(1),
	   KPI_TYPE  			INTEGER,
	   METRIC_SCALE_TYPE  	INTEGER,
	   MEASURE_TYPE  		INTEGER,
	   INTERPRETATION  		VARCHAR(1000),
	   INPUT_ATTRIBUTES  	VARCHAR(1000),
	   MODEL_REFERENCE  	VARCHAR(255),
	   TARGET_AUDIENCE  	VARCHAR(1000),
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
       CONSTRAINT XAK1SBI_KPI UNIQUE  ( CODE, ORGANIZATION ),
 	   PRIMARY KEY ( KPI_ID )
 ) WITHOUT OIDS;
 
CREATE TABLE  SBI_KPI_DOCUMENTS  (
	   ID_KPI_DOC  			INTEGER NOT NULL ,
	   BIOBJ_ID  			INTEGER NOT NULL,
	   KPI_ID  				INTEGER NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
 	   PRIMARY KEY ( ID_KPI_DOC )
) WITHOUT OIDS;

CREATE TABLE  SBI_MEASURE_UNIT  (
	   ID_MEASURE_UNIT  	INTEGER NOT NULL ,
	   NAME  				VARCHAR(400),
	   SCALE_TYPE_ID  		INTEGER NOT NULL,
	   SCALE_CD  			VARCHAR(40),
	   SCALE_NM  			VARCHAR(400),
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
 	   PRIMARY KEY ( ID_MEASURE_UNIT )
) WITHOUT OIDS;
 

CREATE TABLE  SBI_THRESHOLD  (
	   THRESHOLD_ID  		INTEGER NOT NULL ,
	   THRESHOLD_TYPE_ID  	INTEGER NOT NULL,
	   NAME  				VARCHAR(400),
	   DESCRIPTION  		VARCHAR(1000),
	   CODE  				VARCHAR(45) NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     	
	   CONSTRAINT XIF1SBI_THRESHOLD UNIQUE  ( CODE, ORGANIZATION ),
 	   PRIMARY KEY ( THRESHOLD_ID )
) WITHOUT OIDS;

CREATE TABLE  SBI_THRESHOLD_VALUE  (
	   ID_THRESHOLD_VALUE  	INTEGER NOT NULL ,
	   THRESHOLD_ID  		INTEGER NOT NULL,
	   SEVERITY_ID  		INTEGER,
	   KPI_POSITION  		INTEGER,
	   MIN_VALUE  			NUMERIC,
	   MAX_VALUE  			NUMERIC,
	   LABEL  				VARCHAR(20) NOT NULL,
	   COLOUR  				VARCHAR(20),
	   MIN_CLOSED  			BOOLEAN,
	   MAX_CLOSED  			BOOLEAN,
	   TH_VALUE  			NUMERIC,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     	
	   CONSTRAINT XIF1SBI_THRESHOLD_VALUE UNIQUE  ( LABEL ,THRESHOLD_ID ),
 	   PRIMARY KEY ( ID_THRESHOLD_VALUE )
) WITHOUT OIDS;

 CREATE TABLE  SBI_KPI_MODEL  (
	   KPI_MODEL_ID  		INTEGER NOT NULL ,
	   KPI_ID  				INTEGER,
	   KPI_MODEL_TYPE_ID  	INTEGER NOT NULL,
	   KPI_PARENT_MODEL_ID  INTEGER,
	   KPI_MODEL_CD  		VARCHAR(40) NOT NULL,
	   KPI_MODEL_NM  		VARCHAR(400),
	   KPI_MODEL_DESC 		VARCHAR(1000),
	   KPI_MODEL_LBL  		VARCHAR(100) NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     	
	   CONSTRAINT XIF1SBI_KPI_MODEL UNIQUE  ( KPI_MODEL_LBL, ORGANIZATION ),
	   CONSTRAINT UNIQUE_PAR_ID_CD UNIQUE ( KPI_PARENT_MODEL_ID, KPI_MODEL_CD ),
       PRIMARY KEY ( KPI_MODEL_ID )
) WITHOUT OIDS;

-- INSTANCE
CREATE TABLE  SBI_KPI_PERIODICITY  (
	   ID_KPI_PERIODICITY  	INTEGER NOT NULL ,
	   NAME  				VARCHAR(200) NOT NULL,
	   MONTHS  				INTEGER,
	   DAYS  				INTEGER,
	   HOURS  				INTEGER,
	   MINUTES  			INTEGER,
	   CHRON_STRING  		VARCHAR(20),
	   START_DATE  			TIMESTAMP,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
	   CONSTRAINT XIF1SBI_KPI_PERIODICITY UNIQUE  ( NAME, ORGANIZATION ),
 	   PRIMARY KEY ( ID_KPI_PERIODICITY )
) WITHOUT OIDS;

CREATE TABLE  SBI_KPI_INSTANCE  (
	   ID_KPI_INSTANCE  	INTEGER NOT NULL ,
	   KPI_ID  				INTEGER NOT NULL,
	   THRESHOLD_ID  		INTEGER,
	   CHART_TYPE_ID  		INTEGER,
	   ID_MEASURE_UNIT  	INTEGER,
	   WEIGHT  				NUMERIC,
	   TARGET  				NUMERIC,
	   BEGIN_DT  			DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
 	   PRIMARY KEY ( ID_KPI_INSTANCE )
) WITHOUT OIDS;


CREATE TABLE  SBI_KPI_INST_PERIOD  (
       KPI_INST_PERIOD_ID  	INTEGER NOT NULL ,
       KPI_INSTANCE_ID  	INTEGER NOT NULL,
       PERIODICITY_ID  		INTEGER NOT NULL,
   	   DEFAULT_VALUE  		BOOLEAN ,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),      
 	   PRIMARY KEY ( KPI_INST_PERIOD_ID )
)
WITHOUT OIDS;


CREATE TABLE  SBI_KPI_INSTANCE_HISTORY  (
	   ID_KPI_INSTANCE_HISTORY  INTEGER NOT NULL ,
	   ID_KPI_INSTANCE  	INTEGER NOT NULL,
	   THRESHOLD_ID  		INTEGER,
	   CHART_TYPE_ID  		INTEGER,
	   ID_MEASURE_UNIT  	INTEGER,
	   WEIGHT  				NUMERIC,
	   TARGET  				NUMERIC,
	   BEGIN_DT  			DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	   END_DT  				DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
 		PRIMARY KEY ( ID_KPI_INSTANCE_HISTORY )
) WITHOUT OIDS;

CREATE TABLE  SBI_KPI_VALUE  (
	 ID_KPI_INSTANCE_VALUE  INTEGER NOT NULL ,
	 ID_KPI_INSTANCE  INTEGER NOT NULL,
	 RESOURCE_ID  INTEGER,
	 VALUE  VARCHAR(40),
	 BEGIN_DT  DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	 END_DT  DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	 DESCRIPTION  VARCHAR(100),
	 XML_DATA  TEXT,
	 ORG_UNIT_ID  INTEGER,
	 HIERARCHY_ID  INTEGER,
	 COMPANY  VARCHAR(200),
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
 PRIMARY KEY ( ID_KPI_INSTANCE_VALUE )
 ) WITHOUT OIDS;

CREATE TABLE  SBI_KPI_MODEL_INST  (
	 KPI_MODEL_INST  INTEGER NOT NULL ,
	 KPI_MODEL_INST_PARENT  INTEGER,
	 KPI_MODEL_ID  INTEGER,
	 ID_KPI_INSTANCE  INTEGER,
	 NAME  VARCHAR(400),
	 LABEL  VARCHAR(100) NOT NULL,
	 DESCRIPTION  VARCHAR(1000),
	 START_DATE  DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	 END_DATE  DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	 MODELUUID  VARCHAR(400),
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
    CONSTRAINT XAK2SBI_KPI_MODEL_INST UNIQUE  ( LABEL, ORGANIZATION ),
 PRIMARY KEY ( KPI_MODEL_INST )
 ) WITHOUT OIDS;

CREATE TABLE  SBI_RESOURCES  (
	 RESOURCE_ID  INTEGER NOT NULL ,
	 RESOURCE_TYPE_ID  INTEGER NOT NULL,
	 TABLE_NAME  VARCHAR(40),
	 COLUMN_NAME  VARCHAR(40),
	 RESOURCE_NAME  VARCHAR(200) NOT NULL,
	 RESOURCE_DESCR  VARCHAR(400),
	 RESOURCE_CODE  VARCHAR(45) NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
	CONSTRAINT XIF1SBI_RESOURCES UNIQUE  ( RESOURCE_CODE, ORGANIZATION ),
 PRIMARY KEY ( RESOURCE_ID )
 ) WITHOUT OIDS;

CREATE TABLE  SBI_KPI_MODEL_RESOURCES  (
	 KPI_MODEL_RESOURCES_ID  INTEGER NOT NULL ,
	 RESOURCE_ID  INTEGER NOT NULL,
	 KPI_MODEL_INST  INTEGER NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
 PRIMARY KEY ( KPI_MODEL_RESOURCES_ID )
 ) WITHOUT OIDS;

-- ALARM

CREATE TABLE  SBI_ALARM  (
	 ALARM_ID  INTEGER NOT NULL ,
	 ID_KPI_INSTANCE  INTEGER ,
	 MODALITY_ID  INTEGER NOT NULL ,
	 DOCUMENT_ID  INTEGER ,
	 LABEL  VARCHAR(50) NOT NULL,
	 NAME  VARCHAR(50),
	 DESCR  VARCHAR(200),
	 MAIL_SUBJ VARCHAR(256),
	 TEXT  VARCHAR(1000) ,
	 URL  VARCHAR(256) ,
	 SINGLE_EVENT  CHAR(1) ,
	 AUTO_DISABLED  CHAR(1) DEFAULT NULL,
	 ID_THRESHOLD_VALUE  INTEGER ,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
	   CONSTRAINT XIF1SBI_ALARM UNIQUE  ( LABEL, ORGANIZATION ),
 	   PRIMARY KEY ( ALARM_ID )
 ) WITHOUT OIDS;

CREATE TABLE  SBI_ALARM_EVENT  (
	 ALARM_EVENT_ID  INTEGER NOT NULL ,
	 ALARM_ID  INTEGER NOT NULL,
	 EVENT_TS  DATE NOT NULL DEFAULT CURRENT_TIMESTAMP ,
	 ACTIVE  CHAR(1) ,
	 KPI_VALUE  VARCHAR(50) ,
	 THRESHOLD_VALUE  VARCHAR(50),
	 KPI_NAME  VARCHAR(100) ,
	 RESOURCES  VARCHAR(200) DEFAULT NULL,
	 KPI_DESCRIPTION  VARCHAR (100),
	 RESOURCE_ID  INTEGER,
	 KPI_INSTANCE_ID  INTEGER,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
 PRIMARY KEY ( ALARM_EVENT_ID )
) WITHOUT OIDS;

CREATE TABLE  SBI_ALARM_CONTACT  (
	 ALARM_CONTACT_ID  INTEGER NOT NULL ,
	 NAME  VARCHAR(100) NOT NULL,
	 EMAIL  VARCHAR(100),
	 MOBILE  VARCHAR(50),
	 RESOURCES  VARCHAR(200) DEFAULT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
	   CONSTRAINT XIF1SBI_ALARM_CONTACT UNIQUE ( NAME, ORGANIZATION ),
 PRIMARY KEY ( ALARM_CONTACT_ID )
) WITHOUT OIDS;

CREATE TABLE SBI_ALARM_DISTRIBUTION (
	ALARM_CONTACT_ID INTEGER NOT NULL,
	ALARM_ID INTEGER NOT NULL,  
 	PRIMARY KEY (ALARM_CONTACT_ID,ALARM_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_EXPORTERS (
  ENGINE_ID INTEGER NOT NULL,
  DOMAIN_ID INTEGER NOT NULL,
  DEFAULT_VALUE BOOLEAN,
  CONSTRAINT XAK1SBI_EXPORTERS UNIQUE ( ENGINE_ID, DOMAIN_ID),
  PRIMARY KEY (ENGINE_ID, DOMAIN_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_OBJ_METADATA (
	OBJ_META_ID 		INTEGER NOT NULL ,
    LABEL	 	        VARCHAR(20) NOT NULL,
    NAME 	            VARCHAR(40) NOT NULL,
    DESCR		        VARCHAR(100),  
    DATA_TYPE_ID			INTEGER NOT NULL,
    CREATION_DATE 	    TIMESTAMP NOT NULL, 
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),  
       CONSTRAINT XAK1SBI_OBJ_METADATA UNIQUE (LABEL, ORGANIZATION),
    PRIMARY KEY (OBJ_META_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_OBJ_METACONTENTS (
  OBJ_METACONTENT_ID INTEGER  NOT NULL ,
  OBJMETA_ID 		 INTEGER  NOT NULL ,
  BIOBJ_ID 			 INTEGER  NOT NULL,
  SUBOBJ_ID 		 INTEGER,
  BIN_ID 			 INTEGER,
  CREATION_DATE 	 TIMESTAMP NOT NULL,   
  LAST_CHANGE_DATE   TIMESTAMP NOT NULL,  
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),  
       CONSTRAINT XAK1SBI_OBJ_METACONTENTS UNIQUE  (OBJMETA_ID,BIOBJ_ID,SUBOBJ_ID),
    PRIMARY KEY (OBJ_METACONTENT_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_CONFIG (
	ID 				INTEGER  NOT NULL ,
	LABEL			VARCHAR(100) NOT NULL,
	NAME			VARCHAR(100) NULL,
	DESCRIPTION 	VARCHAR(500) NULL,
	IS_ACTIVE 		BOOLEAN DEFAULT TRUE,
	VALUE_CHECK 	VARCHAR(1000) NULL,
	VALUE_TYPE_ID 	INTEGER NULL, 
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
       CATEGORY             VARCHAR(50),
       CONSTRAINT XAK1SBI_CONFIG UNIQUE  (LABEL),
 	PRIMARY KEY (ID)
) WITHOUT OIDS;

 CREATE TABLE SBI_USER (
	USER_ID VARCHAR(100) NOT NULL,
	PASSWORD VARCHAR(150),
	FULL_NAME VARCHAR(255),
	ID INTEGER NOT NULL,
	DT_PWD_BEGIN TIMESTAMP NULL DEFAULT NULL,
	DT_PWD_END TIMESTAMP NULL DEFAULT NULL,
	FLG_PWD_BLOCKED BOOLEAN,
	DT_LAST_ACCESS TIMESTAMP NULL DEFAULT NULL,
	IS_SUPERADMIN BOOLEAN DEFAULT FALSE,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
       CONSTRAINT XAK1SBI_USER UNIQUE  (USER_ID),
 	PRIMARY KEY (ID)
) WITHOUT OIDS;

CREATE TABLE SBI_ATTRIBUTE (
	ATTRIBUTE_NAME VARCHAR(255) NOT NULL,
	DESCRIPTION VARCHAR(500) NOT NULL,
	ATTRIBUTE_ID INTEGER NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
       CONSTRAINT XAK1SBI_ATTRIBUTE UNIQUE ( ATTRIBUTE_NAME, ORGANIZATION ),
 	PRIMARY KEY (ATTRIBUTE_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_USER_ATTRIBUTES (
	ID INTEGER NOT NULL,
	ATTRIBUTE_ID INTEGER NOT NULL,
	ATTRIBUTE_VALUE VARCHAR(500),
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
 	PRIMARY KEY (ID,ATTRIBUTE_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_EXT_USER_ROLES (
	ID INTEGER NOT NULL,
	EXT_ROLE_ID INTEGER NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     	
 	PRIMARY KEY (ID,EXT_ROLE_ID)
) WITHOUT OIDS;



CREATE TABLE SBI_UDP (
	UDP_ID	        INTEGER  NOT NULL ,
	TYPE_ID			INTEGER NOT NULL,
	FAMILY_ID		INTEGER NOT NULL,
	LABEL           VARCHAR(20) NOT NULL,
	NAME            VARCHAR(40) NOT NULL,
	DESCRIPTION     VARCHAR(1000) NULL,
	IS_MULTIVALUE   BOOLEAN DEFAULT FALSE,   
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),    
        CONSTRAINT XAK1SBI_UDP UNIQUE  (LABEL, ORGANIZATION),
 		PRIMARY KEY (UDP_ID)
)WITHOUT OIDS;
 
 
 
CREATE TABLE SBI_UDP_VALUE (
	UDP_VALUE_ID       INTEGER  NOT NULL ,
	UDP_ID			   INTEGER NOT NULL,
	VALUE              VARCHAR(1000) NULL,
	PROG               INTEGER NULL,
	LABEL              VARCHAR(20) NULL,
	NAME               VARCHAR(40) NULL,
	FAMILY			   VARCHAR(40) NULL,
    BEGIN_TS           TIMESTAMP NOT NULL,
    END_TS             TIMESTAMP NULL,
    REFERENCE_ID	   INTEGER NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),         
 	PRIMARY KEY (UDP_VALUE_ID)
)WITHOUT OIDS;
 
 
 CREATE TABLE SBI_KPI_REL (
  KPI_REL_ID INTEGER NOT NULL ,
  KPI_FATHER_ID INTEGER  NOT NULL,
  KPI_CHILD_ID INTEGER  NOT NULL,
  PARAMETER VARCHAR(100),
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),      
  PRIMARY KEY (KPI_REL_ID)
)WITHOUT OIDS;

 
 CREATE TABLE SBI_KPI_ERROR (
  KPI_ERROR_ID INTEGER NOT NULL ,
  KPI_MODEL_INST_ID INTEGER NOT NULL,
  USER_MSG VARCHAR(1000),
  FULL_MSG TEXT,
  TS_DATE TIMESTAMP ,
  LABEL_MOD_INST VARCHAR(100) ,
  PARAMETERS VARCHAR(1000),
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),       
  PRIMARY KEY (KPI_ERROR_ID)
)WITHOUT OIDS;


CREATE TABLE SBI_ORG_UNIT (
  ID            INTEGER NOT NULL,
  LABEL            VARCHAR(100) NOT NULL,
  NAME             VARCHAR(200) NOT NULL,
  DESCRIPTION      VARCHAR(1000),
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),     
  CONSTRAINT XIF1SBI_ORG_UNIT UNIQUE  (LABEL, NAME, ORGANIZATION),
  PRIMARY KEY(ID)
) WITHOUT OIDS;

CREATE TABLE SBI_ORG_UNIT_HIERARCHIES (
  ID            INTEGER NOT NULL,
  LABEL            VARCHAR(100) NOT NULL,
  NAME             VARCHAR(200) NOT NULL,
  DESCRIPTION      VARCHAR(1000),
  TARGET     VARCHAR(1000),
  COMPANY    VARCHAR(100) NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),      
  CONSTRAINT XIF1SBI_ORG_UNIT_HIERARCHIES UNIQUE  (LABEL, COMPANY, ORGANIZATION),
  PRIMARY KEY(ID)
) WITHOUT OIDS;

CREATE TABLE SBI_ORG_UNIT_NODES (
  NODE_ID            INTEGER NOT NULL,
  OU_ID           INTEGER NOT NULL,
  HIERARCHY_ID  INTEGER NOT NULL,
  PARENT_NODE_ID INTEGER NULL,
  PATH VARCHAR(4000) NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),      
  PRIMARY KEY(NODE_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_ORG_UNIT_GRANT (
  ID INTEGER NOT NULL,
  HIERARCHY_ID  INTEGER NOT NULL,
  KPI_MODEL_INST_NODE_ID INTEGER NOT NULL,
  START_DATE  DATE,
  END_DATE  DATE,
  LABEL            VARCHAR(200) NOT NULL,
  NAME             VARCHAR(400) NOT NULL,
  DESCRIPTION      VARCHAR(1000),
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),      
       IS_AVAILABLE 		BOOLEAN DEFAULT TRUE,
  CONSTRAINT XIF1SBI_ORG_UNIT_GRANT UNIQUE  (LABEL, ORGANIZATION),
  PRIMARY KEY(ID)
) WITHOUT OIDS;

CREATE TABLE SBI_ORG_UNIT_GRANT_NODES (
  NODE_ID INTEGER NOT NULL,
  KPI_MODEL_INST_NODE_ID INTEGER NOT NULL,
  GRANT_ID INTEGER NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),      
  PRIMARY KEY(NODE_ID, KPI_MODEL_INST_NODE_ID, GRANT_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_GOAL (
  GOAL_ID       INTEGER NOT NULL ,
  GRANT_ID      INTEGER NOT NULL,
  START_DATE    DATE NOT NULL,
  END_DATE      DATE NOT NULL,
  NAME          VARCHAR(20) NOT NULL,
  LABEL          VARCHAR(20) NOT NULL,
  DESCRIPTION		VARCHAR(1000),
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
  CONSTRAINT XIF1SBI_SBI_GOAL UNIQUE (NAME, ORGANIZATION),
  PRIMARY KEY (GOAL_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_GOAL_HIERARCHY (
  GOAL_HIERARCHY_ID INTEGER NOT NULL ,
  ORG_UNIT_ID       INTEGER NOT NULL,
  GOAL_ID           INTEGER NOT NULL,
  PARENT_ID         INTEGER,
  NAME              VARCHAR(50) NOT NULL,
  LABEL             VARCHAR(50),
  GOAL              VARCHAR(1000),
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),      
  PRIMARY KEY (GOAL_HIERARCHY_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_GOAL_KPI (
  GOAL_KPI_ID         INTEGER NOT NULL ,
  KPI_INSTANCE_ID     INTEGER NOT NULL,
  GOAL_HIERARCHY_ID   INTEGER NOT NULL,
  WEIGHT1             NUMERIC,
  WEIGHT2             NUMERIC,
  THRESHOLD1          NUMERIC,
  THRESHOLD2          NUMERIC,
  THRESHOLD1SIGN      INTEGER,
  THRESHOLD2SIGN      INTEGER,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),      
  PRIMARY KEY (GOAL_KPI_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_OBJ_PARVIEW (
         OBJ_PAR_ID         INTEGER NOT NULL,
         OBJ_PAR_FATHER_ID  INTEGER NOT NULL,
         OPERATION          VARCHAR(20) NOT NULL,
         COMPARE_VALUE      VARCHAR(200) NOT NULL,
         VIEW_LABEL         VARCHAR(50),
         PROG               INTEGER,
         USER_IN            VARCHAR(100) NOT NULL,
         USER_UP              VARCHAR(100),
         USER_DE              VARCHAR(100),
         TIME_IN              TIMESTAMP NOT NULL,
         TIME_UP              TIMESTAMP NULL DEFAULT NULL,
         TIME_DE              TIMESTAMP NULL DEFAULT NULL,
         SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),    
      PRIMARY KEY ( OBJ_PAR_ID ,  OBJ_PAR_FATHER_ID ,  OPERATION, COMPARE_VALUE )
) WITHOUT OIDS;

CREATE TABLE SBI_I18N_MESSAGES (
    LANGUAGE_CD INTEGER NOT NULL,
    LABEL VARCHAR(200) NOT NULL,
    MESSAGE TEXT,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20), 
  PRIMARY KEY (LANGUAGE_CD, LABEL, ORGANIZATION)
);

CREATE TABLE SBI_KPI_COMMENTS (
	   KPI_COMMENT_ID 			INTEGER NOT NULL ,
	   KPI_INST_ID 	        INTEGER,
       BIN_ID 	            INTEGER,
       EXEC_REQ 	        VARCHAR(500),
       OWNER 	            VARCHAR(50),
       ISPUBLIC 	        BOOLEAN,  
       CREATION_DATE 	    TIMESTAMP NOT NULL,  
       LAST_CHANGE_DATE     TIMESTAMP NOT NULL, 
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),  
       CONSTRAINT XAK1SBI_KPI_COMMENT UNIQUE  (KPI_COMMENT_ID),
       PRIMARY KEY (KPI_COMMENT_ID)
) WITHOUT OIDS;


CREATE TABLE SBI_PROGRESS_THREAD (
       PROGRESS_THREAD_ID   INTEGER NOT NULL,
       USER_ID              VARCHAR(100) NOT NULL,
       PARTIAL              INTEGER,
       TOTAL        	      INTEGER,
       FUNCTION_CD         VARCHAR(200),
       STATUS             VARCHAR(4000),
       RANDOM_KEY			    VARCHAR(4000),	
       TYPE               VARCHAR(200),
       PRIMARY KEY (PROGRESS_THREAD_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_ORGANIZATIONS (
       ID     INTEGER NOT NULL,
       NAME   VARCHAR(20),
       THEME VARCHAR(100) NULL DEFAULT 'SPAGOBI.THEMES.THEME.default',
       CONSTRAINT XAK1SBI_ORGANIZATIONS UNIQUE (NAME),
       PRIMARY KEY (ID)
) WITHOUT OIDS;

CREATE TABLE SBI_META_MODELS (
       ID                   INTEGER NOT NULL,
       NAME                 VARCHAR(100) NOT NULL,
       DESCR                VARCHAR(500) NULL,
       MODEL_LOCKED         BOOLEAN NULL,
       MODEL_LOCKER         VARCHAR(100) NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,    
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20), 
       CATEGORY_ID			INTEGER NULL,
       DATA_SOURCE_ID		INTEGER,
       CONSTRAINT XAK1SBI_META_MODELS UNIQUE (NAME, ORGANIZATION),
       PRIMARY KEY (ID)
) WITHOUT OIDS;

CREATE TABLE SBI_META_MODELS_VERSIONS (
        ID                   INTEGER NOT NULL,
        MODEL_ID             INTEGER NOT NULL,
        CONTENT              BYTEA NOT NULL,
        NAME                 VARCHAR(100),  
        PROG                 INTEGER,
        CREATION_DATE        TIMESTAMP NULL DEFAULT NULL,
        CREATION_USER        VARCHAR(50) NOT NULL, 
        ACTIVE               BOOLEAN,
        USER_IN              VARCHAR(100) NOT NULL,
        USER_UP              VARCHAR(100),
        USER_DE              VARCHAR(100),
        TIME_IN              TIMESTAMP NOT NULL,
        TIME_UP              TIMESTAMP NULL DEFAULT NULL,    
        TIME_DE              TIMESTAMP NULL DEFAULT NULL,
        SBI_VERSION_IN       VARCHAR(10),
        SBI_VERSION_UP       VARCHAR(10),
        SBI_VERSION_DE       VARCHAR(10),
        META_VERSION         VARCHAR(100),
        ORGANIZATION         VARCHAR(20), 
        PRIMARY KEY (ID)
) WITHOUT OIDS;

CREATE TABLE SBI_ARTIFACTS (
       ID                   INTEGER NOT NULL,
       NAME                 VARCHAR(100) NOT NULL,
       DESCR                VARCHAR(500) NULL,
       TYPE                 VARCHAR(50) NULL,
       MODEL_LOCKED         BOOLEAN NULL,
       MODEL_LOCKER         VARCHAR(100) NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,    
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20), 
       CONSTRAINT XAK1SBI_ARTIFACTS UNIQUE (NAME, TYPE, ORGANIZATION),
       PRIMARY KEY (ID)
) WITHOUT OIDS;

CREATE TABLE SBI_ARTIFACTS_VERSIONS (
        ID                   INTEGER NOT NULL,
        ARTIFACT_ID          INTEGER NOT NULL,
        CONTENT              BYTEA NOT NULL,
        NAME                 VARCHAR(100),  
        PROG                 INTEGER,
        CREATION_DATE        TIMESTAMP NULL DEFAULT NULL,
        CREATION_USER        VARCHAR(50) NOT NULL, 
        ACTIVE               BOOLEAN,
        USER_IN              VARCHAR(100) NOT NULL,
        USER_UP              VARCHAR(100),
        USER_DE              VARCHAR(100),
        TIME_IN              TIMESTAMP NOT NULL,
        TIME_UP              TIMESTAMP NULL DEFAULT NULL,    
        TIME_DE              TIMESTAMP NULL DEFAULT NULL,
        SBI_VERSION_IN       VARCHAR(10),
        SBI_VERSION_UP       VARCHAR(10),
        SBI_VERSION_DE       VARCHAR(10),
        META_VERSION         VARCHAR(100),
        ORGANIZATION         VARCHAR(20), 
        PRIMARY KEY (ID)
) WITHOUT OIDS;

CREATE TABLE  SBI_EXT_ROLES_CATEGORY (
  EXT_ROLE_ID INTEGER NOT NULL,
  CATEGORY_ID INTEGER NOT NULL,
  PRIMARY KEY (EXT_ROLE_ID,CATEGORY_ID),
  CONSTRAINT FK_SB_EXT_ROLES_META_MODEL_CATEGORY_1 FOREIGN KEY (EXT_ROLE_ID) REFERENCES SBI_EXT_ROLES (EXT_ROLE_ID),
  CONSTRAINT FK_SB_EXT_ROLES_META_MODEL_CATEGORY_2 FOREIGN KEY (CATEGORY_ID) REFERENCES SBI_DOMAINS (VALUE_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_COMMUNITY (
	  COMMUNITY_ID INTEGER NOT NULL,
	  NAME varchar(200) NOT NULL,
	  DESCRIPTION varchar(350) DEFAULT NULL,
	  OWNER varchar(200) NOT NULL,
	  FUNCT_CODE varchar(40) DEFAULT NULL,
	  CREATION_DATE timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	  LAST_CHANGE_DATE timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	  USER_IN varchar(100) NOT NULL,
	  USER_UP varchar(100) DEFAULT NULL,
	  USER_DE varchar(100) DEFAULT NULL,
	  TIME_IN timestamp NOT NULL,
	  TIME_UP timestamp NULL DEFAULT NULL,
	  TIME_DE timestamp NULL DEFAULT NULL,
	  SBI_VERSION_IN varchar(10) DEFAULT NULL,
	  SBI_VERSION_UP varchar(10) DEFAULT NULL,
	  SBI_VERSION_DE varchar(10) DEFAULT NULL,
	  META_VERSION varchar(100) DEFAULT NULL,
	  ORGANIZATION varchar(20) DEFAULT NULL,
	  PRIMARY KEY (COMMUNITY_ID)
) WITHOUT OIDS;

CREATE TABLE SBI_COMMUNITY_USERS (
  COMMUNITY_ID INTEGER NOT NULL,
  USER_ID varchar(100) NOT NULL,
  CREATION_DATE timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  LAST_CHANGE_DATE timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  USER_IN varchar(100) NOT NULL,
  USER_UP varchar(100) DEFAULT NULL,
  USER_DE varchar(100) DEFAULT NULL,
  TIME_IN timestamp NOT NULL,
  TIME_UP timestamp NULL DEFAULT NULL,
  TIME_DE timestamp NULL DEFAULT NULL,
  SBI_VERSION_IN varchar(10) DEFAULT NULL,
  SBI_VERSION_UP varchar(10) DEFAULT NULL,
  SBI_VERSION_DE varchar(10) DEFAULT NULL,
  META_VERSION varchar(100) DEFAULT NULL,
  ORGANIZATION varchar(20) DEFAULT NULL,
  PRIMARY KEY (COMMUNITY_ID,USER_ID),
  CONSTRAINT FK_COMMUNITY FOREIGN KEY (COMMUNITY_ID) REFERENCES SBI_COMMUNITY (COMMUNITY_ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_USER FOREIGN KEY (USER_ID) REFERENCES SBI_USER (USER_ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) WITHOUT OIDS;

CREATE TABLE SBI_GEO_LAYERS (
  LAYER_ID INTEGER NOT NULL,
  LABEL varchar(100) NOT NULL,
  NAME varchar(100),
  DESCR varchar(100),
  LAYER_DEFINITION BYTEA NOT NULL,
  TYPE varchar(40),
  IS_BASE_LAYER BOOLEAN DEFAULT FALSE,
  USER_IN varchar(100) NOT NULL,
  USER_UP varchar(100) DEFAULT NULL,
  USER_DE varchar(100) DEFAULT NULL,
  TIME_IN timestamp NOT NULL,
  TIME_UP timestamp NULL DEFAULT NULL,
  TIME_DE timestamp NULL DEFAULT NULL,
  SBI_VERSION_IN varchar(10) DEFAULT NULL,
  SBI_VERSION_UP varchar(10) DEFAULT NULL,
  SBI_VERSION_DE varchar(10) DEFAULT NULL,
  META_VERSION varchar(100) DEFAULT NULL,
  ORGANIZATION varchar(20) DEFAULT NULL,
  CONSTRAINT XAK1SBI_GEO_LAYERS UNIQUE (NAME, TYPE, ORGANIZATION),
  PRIMARY KEY (LAYER_ID)
) ;

CREATE TABLE SBI_AUTHORIZATIONS (
  ID INTEGER NOT NULL,
  NAME VARCHAR(200) DEFAULT NULL,
  USER_IN VARCHAR(100) NOT NULL,
  USER_UP VARCHAR(100) DEFAULT NULL,
  USER_DE VARCHAR(100) DEFAULT NULL,
  TIME_IN timestamp NOT NULL,
  TIME_UP timestamp DEFAULT NULL,
  TIME_DE timestamp DEFAULT NULL,
  SBI_VERSION_IN VARCHAR(10) DEFAULT NULL,
  SBI_VERSION_UP VARCHAR(10) DEFAULT NULL,
  SBI_VERSION_DE VARCHAR(10) DEFAULT NULL,
  META_VERSION VARCHAR(100) DEFAULT NULL,
  ORGANIZATION VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (ID)
);
CREATE TABLE SBI_AUTHORIZATIONS_ROLES (
  AUTHORIZATION_ID INTEGER NOT NULL,
  ROLE_ID INTEGER NOT NULL,
  USER_IN VARCHAR(100) NOT NULL,
  USER_UP VARCHAR(100) DEFAULT NULL,
  USER_DE VARCHAR(100) DEFAULT NULL,
  TIME_IN timestamp NOT NULL,
  TIME_UP timestamp  DEFAULT NULL,
  TIME_DE timestamp  DEFAULT NULL,
  SBI_VERSION_IN VARCHAR(10) DEFAULT NULL,
  SBI_VERSION_UP VARCHAR(10) DEFAULT NULL,
  SBI_VERSION_DE VARCHAR(10) DEFAULT NULL,
  META_VERSION VARCHAR(100) DEFAULT NULL,
  ORGANIZATION VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (AUTHORIZATION_ID,ROLE_ID )
);

CREATE TABLE SBI_ORGANIZATION_ENGINE (
  ENGINE_ID INTEGER NOT NULL,
  ORGANIZATION_ID INTEGER NOT NULL,
  CREATION_DATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  LAST_CHANGE_DATE timestamp NULL DEFAULT NULL,
  USER_IN VARCHAR(100) NOT NULL,
  USER_UP VARCHAR(100) DEFAULT NULL,
  USER_DE VARCHAR(100) DEFAULT NULL,
  TIME_IN TIMESTAMP NULL DEFAULT NULL,
  TIME_UP TIMESTAMP NULL DEFAULT NULL,
  TIME_DE TIMESTAMP NULL DEFAULT NULL,
  SBI_VERSION_IN VARCHAR(10) DEFAULT NULL,
  SBI_VERSION_UP VARCHAR(10) DEFAULT NULL,
  SBI_VERSION_DE VARCHAR(10) DEFAULT NULL,
  META_VERSION VARCHAR(100) DEFAULT NULL,
  ORGANIZATION VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (ENGINE_ID,ORGANIZATION_ID )
) ;


CREATE TABLE SBI_ORGANIZATION_DATASOURCE (
  DATASOURCE_ID INTEGER NOT NULL,
  ORGANIZATION_ID INTEGER NOT NULL,
  CREATION_DATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  LAST_CHANGE_DATE TIMESTAMP NULL DEFAULT NULL,
  USER_IN VARCHAR(100) NOT NULL,
  USER_UP VARCHAR(100) DEFAULT NULL,
  USER_DE VARCHAR(100) DEFAULT NULL,
  TIME_IN TIMESTAMP NULL DEFAULT NULL,
  TIME_UP TIMESTAMP NULL DEFAULT NULL,
  TIME_DE TIMESTAMP NULL DEFAULT NULL,
  SBI_VERSION_IN VARCHAR(10) DEFAULT NULL,
  SBI_VERSION_UP VARCHAR(10) DEFAULT NULL,
  SBI_VERSION_DE VARCHAR(10) DEFAULT NULL,
  META_VERSION VARCHAR(100) DEFAULT NULL,
  ORGANIZATION VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (DATASOURCE_ID,ORGANIZATION_ID )
) ;

CREATE TABLE SBI_TRIGGER_PAUSED (
	   ID 					INTEGER  NOT NULL ,
       TRIGGER_NAME	 	    VARCHAR(80) NOT NULL,
       TRIGGER_GROUP 	    VARCHAR(80) NOT NULL,
       JOB_NAME 	        VARCHAR(80) NOT NULL,
       JOB_GROUP 	        VARCHAR(80) NOT NULL,	   	   
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),  
       CONSTRAINT XAK1SBI_TRIGGER_PAUSED UNIQUE(TRIGGER_NAME, TRIGGER_GROUP, JOB_NAME, JOB_GROUP),
       PRIMARY KEY (ID)
) WITHOUT OIDS;

CREATE TABLE SBI_CACHE_ITEM (
	   SIGNATURE			VARCHAR(100) NOT NULL,
	   NAME					VARCHAR(100) NULL,
	   TABLE_NAME 			VARCHAR(100) NOT NULL,
	   DIMENSION 			NUMERIC NULL,
	   CREATION_DATE 		DATE NULL,
	   LAST_USED_DATE 		DATE NULL,
       PROPERTIES			TEXT NULL,
	   USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
       CONSTRAINT XAK1SBI_CACHE_ITEM UNIQUE(TABLE_NAME),
	   PRIMARY KEY (SIGNATURE)
);

CREATE TABLE SBI_CACHE_JOINED_ITEM (
	   ID 					INTEGER  NOT NULL,
	   SIGNATURE			VARCHAR(100) NOT NULL,
	   JOINED_SIGNATURE		VARCHAR(100) NOT NULL,
       USER_IN              VARCHAR(100) NOT NULL,
       USER_UP              VARCHAR(100),
       USER_DE              VARCHAR(100),
       TIME_IN              TIMESTAMP NOT NULL,
       TIME_UP              TIMESTAMP NULL DEFAULT NULL,
       TIME_DE              TIMESTAMP NULL DEFAULT NULL,
       SBI_VERSION_IN       VARCHAR(10),
       SBI_VERSION_UP       VARCHAR(10),
       SBI_VERSION_DE       VARCHAR(10),
       META_VERSION         VARCHAR(100),
       ORGANIZATION         VARCHAR(20),
       CONSTRAINT XAK1SBI_CACHE_JOINED_ITEM UNIQUE(SIGNATURE, JOINED_SIGNATURE),
	   PRIMARY KEY (ID)
);

ALTER TABLE SBI_CACHE_JOINED_ITEM  ADD CONSTRAINT FK_SBI_CACHE_JOINED_ITEM_1 FOREIGN KEY ( SIGNATURE ) REFERENCES  SBI_CACHE_ITEM  ( SIGNATURE ) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE SBI_CACHE_JOINED_ITEM  ADD CONSTRAINT FK_SBI_CACHE_JOINED_ITEM_2 FOREIGN KEY ( JOINED_SIGNATURE ) REFERENCES  SBI_CACHE_ITEM  ( SIGNATURE ) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE SBI_CHECKS ADD CONSTRAINT FK_SBI_CHECKS_1 FOREIGN KEY  ( VALUE_TYPE_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_EXT_ROLES ADD CONSTRAINT FK_SBI_EXT_ROLES_1 FOREIGN KEY  ( ROLE_TYPE_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_FUNC_ROLE ADD CONSTRAINT FK_SBI_FUNC_ROLE_3 FOREIGN KEY  ( FUNCT_ID ) REFERENCES SBI_FUNCTIONS ( FUNCT_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_FUNC_ROLE ADD CONSTRAINT FK_SBI_FUNC_ROLE_1 FOREIGN KEY  ( ROLE_ID ) REFERENCES SBI_EXT_ROLES ( EXT_ROLE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_FUNC_ROLE ADD CONSTRAINT FK_SBI_FUNC_ROLE_2 FOREIGN KEY  ( STATE_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_FUNCTIONS ADD CONSTRAINT FK_SBI_FUNCTIONS_1 FOREIGN KEY  ( FUNCT_TYPE_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_FUNCTIONS ADD CONSTRAINT FK_SBI_FUNCTIONS_2 FOREIGN KEY  ( PARENT_FUNCT_ID ) REFERENCES SBI_FUNCTIONS ( FUNCT_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_LOV ADD CONSTRAINT FK_SBI_LOV_1 FOREIGN KEY  ( INPUT_TYPE_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJ_FUNC ADD CONSTRAINT FK_SBI_OBJ_FUNC_2 FOREIGN KEY  ( BIOBJ_ID ) REFERENCES SBI_OBJECTS ( BIOBJ_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJ_FUNC ADD CONSTRAINT FK_SBI_OBJ_FUNC_1 FOREIGN KEY  ( FUNCT_ID ) REFERENCES SBI_FUNCTIONS ( FUNCT_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJ_PAR ADD CONSTRAINT FK_SBI_OBJ_PAR_1 FOREIGN KEY  ( BIOBJ_ID ) REFERENCES SBI_OBJECTS ( BIOBJ_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJ_PAR ADD CONSTRAINT FK_SBI_OBJ_PAR_2 FOREIGN KEY  ( PAR_ID ) REFERENCES SBI_PARAMETERS ( PAR_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJ_STATE ADD CONSTRAINT FK_SBI_OBJ_STATE_1 FOREIGN KEY  ( BIOBJ_ID ) REFERENCES SBI_OBJECTS ( BIOBJ_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJ_STATE ADD CONSTRAINT FK_SBI_OBJ_STATE_2 FOREIGN KEY  ( STATE_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJECTS ADD CONSTRAINT FK_SBI_OBJECTS_2 FOREIGN KEY  ( STATE_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJECTS ADD CONSTRAINT FK_SBI_OBJECTS_3 FOREIGN KEY  ( BIOBJ_TYPE_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
--ALTER TABLE SBI_OBJECTS ADD CONSTRAINT FK_SBI_OBJECTS_5 FOREIGN KEY  ( ENGINE_ID ) REFERENCES SBI_ENGINES ( ENGINE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJECTS ADD CONSTRAINT FK_SBI_OBJECTS_4 FOREIGN KEY  ( EXEC_MODE_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJECTS ADD CONSTRAINT FK_SBI_OBJECTS_1 FOREIGN KEY  ( STATE_CONS_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
--ALTER TABLE SBI_OBJECTS ADD CONSTRAINT FK_SBI_OBJECTS_6 FOREIGN KEY  ( DATA_SOURCE_ID ) REFERENCES SBI_DATA_SOURCE ( DS_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJECTS_RATING ADD CONSTRAINT FK_SBI_OBJECTS_RATING_1 FOREIGN KEY  (OBJ_ID) REFERENCES SBI_OBJECTS (BIOBJ_ID)ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE SBI_PARAMETERS ADD CONSTRAINT FK_SBI_PARAMETERS_1 FOREIGN KEY  ( PAR_TYPE_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_PARUSE ADD CONSTRAINT FK_SBI_PARUSE_1 FOREIGN KEY  ( PAR_ID ) REFERENCES SBI_PARAMETERS ( PAR_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_PARUSE ADD CONSTRAINT FK_SBI_PARUSE_2 FOREIGN KEY  ( LOV_ID ) REFERENCES SBI_LOV ( LOV_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_PARUSE ADD CONSTRAINT FK_SBI_PARUSE_3 FOREIGN KEY  ( DEFAULT_LOV_ID ) REFERENCES SBI_LOV ( LOV_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_PARUSE_CK ADD CONSTRAINT FK_SBI_PARUSE_CK_1 FOREIGN KEY  ( USE_ID ) REFERENCES SBI_PARUSE ( USE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_PARUSE_CK ADD CONSTRAINT FK_SBI_PARUSE_CK_2 FOREIGN KEY  ( CHECK_ID ) REFERENCES SBI_CHECKS ( CHECK_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_PARUSE_DET ADD CONSTRAINT FK_SBI_PARUSE_DET_1 FOREIGN KEY  ( USE_ID ) REFERENCES SBI_PARUSE ( USE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_PARUSE_DET ADD CONSTRAINT FK_SBI_PARUSE_DET_2 FOREIGN KEY  ( EXT_ROLE_ID ) REFERENCES SBI_EXT_ROLES ( EXT_ROLE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJ_PARUSE ADD CONSTRAINT FK_SBI_OBJ_PARUSE_1 FOREIGN KEY  ( OBJ_PAR_ID ) REFERENCES SBI_OBJ_PAR ( OBJ_PAR_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJ_PARUSE ADD CONSTRAINT FK_SBI_OBJ_PARUSE_2 FOREIGN KEY  ( USE_ID ) REFERENCES SBI_PARUSE ( USE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJ_PARUSE ADD CONSTRAINT FK_SBI_OBJ_PARUSE_3 FOREIGN KEY  ( OBJ_PAR_FATHER_ID ) REFERENCES SBI_OBJ_PAR ( OBJ_PAR_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_ENGINES ADD CONSTRAINT FK_SBI_ENGINES_1 FOREIGN KEY  ( BIOBJ_TYPE ) REFERENCES SBI_DOMAINS ( VALUE_ID );
ALTER TABLE SBI_ENGINES ADD CONSTRAINT FK_SBI_ENGINES_2 FOREIGN KEY  ( ENGINE_TYPE ) REFERENCES SBI_DOMAINS ( VALUE_ID );
ALTER TABLE SBI_EVENTS_ROLES ADD CONSTRAINT FK_SBI_EVENTS_ROLES_1 FOREIGN KEY  ( ROLE_ID ) REFERENCES SBI_EXT_ROLES ( EXT_ROLE_ID );
ALTER TABLE SBI_EVENTS_ROLES ADD CONSTRAINT FK_SBI_EVENTS_ROLES_2 FOREIGN KEY  ( EVENT_ID ) REFERENCES SBI_EVENTS_LOG ( ID );
ALTER TABLE SBI_SUBREPORTS ADD CONSTRAINT FK_SBI_SUBREPORTS_1 FOREIGN KEY  ( MASTER_RPT_ID ) REFERENCES SBI_OBJECTS ( BIOBJ_ID );
ALTER TABLE SBI_SUBREPORTS ADD CONSTRAINT FK_SBI_SUBREPORTS_2 FOREIGN KEY  ( SUB_RPT_ID ) REFERENCES SBI_OBJECTS ( BIOBJ_ID );
ALTER TABLE SBI_AUDIT ADD CONSTRAINT FK_SBI_AUDIT_1 FOREIGN KEY  (DOC_REF) REFERENCES SBI_OBJECTS ( BIOBJ_ID ) ON DELETE SET NULL ;
ALTER TABLE SBI_AUDIT ADD CONSTRAINT FK_SBI_AUDIT_2 FOREIGN KEY  (ENGINE_REF) REFERENCES SBI_ENGINES (ENGINE_ID) ON DELETE SET NULL ;
ALTER TABLE SBI_AUDIT ADD CONSTRAINT FK_SBI_AUDIT_3 FOREIGN KEY  (SUBOBJ_REF) REFERENCES SBI_SUBOBJECTS ( SUBOBJ_ID ) ON DELETE SET NULL ;
ALTER TABLE SBI_GEO_MAPS ADD CONSTRAINT FK_SBI_GEO_MAPS_1 FOREIGN KEY  (BIN_ID) REFERENCES SBI_BINARY_CONTENTS(BIN_ID);
ALTER TABLE SBI_GEO_MAP_FEATURES ADD CONSTRAINT FK_GEO_MAP_FEATURES1 FOREIGN KEY  (MAP_ID) REFERENCES SBI_GEO_MAPS (MAP_ID); 
ALTER TABLE SBI_GEO_MAP_FEATURES ADD CONSTRAINT FK_GEO_MAP_FEATURES2 FOREIGN KEY  (FEATURE_ID) REFERENCES SBI_GEO_FEATURES (FEATURE_ID); 
ALTER TABLE SBI_VIEWPOINTS ADD CONSTRAINT FK_SBI_VIEWPOINTS_1 FOREIGN KEY  ( BIOBJ_ID ) REFERENCES SBI_OBJECTS ( BIOBJ_ID );

ALTER TABLE SBI_OBJECT_TEMPLATES ADD CONSTRAINT FK_SBI_OBJECT_TEMPLATES_1 FOREIGN KEY  ( BIN_ID ) REFERENCES SBI_BINARY_CONTENTS(BIN_ID);
ALTER TABLE SBI_OBJECT_TEMPLATES ADD CONSTRAINT FK_SBI_OBJECT_TEMPLATES_2 FOREIGN KEY  ( BIOBJ_ID ) REFERENCES SBI_OBJECTS(BIOBJ_ID);

ALTER TABLE SBI_OBJECT_NOTES ADD CONSTRAINT FK_SBI_OBJECT_NOTES_1 FOREIGN KEY  ( BIN_ID ) REFERENCES SBI_BINARY_CONTENTS(BIN_ID);
ALTER TABLE SBI_OBJECT_NOTES ADD CONSTRAINT FK_SBI_OBJECT_NOTES_2 FOREIGN KEY  ( BIOBJ_ID ) REFERENCES SBI_OBJECTS(BIOBJ_ID);

ALTER TABLE SBI_SUBOBJECTS ADD CONSTRAINT FK_SBI_SUBOBJECTS_1 FOREIGN KEY  ( BIN_ID ) REFERENCES SBI_BINARY_CONTENTS(BIN_ID);
ALTER TABLE SBI_SUBOBJECTS ADD CONSTRAINT FK_SBI_SUBOBJECTS_2 FOREIGN KEY  ( BIOBJ_ID ) REFERENCES SBI_OBJECTS(BIOBJ_ID);

ALTER TABLE SBI_SNAPSHOTS ADD CONSTRAINT FK_SBI_SNAPSHOTS_1 FOREIGN KEY  ( BIN_ID ) REFERENCES SBI_BINARY_CONTENTS(BIN_ID);
ALTER TABLE SBI_SNAPSHOTS ADD CONSTRAINT FK_SBI_SNAPSHOTS_2 FOREIGN KEY  ( BIOBJ_ID ) REFERENCES SBI_OBJECTS(BIOBJ_ID);

ALTER TABLE SBI_ROLE_TYPE_USER_FUNC ADD CONSTRAINT FK_SBI_ROLE_TYPE_USER_FUNC_1 FOREIGN KEY  ( ROLE_TYPE_ID ) REFERENCES SBI_DOMAINS(VALUE_ID);
ALTER TABLE SBI_ROLE_TYPE_USER_FUNC ADD CONSTRAINT FK_SBI_ROLE_TYPE_USER_FUNC_2 FOREIGN KEY  ( USER_FUNCT_ID ) REFERENCES SBI_USER_FUNC(USER_FUNCT_ID);
ALTER TABLE SBI_DATA_SOURCE ADD CONSTRAINT FK_SBI_DATA_SOURCE_1 FOREIGN KEY  ( DIALECT_ID ) REFERENCES SBI_DOMAINS(VALUE_ID);

ALTER TABLE SBI_DOSSIER_PRES ADD CONSTRAINT FK_SBI_DOSSIER_PRES_1 FOREIGN KEY  ( BIN_ID ) REFERENCES SBI_BINARY_CONTENTS(BIN_ID);
ALTER TABLE SBI_DOSSIER_PRES ADD CONSTRAINT FK_SBI_DOSSIER_PRES_2 FOREIGN KEY  ( BIOBJ_ID ) REFERENCES SBI_OBJECTS(BIOBJ_ID);
ALTER TABLE SBI_DOSSIER_TEMP ADD CONSTRAINT FK_SBI_DOSSIER_TEMP_1 FOREIGN KEY  ( BIOBJ_ID ) REFERENCES SBI_OBJECTS(BIOBJ_ID);
ALTER TABLE SBI_DOSSIER_BIN_TEMP ADD CONSTRAINT FK_SBI_DOSSIER_BIN_TEMP_1 FOREIGN KEY  ( PART_ID ) REFERENCES SBI_DOSSIER_TEMP(PART_ID) ON DELETE CASCADE;

ALTER TABLE SBI_DIST_LIST_USER ADD CONSTRAINT FK_SBI_DIST_LIST_USER_1 FOREIGN KEY  ( LIST_ID ) REFERENCES SBI_DIST_LIST(DL_ID)ON DELETE CASCADE;
ALTER TABLE SBI_DIST_LIST_OBJECTS ADD CONSTRAINT FK_SBI_DIST_LIST_OBJECTS_1 FOREIGN KEY  ( DOC_ID ) REFERENCES SBI_OBJECTS(BIOBJ_ID)ON DELETE CASCADE;
ALTER TABLE SBI_DIST_LIST_OBJECTS ADD CONSTRAINT FK_SBI_DIST_LIST_OBJECTS_2 FOREIGN KEY  ( DL_ID ) REFERENCES SBI_DIST_LIST(DL_ID)ON DELETE CASCADE;

ALTER TABLE SBI_REMEMBER_ME ADD CONSTRAINT FK_SBI_REMEMBER_ME_1 FOREIGN KEY  ( BIOBJ_ID ) REFERENCES SBI_OBJECTS(BIOBJ_ID) ON DELETE CASCADE;
ALTER TABLE SBI_REMEMBER_ME ADD CONSTRAINT FK_SBI_REMEMBER_ME_2 FOREIGN KEY  ( SUBOBJ_ID ) REFERENCES SBI_SUBOBJECTS(SUBOBJ_ID) ON DELETE CASCADE;

ALTER TABLE SBI_MENU_ROLE ADD CONSTRAINT FK_SBI_MENU_ROLE1 FOREIGN KEY  (MENU_ID) REFERENCES SBI_MENU (MENU_ID) ON DELETE CASCADE; 
ALTER TABLE SBI_MENU_ROLE ADD CONSTRAINT FK_SBI_MENU_ROLE2 FOREIGN KEY  (EXT_ROLE_ID) REFERENCES SBI_EXT_ROLES (EXT_ROLE_ID) ON DELETE CASCADE;

-- METADATA
ALTER TABLE SBI_OBJ_METADATA ADD CONSTRAINT FK_SBI_OBJ_METADATA_1 FOREIGN KEY  ( DATA_TYPE_ID ) REFERENCES SBI_DOMAINS(VALUE_ID);
ALTER TABLE SBI_OBJ_METACONTENTS ADD CONSTRAINT FK_SBI_OBJ_METACONTENTS_1 FOREIGN KEY  ( OBJMETA_ID ) REFERENCES SBI_OBJ_METADATA (  OBJ_META_ID );
ALTER TABLE SBI_OBJ_METACONTENTS ADD CONSTRAINT FK_SBI_OBJ_METACONTENTS_2 FOREIGN KEY  ( BIOBJ_ID )   REFERENCES SBI_OBJECTS (  BIOBJ_ID );
ALTER TABLE SBI_OBJ_METACONTENTS ADD CONSTRAINT FK_SBI_OBJ_METACONTENTS_3 FOREIGN KEY  ( SUBOBJ_ID )  REFERENCES SBI_SUBOBJECTS (  SUBOBJ_ID ) ;
ALTER TABLE SBI_OBJ_METACONTENTS ADD CONSTRAINT FK_SBI_OBJ_METACONTENTS_4 FOREIGN KEY  ( BIN_ID )     REFERENCES SBI_BINARY_CONTENTS(BIN_ID);
-- DEFINITION
ALTER TABLE  SBI_THRESHOLD  ADD  CONSTRAINT FK_SBI_THRESHOLD_1 FOREIGN KEY ( THRESHOLD_TYPE_ID ) REFERENCES  SBI_DOMAINS  ( VALUE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_MEASURE_UNIT  ADD  CONSTRAINT FK_SBI_MEASURE_UNIT_1 FOREIGN KEY ( SCALE_TYPE_ID ) REFERENCES  SBI_DOMAINS  ( VALUE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_THRESHOLD_VALUE  ADD  CONSTRAINT FK_SBI_THRESHOLD_VALUE_1 FOREIGN KEY ( SEVERITY_ID ) REFERENCES  SBI_DOMAINS  ( VALUE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_THRESHOLD_VALUE  ADD  CONSTRAINT FK_THRESHOLD_VALUE_2 FOREIGN KEY ( THRESHOLD_ID ) REFERENCES  SBI_THRESHOLD  ( THRESHOLD_ID ) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE  SBI_KPI_ROLE  ADD  CONSTRAINT FK_SBI_KPI_ROLE_1 FOREIGN KEY ( EXT_ROLE_ID ) REFERENCES  SBI_EXT_ROLES  ( EXT_ROLE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_ROLE  ADD CONSTRAINT FK_SBI_KPI_ROLE_2 FOREIGN KEY ( KPI_ID ) REFERENCES  SBI_KPI  ( KPI_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI  ADD  CONSTRAINT FK_SBI_KPI_1 FOREIGN KEY ( ID_MEASURE_UNIT ) REFERENCES  SBI_MEASURE_UNIT  ( ID_MEASURE_UNIT ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI  ADD  CONSTRAINT FK_SBI_KPI_2 FOREIGN KEY ( THRESHOLD_ID ) REFERENCES  SBI_THRESHOLD  ( THRESHOLD_ID ) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE  SBI_KPI  ADD  CONSTRAINT FK_SBI_KPI_3 FOREIGN KEY ( ID_KPI_PARENT ) REFERENCES  SBI_KPI  ( KPI_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE SBI_KPI_DOCUMENTS  ADD CONSTRAINT FK_SBI_KPI_DOCUMENTS_1 FOREIGN KEY ( BIOBJ_ID ) REFERENCES  SBI_OBJECTS  ( BIOBJ_ID ) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE  SBI_KPI_DOCUMENTS  ADD  CONSTRAINT FK_SBI_KPI_DOCUMENTS_2 FOREIGN KEY ( KPI_ID ) REFERENCES  SBI_KPI  ( KPI_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_MODEL  ADD  CONSTRAINT FK_SBI_KPI_MODEL_1 FOREIGN KEY ( KPI_PARENT_MODEL_ID ) REFERENCES  SBI_KPI_MODEL  ( KPI_MODEL_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_MODEL  ADD CONSTRAINT FK_KPI_MODEL_2 FOREIGN KEY ( KPI_MODEL_TYPE_ID ) REFERENCES  SBI_DOMAINS  ( VALUE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_MODEL  ADD  CONSTRAINT FK_SBI_KPI_MODEL_3 FOREIGN KEY ( KPI_ID ) REFERENCES  SBI_KPI  ( KPI_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;

-- INSTANCE
ALTER TABLE  SBI_KPI_INSTANCE  ADD  CONSTRAINT FK_SBI_KPI_INSTANCE_1 FOREIGN KEY ( KPI_ID ) REFERENCES  SBI_KPI  ( KPI_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_INSTANCE  ADD CONSTRAINT FK_SBI_KPI_INSTANCE_2 FOREIGN KEY ( CHART_TYPE_ID ) REFERENCES  SBI_DOMAINS  ( VALUE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_INSTANCE  ADD  CONSTRAINT FK_SBI_KPI_INSTANCE_3 FOREIGN KEY ( ID_MEASURE_UNIT ) REFERENCES  SBI_MEASURE_UNIT  ( ID_MEASURE_UNIT ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_INSTANCE  ADD CONSTRAINT FK_SBI_KPI_INSTANCE_4 FOREIGN KEY ( THRESHOLD_ID ) REFERENCES  SBI_THRESHOLD  ( THRESHOLD_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_INSTANCE_HISTORY  ADD CONSTRAINT FK_SBI_KPI_INSTANCE_HISTORY_1 FOREIGN KEY ( ID_MEASURE_UNIT ) REFERENCES  SBI_MEASURE_UNIT  ( ID_MEASURE_UNIT ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_INSTANCE_HISTORY  ADD CONSTRAINT FK_SBI_KPI_INSTANCE_HISTORY_2 FOREIGN KEY ( THRESHOLD_ID ) REFERENCES  SBI_THRESHOLD  ( THRESHOLD_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_INSTANCE_HISTORY  ADD CONSTRAINT FK_SBI_KPI_INSTANCE_HISTORY_3 FOREIGN KEY ( CHART_TYPE_ID ) REFERENCES  SBI_DOMAINS  ( VALUE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_INSTANCE_HISTORY  ADD  CONSTRAINT FK_SBI_KPI_INSTANCE_HISTORY_4 FOREIGN KEY ( ID_KPI_INSTANCE ) REFERENCES  SBI_KPI_INSTANCE  ( ID_KPI_INSTANCE ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_MODEL_INST  ADD CONSTRAINT FK_SBI_KPI_MODEL_INST_1 FOREIGN KEY ( KPI_MODEL_INST_PARENT ) REFERENCES  SBI_KPI_MODEL_INST  ( KPI_MODEL_INST ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_MODEL_INST  ADD  CONSTRAINT FK_SBI_KPI_MODEL_INST_2 FOREIGN KEY ( KPI_MODEL_ID ) REFERENCES  SBI_KPI_MODEL  ( KPI_MODEL_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_MODEL_INST  ADD CONSTRAINT FK_SBI_KPI_MODEL_INST_3 FOREIGN KEY ( ID_KPI_INSTANCE ) REFERENCES  SBI_KPI_INSTANCE  ( ID_KPI_INSTANCE ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_MODEL_RESOURCES  ADD  CONSTRAINT FK_SBI_KPI_MODEL_RESOURCES_1 FOREIGN KEY ( KPI_MODEL_INST ) REFERENCES  SBI_KPI_MODEL_INST  ( KPI_MODEL_INST ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_MODEL_RESOURCES  ADD  CONSTRAINT FK_SBI_KPI_MODEL_RESOURCES_2 FOREIGN KEY ( RESOURCE_ID ) REFERENCES  SBI_RESOURCES  ( RESOURCE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_RESOURCES  ADD  CONSTRAINT FK_SBI_RESOURCES_1 FOREIGN KEY ( RESOURCE_TYPE_ID ) REFERENCES  SBI_DOMAINS  ( VALUE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_VALUE  ADD  CONSTRAINT FK_SBI_KPI_VALUE_1 FOREIGN KEY ( RESOURCE_ID ) REFERENCES  SBI_RESOURCES  ( RESOURCE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_VALUE  ADD  CONSTRAINT FK_SBI_KPI_VALUE_2 FOREIGN KEY ( ID_KPI_INSTANCE ) REFERENCES  SBI_KPI_INSTANCE  ( ID_KPI_INSTANCE ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI_INST_PERIOD  ADD  CONSTRAINT FK_SBI_KPI_INST_PERIOD_1 FOREIGN KEY ( PERIODICITY_ID ) REFERENCES  SBI_KPI_PERIODICITY  ( ID_KPI_PERIODICITY ) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE  SBI_KPI_INST_PERIOD  ADD  CONSTRAINT FK_SBI_KPI_INST_PERIOD_2 FOREIGN KEY  ( KPI_INSTANCE_ID ) REFERENCES  SBI_KPI_INSTANCE  ( ID_KPI_INSTANCE ) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- ALARM
ALTER TABLE  SBI_ALARM  ADD CONSTRAINT FK_SBI_ALARM_1 FOREIGN KEY ( MODALITY_ID ) REFERENCES  SBI_DOMAINS  ( VALUE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_ALARM  ADD CONSTRAINT FK_SBI_ALARM_2 FOREIGN KEY ( DOCUMENT_ID ) REFERENCES  SBI_OBJECTS  ( BIOBJ_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_ALARM  ADD CONSTRAINT FK_SBI_ALARM_3 FOREIGN KEY ( ID_KPI_INSTANCE ) REFERENCES  SBI_KPI_INSTANCE  ( ID_KPI_INSTANCE ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_ALARM  ADD CONSTRAINT FK_SBI_ALARM_4 FOREIGN KEY ( ID_THRESHOLD_VALUE ) REFERENCES  SBI_THRESHOLD_VALUE  ( ID_THRESHOLD_VALUE ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_ALARM_EVENT  ADD CONSTRAINT FK_SBI_ALARM_EVENT_1 FOREIGN KEY ( ALARM_ID ) REFERENCES  SBI_ALARM  ( ALARM_ID ) ON DELETE  CASCADE ON UPDATE  CASCADE;
ALTER TABLE  SBI_ALARM_DISTRIBUTION  ADD CONSTRAINT FK_SBI_ALARM_DISTRIBUTION_1 FOREIGN KEY ( ALARM_ID ) REFERENCES  SBI_ALARM  ( ALARM_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_ALARM_DISTRIBUTION  ADD CONSTRAINT FK_SBI_ALARM_DISTRIBUTION_2 FOREIGN KEY ( ALARM_CONTACT_ID ) REFERENCES  SBI_ALARM_CONTACT  ( ALARM_CONTACT_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;

ALTER TABLE  SBI_KPI  ADD CONSTRAINT FK_SBI_KPI_4 FOREIGN KEY ( KPI_TYPE ) REFERENCES  SBI_DOMAINS  ( VALUE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI  ADD CONSTRAINT FK_SBI_KPI_5 FOREIGN KEY ( METRIC_SCALE_TYPE ) REFERENCES  SBI_DOMAINS  ( VALUE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_KPI  ADD CONSTRAINT FK_SBI_KPI_6 FOREIGN KEY ( MEASURE_TYPE ) REFERENCES  SBI_DOMAINS  ( VALUE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;

ALTER TABLE  SBI_EXPORTERS  ADD CONSTRAINT FK_SBI_EXPORTERS_1 FOREIGN KEY ( ENGINE_ID ) REFERENCES  SBI_ENGINES  ( ENGINE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE  SBI_EXPORTERS  ADD CONSTRAINT FK_SBI_EXPORTERS_2 FOREIGN KEY ( DOMAIN_ID ) REFERENCES  SBI_DOMAINS  ( VALUE_ID ) ON DELETE  RESTRICT ON UPDATE  RESTRICT;

ALTER TABLE SBI_CONFIG ADD CONSTRAINT FK_SBI_CONFIG_1 FOREIGN KEY  ( VALUE_TYPE_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_USER_ATTRIBUTES ADD CONSTRAINT FK_SBI_USER_ATTRIBUTES_1 FOREIGN KEY  (ID) REFERENCES SBI_USER (ID) ON DELETE CASCADE ON UPDATE  RESTRICT;
ALTER TABLE SBI_EXT_USER_ROLES ADD CONSTRAINT FK_SBI_EXT_USER_ROLES_1 FOREIGN KEY  (ID) REFERENCES SBI_USER (ID) ON DELETE CASCADE ON UPDATE  RESTRICT;
ALTER TABLE SBI_USER_ATTRIBUTES ADD CONSTRAINT FK_SBI_USER_ATTRIBUTES_2 FOREIGN KEY  (ATTRIBUTE_ID) REFERENCES SBI_ATTRIBUTE (ATTRIBUTE_ID) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
ALTER TABLE SBI_EXT_USER_ROLES ADD CONSTRAINT FK_SBI_EXT_USER_ROLES_2 FOREIGN KEY  (EXT_ROLE_ID) REFERENCES SBI_EXT_ROLES (EXT_ROLE_ID) ON DELETE  RESTRICT ON UPDATE  RESTRICT;
 
ALTER TABLE SBI_UDP ADD CONSTRAINT FK_SBI_SBI_UDP_1 FOREIGN KEY  ( TYPE_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_UDP ADD CONSTRAINT FK_SBI_SBI_UDP_2 FOREIGN KEY  ( FAMILY_ID ) REFERENCES SBI_DOMAINS ( VALUE_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_UDP_VALUE ADD CONSTRAINT FK_SBI_UDP_VALUE_2 FOREIGN KEY  ( UDP_ID ) REFERENCES SBI_UDP ( UDP_ID ) ON DELETE RESTRICT;

ALTER TABLE SBI_KPI_REL ADD CONSTRAINT FK_SBI_KPI_REL_3 FOREIGN KEY  ( KPI_FATHER_ID ) REFERENCES SBI_KPI ( KPI_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_KPI_REL ADD CONSTRAINT FK_SBI_KPI_REL_4 FOREIGN KEY  ( KPI_CHILD_ID ) REFERENCES SBI_KPI ( KPI_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_KPI_ERROR ADD CONSTRAINT FK_SBI_KPI_ERROR_MODEL_1 FOREIGN KEY  ( KPI_MODEL_INST_ID ) REFERENCES SBI_KPI_MODEL_INST ( KPI_MODEL_INST ) ON DELETE RESTRICT;

ALTER TABLE SBI_ORG_UNIT_NODES ADD CONSTRAINT FK_SBI_ORG_UNIT_NODES_1 FOREIGN KEY  ( OU_ID ) REFERENCES SBI_ORG_UNIT ( ID ) ON DELETE CASCADE;
ALTER TABLE SBI_ORG_UNIT_NODES ADD CONSTRAINT FK_SBI_ORG_UNIT_NODES_2 FOREIGN KEY  ( HIERARCHY_ID ) REFERENCES SBI_ORG_UNIT_HIERARCHIES ( ID ) ON DELETE CASCADE;
ALTER TABLE SBI_ORG_UNIT_NODES ADD CONSTRAINT FK_SBI_ORG_UNIT_NODES_3 FOREIGN KEY  ( PARENT_NODE_ID ) REFERENCES SBI_ORG_UNIT_NODES ( NODE_ID ) ON DELETE CASCADE;
ALTER TABLE SBI_ORG_UNIT_GRANT ADD CONSTRAINT FK_SBI_ORG_UNIT_GRANT_2 FOREIGN KEY  ( HIERARCHY_ID ) REFERENCES SBI_ORG_UNIT_HIERARCHIES ( ID ) ON DELETE CASCADE;
ALTER TABLE SBI_ORG_UNIT_GRANT ADD CONSTRAINT FK_SBI_ORG_UNIT_GRANT_3 FOREIGN KEY  ( KPI_MODEL_INST_NODE_ID ) REFERENCES SBI_KPI_MODEL_INST ( KPI_MODEL_INST ) ON DELETE CASCADE;
ALTER TABLE SBI_ORG_UNIT_GRANT_NODES ADD CONSTRAINT FK_SBI_ORG_UNIT_GRANT_NODES_1 FOREIGN KEY  ( NODE_ID ) REFERENCES SBI_ORG_UNIT_NODES ( NODE_ID ) ON DELETE CASCADE;
ALTER TABLE SBI_ORG_UNIT_GRANT_NODES ADD CONSTRAINT FK_SBI_ORG_UNIT_GRANT_NODES_2 FOREIGN KEY  ( KPI_MODEL_INST_NODE_ID ) REFERENCES SBI_KPI_MODEL_INST ( KPI_MODEL_INST ) ON DELETE CASCADE;
ALTER TABLE SBI_ORG_UNIT_GRANT_NODES ADD CONSTRAINT FK_SBI_ORG_UNIT_GRANT_NODES_3 FOREIGN KEY  ( GRANT_ID ) REFERENCES SBI_ORG_UNIT_GRANT ( ID ) ON DELETE CASCADE;

ALTER TABLE SBI_KPI_VALUE ADD CONSTRAINT  FK_SBI_KPI_VALUE_4  FOREIGN KEY    ( HIERARCHY_ID ) REFERENCES  SBI_ORG_UNIT_HIERARCHIES  ( ID ) ON DELETE RESTRICT  ON UPDATE RESTRICT;
    
ALTER TABLE SBI_GOAL ADD CONSTRAINT FK_GRANT_ID_GRANT  FOREIGN KEY  (GRANT_ID) REFERENCES SBI_ORG_UNIT_GRANT (ID) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE SBI_GOAL_HIERARCHY ADD CONSTRAINT FK_SBI_GOAL_HIERARCHY_GOAL  FOREIGN KEY  (GOAL_ID) REFERENCES SBI_GOAL (GOAL_ID) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE SBI_GOAL_HIERARCHY ADD CONSTRAINT FK_SBI_GOAL_HIERARCHY_PARENT  FOREIGN KEY  (PARENT_ID) REFERENCES SBI_GOAL_HIERARCHY (GOAL_HIERARCHY_ID) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE SBI_GOAL_KPI ADD CONSTRAINT FK_SBI_GOAL_KPI_GOAL  FOREIGN KEY  (GOAL_HIERARCHY_ID) REFERENCES SBI_GOAL_HIERARCHY (GOAL_HIERARCHY_ID)  ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE SBI_GOAL_KPI ADD CONSTRAINT FK_SBI_GOAL_KPI_KPI  FOREIGN KEY  (KPI_INSTANCE_ID) REFERENCES SBI_KPI_MODEL_INST (KPI_MODEL_INST) ON DELETE CASCADE ON UPDATE RESTRICT;

ALTER TABLE SBI_OBJ_PARVIEW ADD CONSTRAINT FK_SBI_OBJ_PARVIEW_1 FOREIGN KEY ( OBJ_PAR_ID ) REFERENCES SBI_OBJ_PAR ( OBJ_PAR_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_OBJ_PARVIEW ADD CONSTRAINT FK_SBI_OBJ_PARVIEW_2 FOREIGN KEY ( OBJ_PAR_FATHER_ID ) REFERENCES SBI_OBJ_PAR ( OBJ_PAR_ID ) ON DELETE RESTRICT;
ALTER TABLE SBI_I18N_MESSAGES ADD CONSTRAINT FK_SBI_I18N_MESSAGES FOREIGN KEY (LANGUAGE_CD) REFERENCES SBI_DOMAINS(VALUE_ID) ON DELETE RESTRICT;
ALTER TABLE SBI_KPI_COMMENTS ADD CONSTRAINT FK_SBI_KPI_COMMENT_1 FOREIGN KEY  ( BIN_ID ) REFERENCES SBI_BINARY_CONTENTS(BIN_ID);
ALTER TABLE SBI_KPI_COMMENTS ADD CONSTRAINT FK_SBI_KPI_COMMENT_2 FOREIGN KEY  ( KPI_INST_ID ) REFERENCES SBI_KPI_INSTANCE (ID_KPI_INSTANCE);

ALTER TABLE SBI_META_MODELS_VERSIONS ADD CONSTRAINT FK_SBI_META_MODELS_VERSIONS_1 FOREIGN KEY ( MODEL_ID ) REFERENCES SBI_META_MODELS( ID ) ON DELETE CASCADE;
ALTER TABLE SBI_ARTIFACTS_VERSIONS ADD CONSTRAINT FK_SBI_ARTIFACTS_VERSIONS_1 FOREIGN KEY ( ARTIFACT_ID ) REFERENCES SBI_ARTIFACTS( ID ) ON DELETE CASCADE;

--ALTER TABLE SBI_META_MODELS ADD CONSTRAINT FK_SBIDATA_SOURCE 	  	FOREIGN KEY ( DATA_SOURCE_ID ) REFERENCES SBI_DATA_SOURCE( DS_ID );
ALTER TABLE SBI_DATA_SET 	ADD CONSTRAINT FK_DATA_SET_CATEGORY   	FOREIGN KEY (CATEGORY_ID) 	   REFERENCES SBI_DOMAINS(VALUE_ID);
ALTER TABLE SBI_DATA_SET 	ADD CONSTRAINT FK_SBI_DOMAINS_2   	FOREIGN KEY  ( SCOPE_ID ) 	   REFERENCES SBI_DOMAINS( VALUE_ID );

ALTER TABLE SBI_META_MODELS ADD CONSTRAINT FK_META_MODELS_CATEGORY 	FOREIGN KEY (CATEGORY_ID) 	   REFERENCES SBI_DOMAINS(VALUE_ID);
ALTER TABLE SBI_AUTHORIZATIONS_ROLES ADD CONSTRAINT FK_ROLE1 FOREIGN KEY (ROLE_ID) REFERENCES SBI_EXT_ROLES (EXT_ROLE_ID);
ALTER TABLE SBI_AUTHORIZATIONS_ROLES ADD  CONSTRAINT FK_AUTHORIZATION_1 FOREIGN KEY (AUTHORIZATION_ID) REFERENCES SBI_AUTHORIZATIONS (ID);

ALTER TABLE SBI_ORGANIZATION_ENGINE ADD CONSTRAINT FK_ENGINE_1 FOREIGN KEY (ENGINE_ID) REFERENCES SBI_ENGINES (ENGINE_ID) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE SBI_ORGANIZATION_ENGINE ADD CONSTRAINT FK_ORGANIZATION_1 FOREIGN KEY (ORGANIZATION_ID) REFERENCES SBI_ORGANIZATIONS (ID) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE SBI_ORGANIZATION_DATASOURCE ADD CONSTRAINT FK_ORGANIZATION_2 FOREIGN KEY (ORGANIZATION_ID) REFERENCES SBI_ORGANIZATIONS (ID) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE SBI_ORGANIZATION_DATASOURCE ADD CONSTRAINT FK_DATASOURCE_2 FOREIGN KEY (DATASOURCE_ID) REFERENCES SBI_DATA_SOURCE (DS_ID) ON DELETE CASCADE;