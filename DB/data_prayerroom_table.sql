CREATE TABLE `data_prayerroom` (
  `ID` int NOT NULL,
  `name_english` varchar(200) DEFAULT NULL,
  `type_prayerroom` int DEFAULT NULL,
  `address_english` varchar(200) DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `info_phonenumber` varchar(200) DEFAULT NULL,
  `info_permanent` tinyint DEFAULT NULL,
  `info_genderdivision` tinyint DEFAULT NULL,
  `info_quran` tinyint DEFAULT NULL,
  `info_carpet` tinyint DEFAULT NULL,
  `info_kibla` tinyint DEFAULT NULL,
  `info_feetwashing` tinyint DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3