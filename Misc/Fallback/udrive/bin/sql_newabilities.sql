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
CREATE DATABASE IF NOT EXISTS `dominion` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `dominion`;


-- Structuur van  tabel dominion.Ability wordt geschreven
CREATE TABLE IF NOT EXISTS `Ability` (
  `abilityId` int(11) NOT NULL,
  `abilityName` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`abilityId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumpen data van tabel dominion.Ability: ~28 rows (ongeveer)
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
CREATE TABLE IF NOT EXISTS `Card` (
  `cardName` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `type` tinyint(4) NOT NULL,
  `cost` tinyint(4) NOT NULL,
  PRIMARY KEY (`cardName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumpen data van tabel dominion.Card: ~32 rows (ongeveer)
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
	('witch', 4, 4),
	('woodcutter', 3, 3),
	('workshop', 3, 3);
/*!40000 ALTER TABLE `Card` ENABLE KEYS */;


-- Structuur van  tabel dominion.CardAbility wordt geschreven
CREATE TABLE IF NOT EXISTS `CardAbility` (
  `cardName` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `abilityId` int(11) NOT NULL,
  `abilityAmount` int(11) NOT NULL,
  PRIMARY KEY (`cardName`,`abilityId`),
  KEY `FK__Ability` (`abilityId`),
  CONSTRAINT `FK__Ability` FOREIGN KEY (`abilityId`) REFERENCES `Ability` (`abilityId`),
  CONSTRAINT `FK__Card` FOREIGN KEY (`cardName`) REFERENCES `Card` (`cardName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Dumpen data van tabel dominion.CardAbility: ~61 rows (ongeveer)
/*!40000 ALTER TABLE `CardAbility` DISABLE KEYS */;
INSERT INTO `CardAbility` (`cardName`, `abilityId`, `abilityAmount`) VALUES
	('adventurer', 13, -1),
	('bureaucrat', 14, -1),
	('bureaucrat', 25, 1),
	('cellar', 0, -1),
	('cellar', 1, 1),
	('cellar', 5, -1),
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
	('moneylender', -1, 0),
	('moneylender', 3, 3),
	('moneylender', 7, 1),
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


-- Structuur van  view dominion.MissingCardAbilities wordt geschreven
-- Tijdelijke tabel wordt aangemaakt zodat we geen VIEW afhankelijkheidsfouten krijgen
CREATE TABLE `MissingCardAbilities` (
	`cardName` VARCHAR(30) NOT NULL COLLATE 'utf8_unicode_ci'
) ENGINE=MyISAM;


-- Structuur van  view dominion.MissingCardAbilities wordt geschreven
-- Tijdelijke tabel wordt verwijderd, en definitieve VIEW wordt aangemaakt.
DROP TABLE IF EXISTS `MissingCardAbilities`;
CREATE ALGORITHM=UNDEFINED DEFINER=`proton`@`%` SQL SECURITY DEFINER VIEW `MissingCardAbilities` AS select `Card`.`cardName` AS `cardName` from `Card` where (not(`Card`.`cardName` in (select distinct `CardAbility`.`cardName` from `CardAbility`)));
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
