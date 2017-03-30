-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 11, 2015 alle 16:36
-- Versione del server: 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

DROP DATABASE IF EXISTS carloan;

CREATE DATABASE carloan;

USE carloan;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `carloan`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `Agenzie`
--

CREATE TABLE IF NOT EXISTS `Agenzie` (
`ID` int(11) NOT NULL,
  `Nome` varchar(35) NOT NULL,
  `NumTel` text NOT NULL,
  `Indirizzo` text NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dump dei dati per la tabella `Agenzie`
--

INSERT INTO `Agenzie` (`ID`, `Nome`, `NumTel`, `Indirizzo`) VALUES
(1, 'Ferrari SRL', '0803456879', 'Via Giovanni Amendola 184'),
(2, 'Belushi Corp', '0993456198', 'Via C. Battisti 456'),
(3, 'Fratelli Rossi', '0807766345', 'Via Rocco di Cillo 3'),
(4, 'Alderaan Cars', '0118877345', 'Via Coruscant 1');

-- --------------------------------------------------------

--
-- Struttura della tabella `auto`
--

CREATE TABLE IF NOT EXISTS `auto` (
`ID` int(11) NOT NULL,
  `Targa` char(7) NOT NULL,
  `Chilometraggio` int(11) NOT NULL,
  `Modello` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dump dei dati per la tabella `auto`
--

INSERT INTO `auto` (`ID`, `Targa`, `Chilometraggio`, `Modello`) VALUES
(1, 'AA111AA', 67, 1),
(2, 'TA942BA', 12, 2),
(3, 'TY653AS', 23, 4),
(4, 'WW456RT', 56, 5),
(6, 'AA999BB', 114, 7),
(7, 'ST017OU', 90, 6),
(8, 'RO770TU', 3, 3),
(9, 'TU531BL', 67, 8);

-- --------------------------------------------------------

--
-- Struttura della tabella `autodisponibili`
--

CREATE TABLE IF NOT EXISTS `autodisponibili` (
`ID` int(11) NOT NULL,
  `Auto` int(11) NOT NULL,
  `Agenzia` int(11) NOT NULL,
  `DataInizio` date NOT NULL,
  `DataFine` date DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=17 ;

--
-- Dump dei dati per la tabella `autodisponibili`
--

INSERT INTO `autodisponibili` (`ID`, `Auto`, `Agenzia`, `DataInizio`, `DataFine`) VALUES
(3, 3, 4, '2015-11-11', NULL),
(4, 4, 1, '2015-11-11', NULL),
(6, 7, 3, '2015-11-11', NULL),
(7, 8, 1, '2015-11-11', NULL),
(8, 9, 2, '2015-11-11', NULL),
(9, 2, 2, '2015-11-11', '2015-11-11'),
(10, 2, 2, '2015-11-15', NULL),
(12, 6, 2, '2015-11-20', NULL),
(14, 1, 2, '2015-11-18', NULL),
(15, 1, 4, '2015-11-11', '2015-11-20'),
(16, 6, 2, '2015-11-11', '2015-11-17');

-- --------------------------------------------------------

--
-- Struttura della tabella `automanutenzione`
--

CREATE TABLE IF NOT EXISTS `automanutenzione` (
`ID` int(11) NOT NULL,
  `Auto` int(11) NOT NULL,
  `TipoManut` enum('ORDINARIA','STRAORDINARIA') NOT NULL,
  `DataInizio` date NOT NULL,
  `DataFine` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Struttura della tabella `clienti`
--

CREATE TABLE IF NOT EXISTS `clienti` (
`ID` int(11) NOT NULL,
  `Nome` text NOT NULL,
  `Cognome` text NOT NULL,
  `NumTel` text NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dump dei dati per la tabella `clienti`
--

INSERT INTO `clienti` (`ID`, `Nome`, `Cognome`, `NumTel`) VALUES
(1, 'Andrea', 'Del Fante', '3347898187'),
(2, 'Angelo', 'Milano', '3345676198'),
(3, 'Simone', 'Marzulli', '0997765234'),
(4, 'Pippo', 'Visci', '3367629102'),
(5, 'Lorenzo', 'Micalizzi', '0804455321');

-- --------------------------------------------------------

--
-- Struttura della tabella `contrattiaperti`
--

CREATE TABLE IF NOT EXISTS `contrattiaperti` (
`ID` int(11) NOT NULL,
  `DataInizio` date NOT NULL,
  `DataLimite` date NOT NULL,
  `Acconto` float NOT NULL,
  `BaseNoleggio` enum('GIORNALIERA','SETTIMANALE') NOT NULL,
  `Chilometraggio` enum('LIMITATO','ILLIMITATO') NOT NULL,
  `FasciaMacchina` int(11) NOT NULL,
  `LuogoRest` int(11) DEFAULT NULL,
  `Agenzia` int(11) NOT NULL,
  `Cliente` int(11) NOT NULL,
  `Auto` int(11) DEFAULT NULL,
  `contrattoAperto` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dump dei dati per la tabella `contrattiaperti`
--

INSERT INTO `contrattiaperti` (`ID`, `DataInizio`, `DataLimite`, `Acconto`, `BaseNoleggio`, `Chilometraggio`, `FasciaMacchina`, `LuogoRest`, `Agenzia`, `Cliente`, `Auto`, `contrattoAperto`) VALUES
(2, '2015-11-12', '2015-11-14', 34, 'GIORNALIERA', 'LIMITATO', 1, 3, 2, 1, 2, 1),
(3, '2015-11-12', '2015-11-19', 79, 'SETTIMANALE', 'ILLIMITATO', 4, 2, 2, 2, 6, 0),
(4, '2015-11-13', '2015-11-17', 34, 'GIORNALIERA', 'ILLIMITATO', 5, 4, 2, 3, 1, 0),
(5, '2015-11-20', '2015-11-27', 98, 'SETTIMANALE', 'LIMITATO', 5, 2, 2, 4, NULL, 1),
(6, '2015-11-19', '2015-11-22', 0, 'GIORNALIERA', 'LIMITATO', 1, 2, 2, 2, NULL, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `contrattichiusi`
--

CREATE TABLE IF NOT EXISTS `contrattichiusi` (
`ID` int(11) NOT NULL,
  `ContrattoAperto` int(11) NOT NULL,
  `DataRientro` date NOT NULL,
  `KmPercorsi` int(11) NOT NULL,
  `ImportoEffettivo` float NOT NULL,
  `Saldo` float NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dump dei dati per la tabella `contrattichiusi`
--

INSERT INTO `contrattichiusi` (`ID`, `ContrattoAperto`, `DataRientro`, `KmPercorsi`, `ImportoEffettivo`, `Saldo`) VALUES
(1, 4, '2015-11-20', 67, 1265, 1231),
(2, 3, '2015-11-17', 80, 0, -79);

-- --------------------------------------------------------

--
-- Struttura della tabella `fasce`
--

CREATE TABLE IF NOT EXISTS `fasce` (
`ID` int(11) NOT NULL,
  `Tipo` varchar(3) NOT NULL COMMENT 'Nome effettivo della fascia',
  `Descrizione` text NOT NULL,
  `CostoKm` float NOT NULL,
  `CostoGiornoLim` float NOT NULL,
  `CostoSettimanaLim` float NOT NULL,
  `CostoGiornoIllim` float NOT NULL,
  `CostoSettimanaIllim` float NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dump dei dati per la tabella `fasce`
--

INSERT INTO `fasce` (`ID`, `Tipo`, `Descrizione`, `CostoKm`, `CostoGiornoLim`, `CostoSettimanaLim`, `CostoGiornoIllim`, `CostoSettimanaIllim`) VALUES
(1, 'C', '4 porte, due volumi', 0.15, 60, 320, 65, 330),
(2, 'D', 'Familiare, Station wagon', 0.18, 70, 380, 75, 385),
(3, 'F', 'Monovolume', 0.18, 75, 430, 76, 435),
(4, 'K', 'Berlina', 0.19, 76, 430, 80, 450),
(5, 'A', 'Sistematica, Ultramatica, Idromatica', 0.2, 90, 500, 95, 510);

-- --------------------------------------------------------

--
-- Struttura della tabella `modelli`
--

CREATE TABLE IF NOT EXISTS `modelli` (
`ID` int(11) NOT NULL,
  `Nome` varchar(35) NOT NULL,
  `Fascia` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dump dei dati per la tabella `modelli`
--

INSERT INTO `modelli` (`ID`, `Nome`, `Fascia`) VALUES
(1, 'Greased Lightning', 5),
(2, 'Opel Agila', 1),
(3, 'Fiat Panda', 1),
(4, 'Grande Punto', 2),
(5, 'Opel Corsa', 2),
(6, 'Lancia Musa', 3),
(7, 'Fiat Bravo', 4),
(8, 'Opel Astra SW', 3);

-- --------------------------------------------------------

--
-- Struttura della tabella `utenti`
--

CREATE TABLE IF NOT EXISTS `utenti` (
`ID` int(11) NOT NULL,
  `Username` varchar(20) NOT NULL,
  `Password` text NOT NULL,
  `TipoUtente` enum('AMMINISTRATORE','OPERATORE') NOT NULL,
  `Agenzia` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dump dei dati per la tabella `utenti`
--

INSERT INTO `utenti` (`ID`, `Username`, `Password`, `TipoUtente`, `Agenzia`) VALUES
(1, 'Simone', '1000:a7971af49b618f35717c12d2ae8604a7b3f0e78768499a37:9f909d01b939853db7a42c6fac099b946afb506d0b1355ef', 'AMMINISTRATORE', 4),
(2, 'Morena', '1000:d81856c9545d8f89187f6a282a189962f2120108affe59d0:22a4ba8b28829d736f6fb35dc6034bb4d3fe090274c44ef1', 'OPERATORE', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Agenzie`
--
ALTER TABLE `Agenzie`
 ADD PRIMARY KEY (`ID`), ADD UNIQUE KEY `Nome` (`Nome`);

--
-- Indexes for table `auto`
--
ALTER TABLE `auto`
 ADD PRIMARY KEY (`ID`), ADD UNIQUE KEY `Targa` (`Targa`);

--
-- Indexes for table `autodisponibili`
--
ALTER TABLE `autodisponibili`
 ADD PRIMARY KEY (`ID`), ADD UNIQUE KEY `Auto` (`Auto`,`DataInizio`);

--
-- Indexes for table `automanutenzione`
--
ALTER TABLE `automanutenzione`
 ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `clienti`
--
ALTER TABLE `clienti`
 ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `contrattiaperti`
--
ALTER TABLE `contrattiaperti`
 ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `contrattichiusi`
--
ALTER TABLE `contrattichiusi`
 ADD PRIMARY KEY (`ID`), ADD UNIQUE KEY `ContrattoAperto` (`ContrattoAperto`);

--
-- Indexes for table `fasce`
--
ALTER TABLE `fasce`
 ADD PRIMARY KEY (`ID`), ADD UNIQUE KEY `Tipo` (`Tipo`);

--
-- Indexes for table `modelli`
--
ALTER TABLE `modelli`
 ADD PRIMARY KEY (`ID`), ADD UNIQUE KEY `Nome` (`Nome`);

--
-- Indexes for table `utenti`
--
ALTER TABLE `utenti`
 ADD PRIMARY KEY (`ID`), ADD UNIQUE KEY `Username` (`Username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Agenzie`
--
ALTER TABLE `Agenzie`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `auto`
--
ALTER TABLE `auto`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `autodisponibili`
--
ALTER TABLE `autodisponibili`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=17;
--
-- AUTO_INCREMENT for table `automanutenzione`
--
ALTER TABLE `automanutenzione`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `clienti`
--
ALTER TABLE `clienti`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `contrattiaperti`
--
ALTER TABLE `contrattiaperti`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `contrattichiusi`
--
ALTER TABLE `contrattichiusi`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `fasce`
--
ALTER TABLE `fasce`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `modelli`
--
ALTER TABLE `modelli`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `utenti`
--
ALTER TABLE `utenti`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;

-- FOREIGN KEYS --
ALTER TABLE `auto` ADD FOREIGN KEY (`Modello`) REFERENCES `modelli`(`ID`) ON DELETE CASCADE;
ALTER TABLE `autodisponibili` ADD FOREIGN KEY (`Auto`) REFERENCES `auto`(`ID`) ON DELETE CASCADE;
ALTER TABLE `autodisponibili` ADD FOREIGN KEY (`Agenzia`) REFERENCES `Agenzie`(`ID`) ON DELETE CASCADE;
ALTER TABLE `automanutenzione` ADD FOREIGN KEY (`Auto`) REFERENCES `auto`(`ID`) ON DELETE CASCADE;
ALTER TABLE `contrattiaperti` ADD FOREIGN KEY (`FasciaMacchina`) REFERENCES `fasce`(`ID`) ON DELETE CASCADE;
ALTER TABLE `contrattiaperti` ADD FOREIGN KEY (`LuogoRest`) REFERENCES `Agenzie`(`ID`) ON DELETE SET NULL;
ALTER TABLE `contrattiaperti` ADD FOREIGN KEY (`Agenzia`) REFERENCES `Agenzie`(`ID`) ON DELETE CASCADE;
ALTER TABLE `contrattiaperti` ADD FOREIGN KEY (`Cliente`) REFERENCES `clienti`(`ID`) ON DELETE CASCADE;
ALTER TABLE `contrattiaperti` ADD FOREIGN KEY (`Auto`) REFERENCES `auto`(`ID`) ON DELETE SET NULL;
ALTER TABLE `contrattichiusi` ADD FOREIGN KEY (`ContrattoAperto`) REFERENCES `contrattiaperti`(`ID`) ON DELETE CASCADE;
ALTER TABLE `modelli` ADD FOREIGN KEY (`Fascia`) REFERENCES `fasce`(`ID`) ON DELETE CASCADE;
ALTER TABLE `utenti` ADD FOREIGN KEY (`Agenzia`) REFERENCES `Agenzie`(`ID`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
