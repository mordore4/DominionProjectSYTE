-- --------------------------------------------------------
-- Host:                         178.117.107.177
-- Server versie:                5.5.44-0+deb8u1 - (Raspbian)
-- Server OS:                    debian-linux-gnu
-- HeidiSQL Versie:              9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Databasestructuur van dominion wordt geschreven
DROP DATABASE IF EXISTS `dominion`;
CREATE DATABASE IF NOT EXISTS `dominion` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `dominion`;


-- Structuur van  tabel dominion.Ability wordt geschreven
DROP TABLE IF EXISTS `Ability`;
CREATE TABLE IF NOT EXISTS `Ability` (
  `abilityId` int(11) NOT NULL,
  `abilityName` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`abilityId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumpen data van tabel dominion.Ability: ~30 rows (ongeveer)
DELETE FROM `Ability`;
/*!40000 ALTER TABLE `Ability` DISABLE KEYS */;
INSERT INTO `Ability` (`abilityId`, `abilityName`) VALUES
	(-1, 'chooseCardsOfType'),
	(0, 'chooseCards'),
	(1, 'addAction'),
	(2, 'addBuy'),
	(3, 'addCoin'),
	(4, 'addCard'),
	(5, 'discard'),
	(6, 'trashThisCard'),
	(7, 'trashCards'),
	(8, 'trashCardAndGetValue'),
	(9, 'gainCardCostingUpTo'),
	(10, 'revealCardsOfDeck'),
	(11, 'playNextCardTwice'),
	(12, 'curseOtherPlayers'),
	(13, 'adventurerSpecial'),
	(14, 'bureaucratSpecial'),
	(15, 'cellarSpecial'),
	(16, 'chancellorSpecial'),
	(17, 'councilRoomSpecial'),
	(18, 'librarySpecial'),
	(19, 'militiaSpecial'),
	(20, 'moatSpecial'),
	(21, 'spySpecial'),
	(22, 'thiefSpecial'),
	(23, 'getVictoryPoints'),
	(24, 'gardenSpecial'),
	(25, 'gainSilverCard'),
	(26, 'gainTreasureCardCost'),
	(27, 'moneyLenderSpecial'),
	(28, 'remodelSpecial');
/*!40000 ALTER TABLE `Ability` ENABLE KEYS */;


-- Structuur van  tabel dominion.Card wordt geschreven
DROP TABLE IF EXISTS `Card`;
CREATE TABLE IF NOT EXISTS `Card` (
  `cardName` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `type` tinyint(4) NOT NULL,
  `cost` tinyint(4) NOT NULL,
  PRIMARY KEY (`cardName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumpen data van tabel dominion.Card: ~33 rows (ongeveer)
DELETE FROM `Card`;
/*!40000 ALTER TABLE `Card` DISABLE KEYS */;
INSERT INTO `Card` (`cardName`, `type`, `cost`) VALUES
	('adventurer', 3, 6),
	('bureaucrat', 4, 4),
	('cellar', 3, 2),
	('chancellor', 3, 3),
	('chapel', 3, 2),
	('copper', 1, 0),
	('council room', 3, 5),
	('curse', 2, 0),
	('duchy', 2, 5),
	('estate', 2, 2),
	('father time', 3, 10),
	('feast', 3, 4),
	('festival', 3, 5),
	('gardens', 6, 4),
	('gold', 1, 6),
	('laboratory', 3, 5),
	('library', 3, 5),
	('market', 3, 5),
	('militia', 4, 4),
	('mine', 3, 5),
	('moat', 5, 2),
	('moneylender', 3, 4),
	('province', 2, 8),
	('remodel', 3, 4),
	('silver', 1, 3),
	('smithy', 3, 4),
	('spy', 4, 4),
	('thief', 4, 4),
	('throne room', 3, 4),
	('village', 3, 3),
	('witch', 4, 5),
	('woodcutter', 3, 3),
	('workshop', 3, 3);
/*!40000 ALTER TABLE `Card` ENABLE KEYS */;


-- Structuur van  tabel dominion.CardAbility wordt geschreven
DROP TABLE IF EXISTS `CardAbility`;
CREATE TABLE IF NOT EXISTS `CardAbility` (
  `cardName` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `abilityId` int(11) NOT NULL,
  `abilityAmount` int(11) NOT NULL,
  PRIMARY KEY (`cardName`,`abilityId`),
  KEY `FK__Ability` (`abilityId`),
  CONSTRAINT `FK__Ability` FOREIGN KEY (`abilityId`) REFERENCES `Ability` (`abilityId`),
  CONSTRAINT `FK__Card` FOREIGN KEY (`cardName`) REFERENCES `Card` (`cardName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumpen data van tabel dominion.CardAbility: ~56 rows (ongeveer)
DELETE FROM `CardAbility`;
/*!40000 ALTER TABLE `CardAbility` DISABLE KEYS */;
INSERT INTO `CardAbility` (`cardName`, `abilityId`, `abilityAmount`) VALUES
	('adventurer', 13, -1),
	('bureaucrat', 14, -1),
	('bureaucrat', 25, 1),
	('cellar', 1, 1),
	('cellar', 15, -1),
	('chancellor', 3, 2),
	('chancellor', 16, -1),
	('chapel', 0, 4),
	('chapel', 7, 4),
	('copper', 3, 1),
	('council room', 2, 1),
	('council room', 4, 4),
	('council room', 17, -1),
	('curse', 23, -1),
	('duchy', 23, 3),
	('estate', 23, 1),
	('feast', 6, -1),
	('feast', 9, 5),
	('festival', 1, 2),
	('festival', 2, 1),
	('festival', 3, 2),
	('gardens', 24, -1),
	('gold', 3, 3),
	('laboratory', 1, 1),
	('laboratory', 4, 2),
	('library', 18, -1),
	('market', 1, 1),
	('market', 2, 1),
	('market', 3, 1),
	('market', 4, 1),
	('militia', 3, 2),
	('militia', 19, -1),
	('mine', -1, 1),
	('mine', 8, -1),
	('mine', 26, -1),
	('moat', 4, 2),
	('moat', 20, -1),
	('moneylender', 3, 3),
	('moneylender', 27, 1),
	('province', 23, 6),
	('remodel', 28, -1),
	('silver', 3, 2),
	('smithy', 4, 3),
	('spy', 1, 1),
	('spy', 4, 1),
	('spy', 21, -1),
	('thief', 22, -1),
	('throne room', -1, 345),
	('throne room', 11, -1),
	('village', 1, 2),
	('village', 4, 1),
	('witch', 4, 2),
	('witch', 12, -1),
	('woodcutter', 2, 1),
	('woodcutter', 3, 2),
	('workshop', 9, 4);
/*!40000 ALTER TABLE `CardAbility` ENABLE KEYS */;


-- Structuur van  tabel dominion.Cardset wordt geschreven
DROP TABLE IF EXISTS `Cardset`;
CREATE TABLE IF NOT EXISTS `Cardset` (
  `name` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumpen data van tabel dominion.Cardset: ~6 rows (ongeveer)
DELETE FROM `Cardset`;
/*!40000 ALTER TABLE `Cardset` DISABLE KEYS */;
INSERT INTO `Cardset` (`name`) VALUES
	('big money'),
	('first game'),
	('interaction'),
	('paranormal'),
	('size distortion'),
	('village square');
/*!40000 ALTER TABLE `Cardset` ENABLE KEYS */;


-- Structuur van  tabel dominion.CardsetCard wordt geschreven
DROP TABLE IF EXISTS `CardsetCard`;
CREATE TABLE IF NOT EXISTS `CardsetCard` (
  `cardset` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `cardName` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`cardName`,`cardset`),
  KEY `FK_CardsetCard_Cardset` (`cardset`),
  CONSTRAINT `FK_CardsetCard_Card` FOREIGN KEY (`cardName`) REFERENCES `Card` (`cardName`),
  CONSTRAINT `FK_CardsetCard_Cardset` FOREIGN KEY (`cardset`) REFERENCES `Cardset` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumpen data van tabel dominion.CardsetCard: ~60 rows (ongeveer)
DELETE FROM `CardsetCard`;
/*!40000 ALTER TABLE `CardsetCard` DISABLE KEYS */;
INSERT INTO `CardsetCard` (`cardset`, `cardName`) VALUES
	('big money', 'adventurer'),
	('big money', 'bureaucrat'),
	('big money', 'chancellor'),
	('big money', 'chapel'),
	('big money', 'feast'),
	('big money', 'laboratory'),
	('big money', 'market'),
	('big money', 'mine'),
	('big money', 'moneylender'),
	('big money', 'throne room'),
	('first game', 'cellar'),
	('first game', 'market'),
	('first game', 'militia'),
	('first game', 'mine'),
	('first game', 'moat'),
	('first game', 'remodel'),
	('first game', 'smithy'),
	('first game', 'village'),
	('first game', 'woodcutter'),
	('first game', 'workshop'),
	('interaction', 'bureaucrat'),
	('interaction', 'chancellor'),
	('interaction', 'council room'),
	('interaction', 'festival'),
	('interaction', 'library'),
	('interaction', 'militia'),
	('interaction', 'moat'),
	('interaction', 'spy'),
	('interaction', 'thief'),
	('interaction', 'village'),
	('paranormal', 'cellar'),
	('paranormal', 'chancellor'),
	('paranormal', 'father time'),
	('paranormal', 'laboratory'),
	('paranormal', 'market'),
	('paranormal', 'moneylender'),
	('paranormal', 'smithy'),
	('paranormal', 'throne room'),
	('paranormal', 'village'),
	('paranormal', 'workshop'),
	('size distortion', 'cellar'),
	('size distortion', 'chapel'),
	('size distortion', 'feast'),
	('size distortion', 'gardens'),
	('size distortion', 'laboratory'),
	('size distortion', 'thief'),
	('size distortion', 'village'),
	('size distortion', 'witch'),
	('size distortion', 'woodcutter'),
	('size distortion', 'workshop'),
	('village square', 'bureaucrat'),
	('village square', 'cellar'),
	('village square', 'festival'),
	('village square', 'library'),
	('village square', 'market'),
	('village square', 'remodel'),
	('village square', 'smithy'),
	('village square', 'throne room'),
	('village square', 'village'),
	('village square', 'woodcutter');
/*!40000 ALTER TABLE `CardsetCard` ENABLE KEYS */;


-- Structuur van  tabel dominion.Game wordt geschreven
DROP TABLE IF EXISTS `Game`;
CREATE TABLE IF NOT EXISTS `Game` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `start` datetime NOT NULL,
  `end` datetime DEFAULT NULL,
  `cardset` tinytext COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumpen data van tabel dominion.Game: ~17 rows (ongeveer)
DELETE FROM `Game`;
/*!40000 ALTER TABLE `Game` DISABLE KEYS */;
INSERT INTO `Game` (`id`, `start`, `end`, `cardset`) VALUES
	(24, '2016-05-17 14:39:32', NULL, 'first game'),
	(25, '2016-05-17 14:39:33', NULL, 'first game'),
	(26, '2016-05-17 14:39:33', NULL, 'first game'),
	(27, '2016-05-17 14:39:33', NULL, 'first game'),
	(28, '2016-05-17 14:39:33', NULL, 'first game'),
	(29, '2016-05-17 14:39:33', NULL, 'first game'),
	(30, '2016-05-17 14:39:35', NULL, 'first game'),
	(31, '2016-05-17 14:39:35', NULL, 'first game'),
	(32, '2016-05-17 14:39:35', NULL, 'first game'),
	(33, '2016-05-17 14:39:35', NULL, 'first game'),
	(34, '2016-05-17 14:39:36', NULL, 'first game'),
	(35, '2016-05-17 14:39:36', NULL, 'first game'),
	(36, '2016-05-17 14:39:36', NULL, 'first game'),
	(37, '2016-05-17 14:39:36', NULL, 'first game'),
	(38, '2016-05-17 14:39:36', NULL, 'first game'),
	(39, '2016-05-17 14:39:36', NULL, 'first game'),
	(40, '2016-05-17 14:39:36', NULL, 'first game');
/*!40000 ALTER TABLE `Game` ENABLE KEYS */;


-- Structuur van  tabel dominion.GamePlayer wordt geschreven
DROP TABLE IF EXISTS `GamePlayer`;
CREATE TABLE IF NOT EXISTS `GamePlayer` (
  `game_id` int(10) unsigned NOT NULL,
  `name` tinytext COLLATE utf8_unicode_ci NOT NULL,
  `number` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`game_id`,`name`(100)),
  CONSTRAINT `FK_GamePlayer_Game` FOREIGN KEY (`game_id`) REFERENCES `Game` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumpen data van tabel dominion.GamePlayer: ~34 rows (ongeveer)
DELETE FROM `GamePlayer`;
/*!40000 ALTER TABLE `GamePlayer` DISABLE KEYS */;
INSERT INTO `GamePlayer` (`game_id`, `name`, `number`) VALUES
	(24, 'alice', 1),
	(24, 'bob', 0),
	(25, 'alice', 1),
	(25, 'bob', 0),
	(26, 'alice', 1),
	(26, 'bob', 0),
	(27, 'alice', 1),
	(27, 'bob', 0),
	(28, 'alice', 1),
	(28, 'bob', 0),
	(29, 'alice', 1),
	(29, 'bob', 0),
	(30, 'alice', 1),
	(30, 'bob', 0),
	(31, 'alice', 1),
	(31, 'bob', 0),
	(32, 'alice', 1),
	(32, 'bob', 0),
	(33, 'alice', 1),
	(33, 'bob', 0),
	(34, 'alice', 1),
	(34, 'bob', 0),
	(35, 'alice', 1),
	(35, 'bob', 0),
	(36, 'alice', 1),
	(36, 'bob', 0),
	(37, 'alice', 1),
	(37, 'bob', 0),
	(38, 'alice', 1),
	(38, 'bob', 0),
	(39, 'player1', 0),
	(39, 'player2', 1),
	(40, 'alice', 1),
	(40, 'bob', 0);
/*!40000 ALTER TABLE `GamePlayer` ENABLE KEYS */;


-- Structuur van  tabel dominion.History wordt geschreven
DROP TABLE IF EXISTS `History`;
CREATE TABLE IF NOT EXISTS `History` (
  `game_id` int(10) unsigned NOT NULL,
  `sequence` int(10) unsigned NOT NULL,
  `event` tinytext COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`game_id`,`sequence`),
  CONSTRAINT `FK_History_Game` FOREIGN KEY (`game_id`) REFERENCES `Game` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumpen data van tabel dominion.History: ~0 rows (ongeveer)
DELETE FROM `History`;
/*!40000 ALTER TABLE `History` DISABLE KEYS */;
/*!40000 ALTER TABLE `History` ENABLE KEYS */;


-- Structuur van  view dominion.MissingCardAbilities wordt geschreven
DROP VIEW IF EXISTS `MissingCardAbilities`;
-- Tijdelijke tabel wordt aangemaakt zodat we geen VIEW afhankelijkheidsfouten krijgen
CREATE TABLE `MissingCardAbilities` (
	`cardName` VARCHAR(30) NOT NULL COLLATE 'utf8_unicode_ci'
) ENGINE=MyISAM;


-- Structuur van  view dominion.MissingCardAbilities wordt geschreven
DROP VIEW IF EXISTS `MissingCardAbilities`;
-- Tijdelijke tabel wordt verwijderd, en definitieve VIEW wordt aangemaakt.
DROP TABLE IF EXISTS `MissingCardAbilities`;
CREATE ALGORITHM=UNDEFINED DEFINER=`proton`@`%` SQL SECURITY DEFINER VIEW `MissingCardAbilities` AS select `Card`.`cardName` AS `cardName` from `Card` where (not(`Card`.`cardName` in (select distinct `CardAbility`.`cardName` from `CardAbility`)));
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
