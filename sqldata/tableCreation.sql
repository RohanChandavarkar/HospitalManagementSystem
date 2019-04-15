DROP TABLE IF EXISTS `Patient`; 
CREATE TABLE `Patient` (
	 `pId` int(11) NOT NULL,
	 `pName` varchar(128) NOT NULL,
	 `SSN` varchar(12) DEFAULT NULL,
	 `DOB` date NOT NULL,
	 `pGender` varchar(1) NOT NULL,
	 `pPhone` int(10) DEFAULT NULL,
	 `pAddress` varchar(256) NOT NULL,
	 PRIMARY KEY (`pId`)
	);


INSERT INTO `Patient` values (1001,'David','000011234','1980-01-30','Male',9191233324,'69 ABC St, Raleigh NC 27730');
INSERT INTO `Patient` values (1002,'Sarah','000021234','1971-01-30','Female',9195633478,'81 DEF St, Cary NC 27519');
INSERT INTO `Patient` values (1003,'Joseph','000031234','1987-01-30','Male',9199572199,'31 OPG St, Cary NC 27519');
INSERT INTO `Patient` values (1004,'Lucy','000041234','1985-01-30','Female',9198387123,'10 TBC St, Raleigh NC 27730');



DROP TABLE IF EXISTS `Staff`; 
CREATE TABLE `Staff` (
	 `sId` int(11) NOT NULL,
	 `sName` varchar(128) NOT NULL,
	 `sGender` varchar(1) NOT NULL,
	 `jobTitle` varchar(24) NOT NULL,
	 `professionalTitle` varchar(24) DEFAULT NULL,
	 `pPhone` int(10) DEFAULT NULL,
	 `pAddress` varchar(256) NOT NULL,
	 `consultationFee` int(10) DEFAULT 0,
	 PRIMARY KEY (`sId`)
	);


INSERT INTO `Staff` VALUES (100,'Mary','Female','Doctor','senior',654,'90 ABC St , Raleigh NC 27',300);
INSERT INTO `Staff` VALUES (101,'John','Male','Billing Staff',NULL,564,'798 XYZ St , Rochester NY 54',0);
INSERT INTO `Staff` VALUES (102,'Carol','Female','Nurse',NULL,911,'351 MH St , Greensboro NC 27',0);
INSERT INTO `Staff` VALUES (103,'Emma','Female','Doctor','Senior Surgeon',546,'49 ABC St , Raleigh NC 27',300);
INSERT INTO `Staff` VALUES (104,'Eva','Female','Front Desk Staff',NULL,777,'425 RG St , Raleigh NC 27',0);
INSERT INTO `Staff` VALUES (105,'Peter','Male','Doctor','Anesthetist',724,'475 RG St , Raleigh NC 27',300);
INSERT INTO `Staff` VALUES (106,'Olivia','Female','Nurse',NULL,799,'325 PD St , Raleigh NC 27',0);



DROP TABLE IF EXISTS `Ward`; 
CREATE TABLE `Ward` (
	 `wNumber` int(11) NOT NULL,
	 `type` int(11) NOT NULL,
	 `avail` int(11) NOT NULL,
	 `wcost` int(11) NOT NULL,
	 PRIMARY KEY (`wNumber`)
	);

INSERT INTO `Ward` VALUES (001,4,2,50);
INSERT INTO `Ward` VALUES (002,4,3,50);
INSERT INTO `Ward` VALUES (003,2,2,100);
INSERT INTO `Ward` VALUES (004,2,2,100);

DROP TABLE IF EXISTS `Bed`; 
CREATE TABLE `Bed` (
	 `wNumber` int(11) NOT NULL,
	 `bNumber` int(11) NOT NULL,
	 PRIMARY KEY (`wNumber`, `bNumber`),
	 KEY `ward_in_hospital`(`wNumber`),
	 CONSTRAINT `ward_in_hospital` FOREIGN KEY (`wNumber`) REFERENCES `Ward` (`wNumber`) ON DELETE CASCADE ON UPDATE CASCADE
	);



