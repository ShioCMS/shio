--
-- Base de Dados: `japode`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `jp_users`
--

CREATE TABLE `jp_users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Japode Users';
