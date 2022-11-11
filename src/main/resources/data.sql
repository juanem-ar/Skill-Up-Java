insert into wallet.roles(id, name)
	select 1, 'ADMIN'
where not exists
	(select * from roles where id = 1 and name = 'ADMIN');

	
insert into roles(id, name)
		select 2, 'USER'
where not exists
	(select * from roles where id = 2 and name = 'USER');