DROP TABLE IF EXISTS `MedicalRecord`; 
CREATE TABLE `MedicalRecord` (
	`mId` int(11) NOT NULL,
	`diagnosis` varchar(64) NOT NULL, 
	`startdate` date NOT NULL,
	`enddate` date DEFAULT NULL,
	`regFee` int DEFAULT 0, 
  	`proTreatPlan` int DEFAULT 1,	
	`inWard` char(1) DEFAULT 'Y',
	`compTreatment` char(1) DEFAULT 'N',	
	PRIMARY KEY (`mId`)
	);


INSERT INTO `MedicalRecord` values (1,'Hospitalization','2019-03-01',NULL,100,20,'Y','N');
INSERT INTO `MedicalRecord` values (2,'Hospitalization','2019-03-10',NULL,100,20,'Y','N');
INSERT INTO `MedicalRecord` values (3,'Hospitalization','2019-03-05',NULL,100,10,'Y','N');
INSERT INTO `MedicalRecord` values (4,'Surgeon,Hospitalization','2019-03-17','2019-03-21',100,5,'N','Y');


DROP TABLE IF EXISTS `Department`; 
CREATE TABLE `Department` (
	 `deptId` int(11) NOT NULL,
	 `deptName` varchar(32) NOT NULL, 
	 PRIMARY KEY (`deptId`)
	);

INSERT INTO `Department` VALUES (9001,'Neurology');
INSERT INTO `Department` VALUES (9002,'Office');
INSERT INTO `Department` VALUES (9003,'ER');
INSERT INTO `Department` VALUES (9004,'Oncological Surgery');

DROP TABLE IF EXISTS `Drugs`; 
CREATE TABLE `Drugs` (
	 `drugId` int(11) NOT NULL,
	 `drugName` varchar(32) NOT NULL,
	 `drugCost` int(11) DEFAULT 0, 
	 PRIMARY KEY (`drugId`)
	);
	
INSERT INTO `Drugs` VALUES (201,'nervine',300);
INSERT INTO `Drugs` VALUES (202,'analgesic',400);

DROP TABLE IF EXISTS `Tests`; 
CREATE TABLE `Tests` (
	 `tId` int(11) NOT NULL,
	 `tName` varchar(32) NOT NULL,
	 `tCost` int(11) NOT NULL, 
	 PRIMARY KEY (`tId`)
	);


DROP TABLE IF EXISTS `BillingAccount`; 
CREATE TABLE `BillingAccount` (
	`payerSSN` varchar(12) NOT NULL,
	`paymentMethod` varchar(10) NOT NULL,
	`accountNumber` int(15) DEFAULT NULL, 
	`routingNumber` int(10) DEFAULT NULL,
	`cardNumber` int(15) DEFAULT NULL, 
	`expiryMonth` int(2) DEFAULT NULL,
	`expiryYear` int(4) DEFAULT NULL,
	`address` varchar(30) DEFAULT 0, 
	PRIMARY KEY (`payerSSN`)
	);

INSERT INTO `BillingAccount` VALUES ('000011234','Credit Card',NULL,NULL,4044875409613234,07,23,'69 ABC St , Raleigh NC 27730');
INSERT INTO `BillingAccount` VALUES ('000021234','Credit Card',NULL,NULL,4401982398541143,11,22,'81 DEF St , Cary NC 27519');
INSERT INTO `BillingAccount` VALUES ('000031234','Check',NULL,NULL,NULL,NULL,NULL,'31 OPG St , Cary NC 27519');
INSERT INTO `BillingAccount` VALUES ('000041234','Credit Card',NULL,NULL,4044987612349123,12,24,'10 TBC St. Raleigh NC 27730');


