CREATE DATABASE IF NOT EXISTS `grab` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `grab`;

CREATE TABLE IF NOT EXISTS `folders` (
`folder_id` int(20) NOT NULL,
  `user_id` int(10) NOT NULL,
  `folder_name` varchar(50) NOT NULL,
  `folder_path` varchar(300) NOT NULL,
  `image_resize` int(5) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=19 ;

INSERT INTO `folders` (`folder_id`, `user_id`, `folder_name`, `folder_path`, `image_resize`) VALUES
(10, 4, 'Inbox - print', '\\\\gemt02S\\Inbox GCET\\In-Box Panittha.k', 1),
(11, 4, 'EVO_32_CSI', '\\\\gemt02S\\GCET_Project\\NBS Technology\\01_OPERATIONS\\01_EVO\\09_Media\\02_EVO_32_CSI', 1),
(12, 4, 'BBDP', '\\\\gemt02S\\GCET_Project\\Bombardier\\01_OPERATIONS\\09_Media', 1);

CREATE TABLE IF NOT EXISTS `users` (
`user_id` int(10) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `current_folder` int(2) NOT NULL,
  `upload_immediately` int(1) NOT NULL,
  `printer` varchar(100) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

INSERT INTO `users` (`user_id`, `user_name`, `password`, `current_folder`, `upload_immediately`, `printer`) VALUES
(4, 'john.d', '112', 1, 1, '');


ALTER TABLE `folders`
 ADD PRIMARY KEY (`folder_id`);

ALTER TABLE `users`
 ADD PRIMARY KEY (`user_id`), ADD UNIQUE KEY `user_id` (`user_id`), ADD UNIQUE KEY `user_name` (`user_name`);


ALTER TABLE `folders`
MODIFY `folder_id` int(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=19;
ALTER TABLE `users`
MODIFY `user_id` int(10) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;