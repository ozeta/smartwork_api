create table requests
(
	id int auto_increment
		primary key,
	user varchar(50) null,
	approver varchar(50) null,
	date timestamp null,
	status varchar(50) null
)
comment 'the requests table';

