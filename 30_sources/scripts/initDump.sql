-- MySQL dump 10.13  Distrib 5.1.41, for apple-darwin9.5.0 (i386)
--
-- Initial Database Dump for OpenPatternRepository
-- Providing:
--  * Categories
--  * Templates, including components
--  * Licenses
--  * Quality Attributes for Forces and Consequences
--  * Relationship types
-- 
-- Host: localhost    Database: patternrepository
-- ------------------------------------------------------
-- Server version	5.1.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `CATEGORY`
--

DROP TABLE IF EXISTS `CATEGORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CATEGORY` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `PARENT_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`),
  KEY `FK_CATEGORY_PARENT_ID` (`PARENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CATEGORY`
--

LOCK TABLES `CATEGORY` WRITE;
/*!40000 ALTER TABLE `CATEGORY` DISABLE KEYS */;
INSERT INTO `CATEGORY` VALUES (1,'Categories',1),(2,'Pattern',1),(3,'Technologie',1),(4,'Architectural Pattern',2),(5,'Enterprise Pattern',2),(6,'Design Pattern',2),(7,'Analysis Pattern',2),(8,'Security',3),(9,'Object Relational Mapping',3),(10,'Programming Language',3),(11,'Database',3),(12,'Logging',3),(13,'Web',3),(14,'Java',10), (15,'Gang of Four',6);
/*!40000 ALTER TABLE `CATEGORY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CATEGORY_CATEGORY`
--

DROP TABLE IF EXISTS `CATEGORY_CATEGORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CATEGORY_CATEGORY` (
  `Category_ID` bigint(20) NOT NULL,
  `children_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Category_ID`,`children_ID`),
  KEY `FK_CATEGORY_CATEGORY_children_ID` (`children_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CATEGORY_CATEGORY`
--

LOCK TABLES `CATEGORY_CATEGORY` WRITE;
/*!40000 ALTER TABLE `CATEGORY_CATEGORY` DISABLE KEYS */;
/*!40000 ALTER TABLE `CATEGORY_CATEGORY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COMPONENT`
--

DROP TABLE IF EXISTS `COMPONENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `COMPONENT` (
  `ID` bigint(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `IDENTIFIER` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `SORTORDER` int(11) DEFAULT NULL,
  `MANDATORY` tinyint(1) DEFAULT '0',
  `TYPE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COMPONENT`
--

LOCK TABLES `COMPONENT` WRITE;
/*!40000 ALTER TABLE `COMPONENT` DISABLE KEYS */;
INSERT INTO `COMPONENT` VALUES 
(1,'','description','Description',0,1,'DESCRIPTION'),
(2,'','example','Example/Figure',1,0,'DESCRIPTION'),
(3,'','example','Example',0,0,'PROBLEM'),
(9,'','implementation','Implementation',6,0,'SOLUTION'),
(4,'','context','Context',1,0,'DESCRIPTION'),
(10,'','consequences','Consequences',7,0,'CONSEQUENCES'),
(7,'','structure','Structure',4,0,'SOLUTION'),
(5,'','problem','Problem',2,0,'PROBLEM'),
(8,'','dynamics','Dynamics',5,0,'SOLUTION'),
(6,'','solution','Solution',3,0,'SOLUTION'),
(16,'','solution','Solution',5,0,'SOLUTION'),
(15,'','forces','Forces',4,0,'FORCES'),
(17,'','consequences','Consequences',6,0,'CONSEQUENCES'),
(18,'','knownuse','Known uses',7,0,'SOLUTION'),
(11,'','description','Description',0,0,'DESCRIPTION'),
(12,'','version','Version',1,0,'DESCRIPTION'),
(14,'','problem','Problem',3,0,'PROBLEM'),
(13,'','context','Context',2,0,'CONTEXT'),
(24,'','dynamics','Dynamics',5,0,'SOLUTION'),
(26,'','consequences','Consequences',7,0,'CONSEQUENCES'),
(25,'','implementation','Implementation',6,0,'SOLUTION'),
(20,'','context','Context',1,0,'DESCRIPTION'),
(21,'','problem','Problem',2,0,'PROBLEM'),
(19,'','example','Example',0,0,'PROBLEM'),
(22,'','solution','Solution',3,0,'SOLUTION'),
(23,'','structure','Structure',4,0,'SOLUTION'),
(43,'','consequences','Consequences',9,0,'CONSEQUENCES'),
(35,'The current version number of the technology. This is useful to find out later if the description is outdated and needs to be revised.','version','Version',1,0,'DESCRIPTION'),
(34,'1-5 sentences describing the patterns in (very) brief','description','Description',0,0,'DESCRIPTION'),
(37,'The license under which the software is published and the costs if applicable','licosts','License and Costs',3,0,'DESCRIPTION'),
(36,'A URL pointing to a location where the software can be obtained','url','URL',2,0,'DESCRIPTION'),
(42,'','solution','Solution',8,0,'SOLUTION'),
(40,'','problem','Problem',6,0,'PROBLEM'),
(44,'References used in the description','references','References',10,0,'DESCRIPTION'),
(39,'','context','Context',5,0,'CONTEXT'),
(41,'','forces','Forces',7,0,'FORCES'),
(38,'Platforms supported my the technology','platforms','Platforms',4,0,'DESCRIPTION'),
(31,'','solution','Solution',4,0,'SOLUTION'),
(28,'','context','Context',1,0,'CONTEXT'),
(29,'','problem','Problem',2,0,'PROBLEM'),
(27,'1-5 sentences describing the patterns in (very) brief','description','Description',0,0,'DESCRIPTION'),
(33,'References used in the description','references','References',6,0,'DESCRIPTION'),
(30,'','forces','Forces',3,0,'FORCES'),
(32,'','consequences','Consequences',5,0,'CONSEQUENCES');
/*!40000 ALTER TABLE `COMPONENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONSEQUENCE`
--

DROP TABLE IF EXISTS `CONSEQUENCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONSEQUENCE` (
  `ID` bigint(20) NOT NULL,
  `IMPACTINDICATION` varchar(255) DEFAULT NULL,
  `DESCRIPTION` text,
  `QUALITYATTRIBUTE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CONSEQUENCE_QUALITYATTRIBUTE_ID` (`QUALITYATTRIBUTE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONSEQUENCE`
--

LOCK TABLES `CONSEQUENCE` WRITE;
/*!40000 ALTER TABLE `CONSEQUENCE` DISABLE KEYS */;
/*!40000 ALTER TABLE `CONSEQUENCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DRIVER`
--

DROP TABLE IF EXISTS `DRIVER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DRIVER` (
  `ID` bigint(20) NOT NULL,
  `DTYPE` varchar(31) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DRIVER`
--

LOCK TABLES `DRIVER` WRITE;
/*!40000 ALTER TABLE `DRIVER` DISABLE KEYS */;
/*!40000 ALTER TABLE `DRIVER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FILE`
--

DROP TABLE IF EXISTS `FILE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FILE` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `CHECKSUM` varchar(255) NOT NULL,
  `SIZEINBYTES` int(11) DEFAULT NULL,
  `CONTENT` longblob,
  `DATECREATED` datetime NOT NULL,
  `MIME` varchar(255) DEFAULT NULL,
  `LICENSE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_FILE_LICENSE_ID` (`LICENSE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FILE`
--

LOCK TABLES `FILE` WRITE;
/*!40000 ALTER TABLE `FILE` DISABLE KEYS */;
/*!40000 ALTER TABLE `FILE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LICENSE`
--

DROP TABLE IF EXISTS `LICENSE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LICENSE` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `RESTRICTIVE` tinyint(1) DEFAULT '0',
  `LICENSESOURCE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LICENSE`
--

LOCK TABLES `LICENSE` WRITE;
/*!40000 ALTER TABLE `LICENSE` DISABLE KEYS */;
INSERT INTO `LICENSE` VALUES (1,'Creative Commons',0,'http://creativecommons.org/licenses/by-sa/3.0/'),(2,'GNU GPL Version 3',0,'http://www.gnu.de/documents/gpl.de.html'),(3,'Reserved Copyright',1,'http://en.wikipedia.org/wiki/Copyright');
/*!40000 ALTER TABLE `LICENSE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OPRFORCE`
--

DROP TABLE IF EXISTS `OPRFORCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OPRFORCE` (
  `ID` bigint(20) NOT NULL,
  `FUNCTIONALITY` varchar(255) DEFAULT NULL,
  `IMPACTINDICATION` varchar(255) DEFAULT NULL,
  `DESCRIPTION` text,
  `QUALITYATTRIBUTE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_OPRFORCE_QUALITYATTRIBUTE_ID` (`QUALITYATTRIBUTE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OPRFORCE`
--

LOCK TABLES `OPRFORCE` WRITE;
/*!40000 ALTER TABLE `OPRFORCE` DISABLE KEYS */;
/*!40000 ALTER TABLE `OPRFORCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATTERN`
--

DROP TABLE IF EXISTS `PATTERN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATTERN` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `UNIQUENAME` varchar(255) NOT NULL,
  `CURRENTVERSION_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNIQUENAME` (`UNIQUENAME`),
  KEY `FK_PATTERN_CURRENTVERSION_ID` (`CURRENTVERSION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATTERN`
--

LOCK TABLES `PATTERN` WRITE;
/*!40000 ALTER TABLE `PATTERN` DISABLE KEYS */;
/*!40000 ALTER TABLE `PATTERN` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATTERNVERSION`
--

DROP TABLE IF EXISTS `PATTERNVERSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATTERNVERSION` (
  `ID` bigint(20) NOT NULL,
  `SOURCE` varchar(255) DEFAULT NULL,
  `AUTHOR` varchar(255) DEFAULT NULL,
  `DOCUMENTEDWHEN` datetime DEFAULT NULL,
  `TEMPLATE_ID` bigint(20) DEFAULT NULL,
  `LICENSE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PATTERNVERSION_LICENSE_ID` (`LICENSE_ID`),
  KEY `FK_PATTERNVERSION_TEMPLATE_ID` (`TEMPLATE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATTERNVERSION`
--

LOCK TABLES `PATTERNVERSION` WRITE;
/*!40000 ALTER TABLE `PATTERNVERSION` DISABLE KEYS */;
/*!40000 ALTER TABLE `PATTERNVERSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATTERNVERSION_CONSEQUENCE`
--

DROP TABLE IF EXISTS `PATTERNVERSION_CONSEQUENCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATTERNVERSION_CONSEQUENCE` (
  `PatternVersion_ID` bigint(20) NOT NULL,
  `consequences_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`PatternVersion_ID`,`consequences_ID`),
  KEY `FK_PATTERNVERSION_CONSEQUENCE_consequences_ID` (`consequences_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATTERNVERSION_CONSEQUENCE`
--

LOCK TABLES `PATTERNVERSION_CONSEQUENCE` WRITE;
/*!40000 ALTER TABLE `PATTERNVERSION_CONSEQUENCE` DISABLE KEYS */;
/*!40000 ALTER TABLE `PATTERNVERSION_CONSEQUENCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATTERNVERSION_CONTEXTS`
--

DROP TABLE IF EXISTS `PATTERNVERSION_CONTEXTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATTERNVERSION_CONTEXTS` (
  `PatternVersion_ID` bigint(20) NOT NULL,
  `context_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`PatternVersion_ID`,`context_ID`),
  KEY `FK_PATTERNVERSION_CONTEXTS_context_ID` (`context_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATTERNVERSION_CONTEXTS`
--

LOCK TABLES `PATTERNVERSION_CONTEXTS` WRITE;
/*!40000 ALTER TABLE `PATTERNVERSION_CONTEXTS` DISABLE KEYS */;
/*!40000 ALTER TABLE `PATTERNVERSION_CONTEXTS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATTERNVERSION_DESCRIPTIONS`
--

DROP TABLE IF EXISTS `PATTERNVERSION_DESCRIPTIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATTERNVERSION_DESCRIPTIONS` (
  `PatternVersion_ID` bigint(20) NOT NULL,
  `description_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`PatternVersion_ID`,`description_ID`),
  KEY `FK_PATTERNVERSION_DESCRIPTIONS_description_ID` (`description_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATTERNVERSION_DESCRIPTIONS`
--

LOCK TABLES `PATTERNVERSION_DESCRIPTIONS` WRITE;
/*!40000 ALTER TABLE `PATTERNVERSION_DESCRIPTIONS` DISABLE KEYS */;
/*!40000 ALTER TABLE `PATTERNVERSION_DESCRIPTIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATTERNVERSION_FILE`
--

DROP TABLE IF EXISTS `PATTERNVERSION_FILE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATTERNVERSION_FILE` (
  `PatternVersion_ID` bigint(20) NOT NULL,
  `files_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`PatternVersion_ID`,`files_ID`),
  KEY `FK_PATTERNVERSION_FILE_files_ID` (`files_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATTERNVERSION_FILE`
--

LOCK TABLES `PATTERNVERSION_FILE` WRITE;
/*!40000 ALTER TABLE `PATTERNVERSION_FILE` DISABLE KEYS */;
/*!40000 ALTER TABLE `PATTERNVERSION_FILE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATTERNVERSION_OPRFORCE`
--

DROP TABLE IF EXISTS `PATTERNVERSION_OPRFORCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATTERNVERSION_OPRFORCE` (
  `PatternVersion_ID` bigint(20) NOT NULL,
  `forces_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`PatternVersion_ID`,`forces_ID`),
  KEY `FK_PATTERNVERSION_OPRFORCE_forces_ID` (`forces_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATTERNVERSION_OPRFORCE`
--

LOCK TABLES `PATTERNVERSION_OPRFORCE` WRITE;
/*!40000 ALTER TABLE `PATTERNVERSION_OPRFORCE` DISABLE KEYS */;
/*!40000 ALTER TABLE `PATTERNVERSION_OPRFORCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATTERNVERSION_PROBLEMS`
--

DROP TABLE IF EXISTS `PATTERNVERSION_PROBLEMS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATTERNVERSION_PROBLEMS` (
  `PatternVersion_ID` bigint(20) NOT NULL,
  `problem_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`PatternVersion_ID`,`problem_ID`),
  KEY `FK_PATTERNVERSION_PROBLEMS_problem_ID` (`problem_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATTERNVERSION_PROBLEMS`
--

LOCK TABLES `PATTERNVERSION_PROBLEMS` WRITE;
/*!40000 ALTER TABLE `PATTERNVERSION_PROBLEMS` DISABLE KEYS */;
/*!40000 ALTER TABLE `PATTERNVERSION_PROBLEMS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATTERNVERSION_SOLUTIONS`
--

DROP TABLE IF EXISTS `PATTERNVERSION_SOLUTIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATTERNVERSION_SOLUTIONS` (
  `PatternVersion_ID` bigint(20) NOT NULL,
  `solution_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`PatternVersion_ID`,`solution_ID`),
  KEY `FK_PATTERNVERSION_SOLUTIONS_solution_ID` (`solution_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATTERNVERSION_SOLUTIONS`
--

LOCK TABLES `PATTERNVERSION_SOLUTIONS` WRITE;
/*!40000 ALTER TABLE `PATTERNVERSION_SOLUTIONS` DISABLE KEYS */;
/*!40000 ALTER TABLE `PATTERNVERSION_SOLUTIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATTERN_CATEGORY`
--

DROP TABLE IF EXISTS `PATTERN_CATEGORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATTERN_CATEGORY` (
  `categories_ID` bigint(20) NOT NULL,
  `categoryPatterns_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`categories_ID`,`categoryPatterns_ID`),
  KEY `FK_PATTERN_CATEGORY_categoryPatterns_ID` (`categoryPatterns_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATTERN_CATEGORY`
--

LOCK TABLES `PATTERN_CATEGORY` WRITE;
/*!40000 ALTER TABLE `PATTERN_CATEGORY` DISABLE KEYS */;
/*!40000 ALTER TABLE `PATTERN_CATEGORY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATTERN_TAG`
--

DROP TABLE IF EXISTS `PATTERN_TAG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATTERN_TAG` (
  `tagPatterns_ID` bigint(20) NOT NULL,
  `tags_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`tagPatterns_ID`,`tags_ID`),
  KEY `FK_PATTERN_TAG_tags_ID` (`tags_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATTERN_TAG`
--

LOCK TABLES `PATTERN_TAG` WRITE;
/*!40000 ALTER TABLE `PATTERN_TAG` DISABLE KEYS */;
/*!40000 ALTER TABLE `PATTERN_TAG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATTERN_VERSIONS`
--

DROP TABLE IF EXISTS `PATTERN_VERSIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATTERN_VERSIONS` (
  `Pattern_ID` bigint(20) NOT NULL,
  `versions_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Pattern_ID`,`versions_ID`),
  KEY `FK_PATTERN_VERSIONS_versions_ID` (`versions_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATTERN_VERSIONS`
--

LOCK TABLES `PATTERN_VERSIONS` WRITE;
/*!40000 ALTER TABLE `PATTERN_VERSIONS` DISABLE KEYS */;
/*!40000 ALTER TABLE `PATTERN_VERSIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QUALITYATTRIBUTE`
--

DROP TABLE IF EXISTS `QUALITYATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QUALITYATTRIBUTE` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QUALITYATTRIBUTE`
--

LOCK TABLES `QUALITYATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `QUALITYATTRIBUTE` DISABLE KEYS */;
INSERT INTO `QUALITYATTRIBUTE` VALUES (1,'Accessibility',''),(2,'Accountability',''),(3,'Adaptability',''),(4,'Availability',''),(5,'Standardization',''),(6,'Capability',''),(7,'Changeability',''),(8,'Compatibility',''),(9,'Composability',''),(10,'Customizability',''),(11,'Dependability',''),(12,'Deployability',''),(13,'Distributability',''),(14,'Effectiveness',''),(15,'Efficiency',''),(16,'Extensibility',''),(17,'Installability',''),(18,'Interchangeability',''),(19,'Interoperability',''),(20,'Lernability',''),(21,'Maintainability',''),(22,'Modularity',''),(23,'Operability',''),(24,'Portability',''),(25,'Recoverability',''),(26,'Reliability',''),(27,'Reusability',''),(28,'Scalability',''),(29,'Serviceability',''),(30,'Securability',''),(31,'Stability ',''),(32,'Survivability',''),(33,'Testability',''),(34,'Usability','');
/*!40000 ALTER TABLE `QUALITYATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RELATIONSHIP`
--

DROP TABLE IF EXISTS `RELATIONSHIP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RELATIONSHIP` (
  `ID` bigint(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `PATTERNA_ID` bigint(20) DEFAULT NULL,
  `PATTERNB_ID` bigint(20) DEFAULT NULL,
  `TYPE_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_RELATIONSHIP_PATTERNB_ID` (`PATTERNB_ID`),
  KEY `FK_RELATIONSHIP_PATTERNA_ID` (`PATTERNA_ID`),
  KEY `FK_RELATIONSHIP_TYPE_ID` (`TYPE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RELATIONSHIP`
--

LOCK TABLES `RELATIONSHIP` WRITE;
/*!40000 ALTER TABLE `RELATIONSHIP` DISABLE KEYS */;
/*!40000 ALTER TABLE `RELATIONSHIP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RELATIONSHIPTYPE`
--

DROP TABLE IF EXISTS `RELATIONSHIPTYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RELATIONSHIPTYPE` (
  `ID` bigint(20) NOT NULL,
  `DTYPE` varchar(31) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RELATIONSHIPTYPE`
--

LOCK TABLES `RELATIONSHIPTYPE` WRITE;
/*!40000 ALTER TABLE `RELATIONSHIPTYPE` DISABLE KEYS */;
INSERT INTO `RELATIONSHIPTYPE` VALUES (1, 'Alternative',NULL), (2, 'Combination',NULL), (3, 'Variant',NULL);
/*!40000 ALTER TABLE `RELATIONSHIPTYPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SEQUENCE`
--

DROP TABLE IF EXISTS `SEQUENCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SEQUENCE` (
  `SEQ_NAME` varchar(50) NOT NULL,
  `SEQ_COUNT` decimal(38,0) DEFAULT NULL,
  PRIMARY KEY (`SEQ_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SEQUENCE`
--

LOCK TABLES `SEQUENCE` WRITE;
/*!40000 ALTER TABLE `SEQUENCE` DISABLE KEYS */;
INSERT INTO `SEQUENCE` (`SEQ_NAME`, `SEQ_COUNT`) VALUES 
('PK_LICENSE','4'),
('PK_FILE','0'),
('PK_TAG','0'),
('PK_PATTERN','0'),
('PK_RELATIONSHIPTYPE','4'),
('PK_TEXTBLOCK','0'),
('PK_CATEGORY','16'),
('PK_QA','35'),
('PK_DRIVER','0'),
('PK_PATTERNVERSION','0'),
('SEQ_GEN','0'),
('PK_TEMPLATE','7');
/*!40000 ALTER TABLE `SEQUENCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TAG`
--

DROP TABLE IF EXISTS `TAG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TAG` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TAG`
--

LOCK TABLES `TAG` WRITE;
/*!40000 ALTER TABLE `TAG` DISABLE KEYS */;
/*!40000 ALTER TABLE `TAG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TEMPLATE`
--

DROP TABLE IF EXISTS `TEMPLATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TEMPLATE` (
  `ID` bigint(20) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `AUTHOR` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TEMPLATE`
--

LOCK TABLES `TEMPLATE` WRITE;
/*!40000 ALTER TABLE `TEMPLATE` DISABLE KEYS */;
INSERT INTO `TEMPLATE` VALUES 
(1,'Pattern Thumbnail','Uwe van Heesch','Pattern Thumbnail'),
(2,'Template used in Patter Oriented Software Architecture','Uwe van Heesch','POSA'),
(3,'Template used to describe technologies, frameworks and other existing software systems','Uwe van Heesch','Technology'),(4,'Template used in Design Patterns: elements of reusable object-oriented software','Uwe van Heesch','Gang of Four'),(5,'Elaborate template used to describe technologies in the OPR','Uwe van Heesch','OPR Technology'),
(6,'Template for Software Architectural Patterns','Uwe van Heesch','OPR Architectural Pattern');
/*!40000 ALTER TABLE `TEMPLATE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TEMPLATE_COMPONENT`
--

DROP TABLE IF EXISTS `TEMPLATE_COMPONENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TEMPLATE_COMPONENT` (
  `Template_ID` bigint(20) NOT NULL,
  `components_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`Template_ID`,`components_ID`),
  KEY `FK_TEMPLATE_COMPONENT_components_ID` (`components_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TEMPLATE_COMPONENT`
--

LOCK TABLES `TEMPLATE_COMPONENT` WRITE;
/*!40000 ALTER TABLE `TEMPLATE_COMPONENT` DISABLE KEYS */;
INSERT INTO `TEMPLATE_COMPONENT` VALUES (1,1),(1,2),(2,3),(2,4),(2,5),(2,6),(2,7),(2,8),(2,9),(2,10),(3,11),(3,12),(3,13),(3,14),(3,15),(3,16),(3,17),(3,18),(4,19),(4,20),(4,21),(4,22),(4,23),(4,24),(4,25),(4,26),(6,27),(6,28),(6,29),(6,30),(6,31),(6,32),(6,33),(5,34),(5,35),(5,36),(5,37),(5,38),(5,39),(5,40),(5,41),(5,42),(5,43),(5,44);
/*!40000 ALTER TABLE `TEMPLATE_COMPONENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TEXTBLOCK`
--

DROP TABLE IF EXISTS `TEXTBLOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TEXTBLOCK` (
  `ID` bigint(20) NOT NULL,
  `TEXT` text,
  `COMPONENT_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_TEXTBLOCK_COMPONENT_ID` (`COMPONENT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TEXTBLOCK`
--

LOCK TABLES `TEXTBLOCK` WRITE;
/*!40000 ALTER TABLE `TEXTBLOCK` DISABLE KEYS */;
/*!40000 ALTER TABLE `TEXTBLOCK` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-12-10 16:20:10