DROP TABLE IF EXISTS `inChargeOf`; 
CREATE TABLE `inChargeOf` (
	 `sId` int(11) NOT NULL,
	 `wNumber` int(11) NOT NULL,
	 PRIMARY KEY (`sID`,`wNumber`),
	 KEY `wNumber_in_inChargeOf` (`wNumber`),
	 CONSTRAINT `sId_in_inChargeOf` FOREIGN KEY (`sId`) REFERENCES `Staff` (`sId`) ON DELETE CASCADE ON UPDATE CASCADE,
	 CONSTRAINT `wNumber_in_inChargeOf` FOREIGN KEY (`wNumber`) REFERENCES `Ward` (`wNumber`) ON DELETE CASCADE ON UPDATE CASCADE
	);

INSERT INTO `inChargeOf` VALUES (102,1);
INSERT INTO `inChargeOf` VALUES (102,2);
INSERT INTO `inChargeOf` VALUES (106,3);
INSERT INTO `inChargeOf` VALUES (106,4);


DROP TABLE IF EXISTS `assigns`; 
CREATE TABLE `assigns` (
	 `mId` int(11) NOT NULL,
	 `wNumber` int(11) NOT NULL,
	 PRIMARY KEY (`mId`,`wNumber`),
	 KEY `mId_in_assigns` (`mId`),
	 CONSTRAINT `mId_in_assigns` FOREIGN KEY (`mId`) REFERENCES `MedicalRecord` (`mId`) ON DELETE CASCADE ON UPDATE CASCADE,
	 CONSTRAINT `wNumber_in_assigns` FOREIGN KEY (`wNumber`) REFERENCES `Ward` (`wNumber`) ON DELETE CASCADE ON UPDATE CASCADE
	);


INSERT INTO `assigns` VALUES (1,1);
INSERT INTO `assigns` VALUES (3,1);
INSERT INTO `assigns` VALUES (2,2);


DROP TABLE IF EXISTS `consults`; 
CREATE TABLE `consults` (
	 `mId` int(11) NOT NULL,
	 `sId` int(11) NOT NULL,
	 PRIMARY KEY (`mId`,`sId`),
	 KEY `mId_in_consults` (`mId`),
	 CONSTRAINT `mId_in_consults` FOREIGN KEY (`mId`) REFERENCES `MedicalRecord` (`mId`) ON DELETE CASCADE ON UPDATE CASCADE,
	 CONSTRAINT `sId_in_consults` FOREIGN KEY (`sId`) REFERENCES `Staff` (`sId`) ON DELETE CASCADE ON UPDATE CASCADE
	);

INSERT INTO `consults` VALUES (1,100);
INSERT INTO `consults` VALUES (2,100);
INSERT INTO `consults` VALUES (3,100);
INSERT INTO `consults` VALUES (4,103);


DROP TABLE IF EXISTS `belongsTo`; 
CREATE TABLE `belongsTo` (
	 `sId` int(11) NOT NULL,
	 `deptId` int(11) NOT NULL,
	 PRIMARY KEY (`sID`,`deptId`),
	 KEY `sId_in_belongsTo` (`sId`),
	 CONSTRAINT `sId_in_belongsTo` FOREIGN KEY (`sId`) REFERENCES `Staff` (`sId`) ON DELETE CASCADE ON UPDATE CASCADE,
	 CONSTRAINT `deptId_in_belongsTo` FOREIGN KEY (`deptId`) REFERENCES `Department` (`deptId`) ON DELETE CASCADE ON UPDATE CASCADE
	);


INSERT INTO `belongsTo` VALUES (100,9001);
INSERT INTO `belongsTo` VALUES (101,9002);
INSERT INTO `belongsTo` VALUES (102,9003);
INSERT INTO `belongsTo` VALUES (103,9004);
INSERT INTO `belongsTo` VALUES (104,9002);
INSERT INTO `belongsTo` VALUES (105,9004);
INSERT INTO `belongsTo` VALUES (106,9001);

