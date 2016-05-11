CREATE MEMORY TABLE SBI_GOAL (GOAL_ID INTEGER NOT NULL,HIERARCHY_ID INTEGER NOT NULL,GRANT_ID INTEGER NOT NULL, START_DATE DATE,END_DATE DATE,NAME VARCHAR(20),	DESCRIPTION VARCHAR(20),PRIMARY KEY (GOAL_ID));
CREATE MEMORY TABLE SBI_GOAL_HIERARCHY (GOAL_HIERARCHY_ID INTEGER NOT NULL, ORG_UNIT_ID INTEGER NOT NULL, GOAL_ID INTEGER NOT NULL, PARENT_ID INTEGER, NAME VARCHAR(50), LABEL VARCHAR(50),GOAL VARCHAR(1000), PRIMARY KEY (GOAL_HIERARCHY_ID));
CREATE MEMORY TABLE SBI_GOAL_KPI ( GOAL_KPI_ID INTEGER NOT NULL, KPI_INSTANCE_ID INTEGER NOT NULL, GOAL_HIERARCHY_ID INTEGER NOT NULL, WEIGHT1 DOUBLE , WEIGHT2 DOUBLE , THRESHOLD1 DOUBLE, THRESHOLD2 DOUBLE, THRESHOLD1SIGN INTEGER, THRESHOLD2SIGN INTEGER,  PRIMARY KEY (GOAL_KPI_ID));

ALTER TABLE SBI_GOAL ADD CONSTRAINT FK_GRANT_ID_GRANT FOREIGN KEY ( GRANT_ID ) REFERENCES SBI_ORG_UNIT_HIERARCHIES ( ID ) ON DELETE CASCADE;   
ALTER TABLE SBI_GOAL_HIERARCHY ADD CONSTRAINT FK_SBI_GOAL_HIERARCHY_GOAL  FOREIGN KEY (GOAL_ID) REFERENCES SBI_GOAL (GOAL_ID) ON DELETE CASCADE;
ALTER TABLE SBI_GOAL_HIERARCHY ADD CONSTRAINT FK_SBI_GOAL_HIERARCHY_PARENT  FOREIGN KEY (PARENT_ID) REFERENCES SBI_GOAL_HIERARCHY (GOAL_HIERARCHY_ID) ON DELETE CASCADE ;
ALTER TABLE SBI_GOAL_KPI ADD CONSTRAINT FK_SBI_GOAL_KPI_GOAL  FOREIGN KEY (GOAL_HIERARCHY_ID) REFERENCES SBI_GOAL_HIERARCHY (GOAL_HIERARCHY_ID)  ON DELETE CASCADE;
ALTER TABLE SBI_GOAL_KPI ADD CONSTRAINT FK_SBI_GOAL_KPI_KPI  FOREIGN KEY (KPI_INSTANCE_ID) REFERENCES SBI_KPI_MODEL_INST (KPI_MODEL_INST) ON DELETE CASCADE;
