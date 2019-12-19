  CREATE TABLE "TEMAS" 
   (	"COD_TEMA" NUMBER, 
	"TEMA" VARCHAR2(255 BYTE), 
	"IMAGEN" VARCHAR2(30 BYTE), 
	"IMAGEN2" VARCHAR2(80 BYTE), 
	"ORDEN" NUMBER(2,0), 
	"TEMAEN" VARCHAR2(255 BYTE), 
	"TEMAFR" VARCHAR2(255 BYTE), 
	"TEMAIT" VARCHAR2(255 BYTE), 
	"COD_TEMAAJ" NUMBER
   ) ;
   
--------------------------------------------------------
--  DDL for Trigger CODTEMA_AUTOINC
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "CODTEMA_AUTOINC" BEFORE
INSERT ON "TEMAS" FOR EACH ROW
BEGIN
   SELECT seq_codtema.NEXTVAL
     INTO :NEW.cod_tema
     FROM DUAL;
END;
/
ALTER TRIGGER "CODTEMA_AUTOINC" ENABLE;

  ALTER TABLE "TEMAS" MODIFY ("COD_TEMA" NOT NULL ENABLE);
 
  ALTER TABLE "TEMAS" ADD CONSTRAINT "pk_TEMAS" PRIMARY KEY ("COD_TEMA");


/* TEMAS */
Insert into TEMAS (COD_TEMA,TEMA,IMAGEN,ORDEN,TEMAEN,TEMAFR,TEMAIT,COD_TEMAAJ) values (1,'Música','imagen/musica.gif',12,'Music','Musique',null,12);
Insert into TEMAS (COD_TEMA,TEMA,IMAGEN,ORDEN,TEMAEN,TEMAFR,TEMAIT,COD_TEMAAJ) values (2,'Formación','imagen/cursos-talleres.gif',6,'Cours et ateliers','Courses and workshops',null,7);
Insert into TEMAS (COD_TEMA,TEMA,IMAGEN,ORDEN,TEMAEN,TEMAFR,TEMAIT,COD_TEMAAJ) values (3,'Teatro y Artes Escénicas','imagen/teatro.gif',13,'Theatre','Théâtre',null,3);
Insert into TEMAS (COD_TEMA,TEMA,IMAGEN,ORDEN,TEMAEN,TEMAFR,TEMAIT,COD_TEMAAJ) values (4,'Conferencias y Congresos','act_congresos_gr.jpg',5,'Conferences and seminars','Congrès et journées',null,6);

  
   CREATE TABLE "ACTO_VALORES" 
   (	
   	"COD_VALOR" NUMBER, 
	"TITLE" VARCHAR2(100),
	"DESCRIPTION" VARCHAR2(300), 
	CONSTRAINT "ACTO_VALOR_PK" PRIMARY KEY ("COD_VALOR")
   );
   
   /* VALORES */
Insert into ACTO_VALORES (COD_VALOR,TITLE,DESCRIPTION) values (SEQ_ACTO_VALORES.NEXTVAL,'Accesible y adaptada','Para tod@s');
Insert into ACTO_VALORES (COD_VALOR,TITLE,DESCRIPTION) values (SEQ_ACTO_VALORES.NEXTVAL,'Comprometida','Especialmente con temas medioambientales y de ética social');
Insert into ACTO_VALORES (COD_VALOR,TITLE,DESCRIPTION) values (SEQ_ACTO_VALORES.NEXTVAL,'Creativa','Sorprende por contenido y metodología que promueven la creatividad');
Insert into ACTO_VALORES (COD_VALOR,TITLE,DESCRIPTION) values (SEQ_ACTO_VALORES.NEXTVAL,'Divertida','Para pasarlo bien');
Insert into ACTO_VALORES (COD_VALOR,TITLE,DESCRIPTION) values (SEQ_ACTO_VALORES.NEXTVAL,'Formativa','Con objetivos educativos que te permitirán manejar nuevas herramientas y adquirir conocimientos');
Insert into ACTO_VALORES (COD_VALOR,TITLE,DESCRIPTION) values (SEQ_ACTO_VALORES.NEXTVAL,'Igualdad','Promueve valores de igualdad de género');
Insert into ACTO_VALORES (COD_VALOR,TITLE,DESCRIPTION) values (SEQ_ACTO_VALORES.NEXTVAL,'Innovadora','Ofrece lo último, lo más actual');
Insert into ACTO_VALORES (COD_VALOR,TITLE,DESCRIPTION) values (SEQ_ACTO_VALORES.NEXTVAL,'Participativa','Puedes intervenir y decidir sobre la organización y el desarrollo de la actividad');
Insert into ACTO_VALORES (COD_VALOR,TITLE,DESCRIPTION) values (SEQ_ACTO_VALORES.NEXTVAL,'Pensamiento crítico','Fomenta la reflexión y la expresión personal de ideas, en un ambiente de libertad y respeto');
Insert into ACTO_VALORES (COD_VALOR,TITLE,DESCRIPTION) values (SEQ_ACTO_VALORES.NEXTVAL,'Respeto','Promueve la aceptación de las diferencias como enriquecimiento personal');
Insert into ACTO_VALORES (COD_VALOR,TITLE,DESCRIPTION) values (SEQ_ACTO_VALORES.NEXTVAL,'Sociabilidad','Favorece el conocer a otr@s jóvenes en un ambiente de confianza y sano');



