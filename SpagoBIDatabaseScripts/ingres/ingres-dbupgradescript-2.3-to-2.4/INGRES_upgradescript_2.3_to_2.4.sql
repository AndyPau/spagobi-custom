/* SQL script  version II 9.3.0                                   */
/* It migrates SpagoBI repository from release 2.3 to release 2.4 */
/* Launch this script by client tools(ie. SQUIRREL, Visual DBA,..)*/
/*----------------------------------------------------------------*/

/* Modifies for add menagement of notes linked to user */
ALTER TABLE SBI_OBJECT_NOTES ADD COLUMN OWNER Varchar(50);
ALTER TABLE SBI_OBJECT_NOTES ADD COLUMN ISPUBLIC TINYINT;
ALTER TABLE SBI_OBJECT_NOTES ADD COLUMN CREATION_DATE TIMESTAMP NOT NULL WITH DEFAULT;
ALTER TABLE SBI_OBJECT_NOTES ADD COLUMN LAST_CHANGE_DATE TIMESTAMP NOT NULL WITH DEFAULT;
ALTER TABLE SBI_SUBOBJECTS MODIFY COLUMN DESCRIPTION VARCHAR(1000) DEFAULT NULL;
ALTER TABLE SBI_DATA_SET ADD COLUMN DS_METADATA VARCHAR(2000) NULL WITH DEFAULT;
MODIFY SBI_DATA_SET RECONSTRUCT;
ALTER TABLE SBI_DATA_SET ALTER DS_METADATA VARCHAR(2000) NULL WITH DEFAULT NULL;
UPDATE SBI_DATA_SET SET DS_METADATA = NULL;

/* force a valid value for date fields in existing records: */
UPDATE SBI_OBJECT_NOTES SET LAST_CHANGE_DATE = CURRENT_TIMESTAMP,CREATION_DATE = CURRENT_TIMESTAMP;

/* force a valid value for owner field in existing records: 
***************************** ATTENTION **********************************
* The OWNER value depends from your context... 
we suggest 'biadmin' because is the classic admin user in SpaogoBI demo: 
you should change this value with a valid user in your platfrom, in this way 
he may change or delete the EXISTING notes!!*/
UPDATE SBI_OBJECT_NOTES SET OWNER = 'biadmin';
/*************************************************************************/
COMMIT;

/* Modifies for add possibility to update subobjects. It's necessary delete all subobjects where 
name is null because we add not null constraint to name column. */
DELETE FROM SBI_REMEMBER_ME WHERE SUBOBJ_ID IN (SELECT SUBOBJ_ID FROM SBI_SUBOBJECTS WHERE NAME IS NULL OR NAME ='');
DELETE FROM SBI_AUDIT WHERE SUBOBJ_ID IN (SELECT SUBOBJ_ID FROM SBI_SUBOBJECTS WHERE NAME IS NULL OR NAME ='');
DELETE FROM SBI_MENU WHERE SUBOBJ_NAME IN (SELECT NAME FROM SBI_SUBOBJECTS WHERE NAME IS NULL OR NAME ='');
DELETE FROM SBI_BINARY_CONTENTS WHERE BIN_ID IN (SELECT BIN_ID FROM SBI_SUBOBJECTS WHERE NAME IS NULL OR NAME ='');
DELETE FROM SBI_SUBOBJECTS WHERE NAME IS NULL OR NAME ='';
COMMIT;

CREATE TABLE SBI_SUBOBJECTS_TMP AS SELECT * FROM SBI_SUBOBJECTS;
DROP TABLE SBI_SUBOBJECTS;

Create table SBI_SUBOBJECTS (
	SUBOBJ_ID Integer NOT NULL with default next value for SBI_SUBOBJECTS_SEQ,
	BIOBJ_ID Integer NOT NULL,
	BIN_ID Integer,
	NAME Varchar(50) NOT NULL,
	DESCRIPTION Varchar(100),
	OWNER Varchar(50),
	ISPUBLIC TINYINT,
	CREATION_DATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	LAST_CHANGE_DATE TIMESTAMP NOT NULL Default CURRENT_TIMESTAMP,
Primary Key (SUBOBJ_ID)
);
alter table SBI_SUBOBJECTS ADD CONSTRAINT FK_SBI_SUBOBJECTS_1 foreign key (BIN_ID) references SBI_BINARY_CONTENTS (BIN_ID);
alter table SBI_SUBOBJECTS ADD CONSTRAINT FK_SBI_SUBOBJECTS_2 foreign key (BIOBJ_ID) references SBI_OBJECTS (BIOBJ_ID);
insert into SBI_SUBOBJECTS (subobj_id,biobj_id, bin_id,name,description,owner,ispublic,creation_date,last_change_date)  select subobj_id,biobj_id, bin_id,name,description,owner,ispublic,creation_date,last_change_date from SBI_SUBOBJECTS_TMP;
DROP TABLE SBI_SUBOBJECTS_TMP;