DROP TABLE IF EXISTS `doneBy`; 
CREATE TABLE `doneBy` (
	 `tId` int(11) NOT NULL,
	 `deptId` int(11) NOT NULL,
	 PRIMARY KEY (`tID`,`deptId`),
	 KEY `tID_in_doneBy` (`tId`),
	 CONSTRAINT `tID_in_doneBy` FOREIGN KEY (`tId`) REFERENCES `Tests` (`tId`) ON DELETE CASCADE ON UPDATE CASCADE,
	 CONSTRAINT `deptId_in_doneBy` FOREIGN KEY (`deptId`) REFERENCES `Department` (`deptId`) ON DELETE CASCADE ON UPDATE CASCADE
	);

DROP TABLE IF EXISTS `prescribesDrugs`; 
CREATE TABLE `prescribesDrugs` (
	 `mId` int(11) NOT NULL,
	 `drugId` int(11) NOT NULL,
	 PRIMARY KEY (`mId`,`drugId`),
	 KEY `mId_in_prescribesDrugs` (`mId`),
	 CONSTRAINT `mId_in_prescribesDrugs` FOREIGN KEY (`mId`) REFERENCES `MedicalRecord` (`mId`) ON DELETE CASCADE ON UPDATE CASCADE,
	 CONSTRAINT `drugId_in_prescribesDrugs` FOREIGN KEY (`drugId`) REFERENCES `Drugs` (`drugId`) ON DELETE CASCADE ON UPDATE CASCADE
	);

INSERT INTO `prescribesDrugs` VALUES (1,201);
INSERT INTO `prescribesDrugs` VALUES (2,201);
INSERT INTO `prescribesDrugs` VALUES (3,201);
INSERT INTO `prescribesDrugs` VALUES (4,202);


DROP TABLE IF EXISTS `prescribesTests`; 
CREATE TABLE `prescribesTests` (
	 `mId` int(11) NOT NULL,
	 `tId` int(11) NOT NULL,
	 PRIMARY KEY (`mId`,`tId`),
	 KEY `mId_in_prescribesTests` (`mId`),
	 CONSTRAINT `mId_in_prescribesTests` FOREIGN KEY (`mId`) REFERENCES `MedicalRecord` (`mId`) ON DELETE CASCADE ON UPDATE CASCADE,
	 CONSTRAINT `tId_in_prescribesTests` FOREIGN KEY (`tId`) REFERENCES `Tests` (`tId`) ON DELETE CASCADE ON UPDATE CASCADE
	);

DROP TABLE IF EXISTS `hasRecord`; 
CREATE TABLE `hasRecord` (
	 `mId` int(11) NOT NULL,
	 `pId` int(11) NOT NULL,
	 PRIMARY KEY (`mID`,`pId`),
	 KEY `mId_in_hasRecord` (`mId`),
	 CONSTRAINT `mId_in_hasRecord` FOREIGN KEY (`mId`) REFERENCES `MedicalRecord` (`mId`) ON DELETE CASCADE ON UPDATE CASCADE,
	 CONSTRAINT `pId_in_hasRecord` FOREIGN KEY (`pId`) REFERENCES `Patient` (`pId`) ON DELETE CASCADE ON UPDATE CASCADE
	);

INSERT INTO `hasRecord` VALUES (1,1001);
INSERT INTO `hasRecord` VALUES (2,1002);
INSERT INTO `hasRecord` VALUES (3,1003);
INSERT INTO `hasRecord` VALUES (4,1004);


CREATE TABLE `paidBy`(
	`payerSSN` varchar(12) NOT NULL,
	`pId` int(11) NOT NULL,
	PRIMARY KEY (`payerSSN`,`pId`),
	CONSTRAINT `pSSN_in_paidBy` FOREIGN KEY (`payerSSN`) REFERENCES `BillingAccount` (`payerSSN`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `pId_in_paidBy` FOREIGN KEY (`pId`) REFERENCES `Patient` (`pId`) ON DELETE CASCADE ON UPDATE CASCADE
);


INSERT INTO `paidBy` VALUES ('000011234',1001);
INSERT INTO `paidBy` VALUES ('000021234',1002);
INSERT INTO `paidBy` VALUES ('000031234',1003);
INSERT INTO `paidBy` VALUES ('000041234',1004);