--------------------------------------------------------
--  DDL for Table USERS_TEMA
--------------------------------------------------------

  CREATE TABLE "USERS_TEMA" 
   (
    "ID_USUARIO_ADENTRA" NUMBER  NOT NULL ENABLE, 
	"COD_TEMA" NUMBER  NOT NULL ENABLE,
	CONSTRAINT "USERS_TEMA_ID_FK1" FOREIGN KEY ("ID_USUARIO_ADENTRA")
  REFERENCES "USERS" ("ID") ENABLE,
  CONSTRAINT "USERS_TEMA_COD_TEMA_FK" FOREIGN KEY ("COD_TEMA")
  REFERENCES "TEMAS" ("COD_TEMA") ENABLE
   ) ;
   
--------------------------------------------------------
--  DDL for Sequence SEQ_USERS_TEMA
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_USERS_TEMA"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE ;
   
--------------------------------------------------------
--  DDL for Table USERS_VALOR
--------------------------------------------------------

  CREATE TABLE "USERS_VALOR" 
   (
	"ID_USUARIO_ADENTRA" NUMBER NOT NULL ENABLE, 
	"COD_VALOR" NUMBER NOT NULL ENABLE, 
	 CONSTRAINT "USERS_VALOR_ID_FK1" FOREIGN KEY ("ID_USUARIO_ADENTRA")
	  REFERENCES "USERS" ("ID") ENABLE, 
	 CONSTRAINT "USERS_VALOR_COD_VALOR_FK" FOREIGN KEY ("COD_VALOR")
	  REFERENCES "ACTO_VALORES" ("COD_VALOR") ENABLE
   );

--------------------------------------------------------
--  DDL for Table PERFILES_USUARIOS
--------------------------------------------------------

  CREATE TABLE "PERFILES_USUARIOS" 
   (
	"ID_USUARIO_ADENTRA" NUMBER(*, 0) NOT NULL ENABLE,        
 	"VISIBLE" CHAR(1 BYTE)  DEFAULT 'S',
   	"GENERO" CHAR(1 BYTE) DEFAULT 'F',
    "PAIS" VARCHAR2(100 BYTE),
    "ESTUDIOS" VARCHAR2(150 BYTE),
    "ESTUDIOS_ACTUALES" VARCHAR2(150 BYTE),
    "OCUPACION" VARCHAR2(150 BYTE),
    "FORMATO" VARCHAR2(150 BYTE),
 	"INDEPENDIENTE" CHAR(1 BYTE),
	"NECESIDADES_ESPECIALES" VARCHAR2(2000 BYTE),
	"telefonocontacto" varchar2(40),
	"FECHA_NACIMIENTO" DATE,
   	CONSTRAINT "PERFILES_USUARIO_PK" PRIMARY KEY ("ID_USUARIO_ADENTRA"),
   	CONSTRAINT "PERFILES_USUARIOS_ID_FK1" FOREIGN KEY ("ID_USUARIO_ADENTRA")
  REFERENCES "USERS" ("ID") ENABLE
   ) ;