/* Modified for metadata management */
ALTER TABLE SBI_OBJECTS DROP COLUMN DESCR_EXT RESTRICT;
ALTER TABLE SBI_OBJECTS DROP COLUMN OBJECTIVE RESTRICT;
ALTER TABLE SBI_OBJECTS DROP COLUMN LANGUAGE RESTRICT;
ALTER TABLE SBI_OBJECTS DROP COLUMN KEYWORDS RESTRICT;

/* adds column and defines system default  */
ALTER TABLE SBI_EXT_ROLES ADD COLUMN SAVE_METADATA TINYINT not null with Default;
/* activates the default on the table */
MODIFY SBI_EXT_ROLES RECONSTRUCT;
/* sets the default for the column (for the new records) */
ALTER TABLE SBI_EXT_ROLES ALTER SAVE_METADATA TINYINT not null with Default  1; 
/* sets the default on the records already present */
UPDATE  SBI_EXT_ROLES SET SAVE_METADATA = 1; 

CREATE SEQUENCE SBI_OBJ_METADATA_SEQ;
CREATE TABLE SBI_OBJ_METADATA (
	OBJ_META_ID 		INTEGER NOT NULL with default next value for SBI_OBJ_METADATA_SEQ,
    LABEL	 	        VARCHAR(20) NOT NULL,
    NAME 	            VARCHAR(40) NOT NULL,
    DESCRIPTION	        VARCHAR(100),  
    DATA_TYPE_ID	    INTEGER NOT NULL,
    CREATION_DATE 	    TIMESTAMP NOT NULL,    
    UNIQUE(LABEL),	
	PRIMARY KEY (OBJ_META_ID)
);

ALTER TABLE SBI_OBJ_METADATA ADD CONSTRAINT FK_SBI_OBJ_METADATA_1 FOREIGN KEY ( DATA_TYPE_ID ) REFERENCES SBI_DOMAINS(VALUE_ID);

CREATE SEQUENCE SBI_OBJ_METACONTENTS_SEQ;
CREATE TABLE SBI_OBJ_METACONTENTS (
  OBJ_METACONTENT_ID INTEGER  NOT NULL with default next value for SBI_OBJ_METACONTENTS_SEQ,
  OBJMETA_ID 		 INTEGER  NOT NULL ,
  BIOBJ_ID 			 INTEGER  NOT NULL,
  SUBOBJ_ID 		 INTEGER,
  BIN_ID 			 INTEGER,
  CREATION_DATE 	 TIMESTAMP NOT NULL,   
  LAST_CHANGE_DATE   TIMESTAMP NOT NULL,   
    PRIMARY KEY (OBJ_METACONTENT_ID)
);

CREATE UNIQUE INDEX XAK1SBI_OBJ_METACONTENTS ON SBI_OBJ_METACONTENTS
(
        OBJMETA_ID,
        BIOBJ_ID,
        SUBOBJ_ID
);

ALTER TABLE SBI_OBJ_METACONTENTS ADD CONSTRAINT FK_SBI_OBJ_METACONTENTS_1 FOREIGN KEY ( OBJMETA_ID ) REFERENCES SBI_OBJ_METADATA (  OBJ_META_ID );
ALTER TABLE SBI_OBJ_METACONTENTS ADD CONSTRAINT FK_SBI_OBJ_METACONTENTS_2 FOREIGN KEY ( BIOBJ_ID ) REFERENCES SBI_OBJECTS (  BIOBJ_ID );
ALTER TABLE SBI_OBJ_METACONTENTS ADD CONSTRAINT FK_SBI_OBJ_METACONTENTS_3 FOREIGN KEY ( SUBOBJ_ID ) REFERENCES SBI_SUBOBJECTS (  SUBOBJ_ID ) ;
ALTER TABLE SBI_OBJ_METACONTENTS ADD CONSTRAINT FK_SBI_OBJ_METACONTENTS_4 FOREIGN KEY ( BIN_ID ) REFERENCES SBI_BINARY_CONTENTS(BIN_ID);

--adds new funcionality for metadata management
INSERT INTO SBI_USER_FUNC (NAME, DESCRIPTION) VALUES ('ObjMetadataManagement', 'ObjMetadataManagement');
INSERT INTO SBI_ROLE_TYPE_USER_FUNC (ROLE_TYPE_ID, USER_FUNCT_ID)
 SELECT a.VALUE_ID  , b.USER_FUNCT_ID FROM
 (
  SELECT VALUE_ID FROM SBI_DOMAINS WHERE DOMAIN_CD = 'ROLE_TYPE' AND VALUE_CD = 'ADMIN' 
 ) a( VALUE_ID ) ,
 (
  SELECT USER_FUNCT_ID FROM SBI_USER_FUNC WHERE NAME='ObjMetadataManagement'
 ) b(USER_FUNCT_ID );
COMMIT;
