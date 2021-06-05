drop table returns;
drop table rentals;
drop table reservations;
drop table vehicles;
drop table vehicletypes;
drop table customers;

create table vehicletypes
	(vtname    varchar2(20) not null,
	 features  varchar2(20),
	 drate     float,
	 wrate     float,
	 hrate     float,
	 wirate    float,
	 dirate    float,
	 krate     float,
	 primary key (vtname)
);
insert into vehicletypes
values('Economy', 'Hybrid', '24',
'168', '1', '14', '2', '3');
insert into vehicletypes
values('Compact', 'Hybrid', '24',
'168', '1', '14', '2', '3');
insert into vehicletypes
values('Mid-sized', 'Electric', '30',
'190', '1.5', '16', '3', '1');
insert into vehicletypes
values('Standard', 'Gas', '240',
'1680', '10', '40', '5', '5');
insert into vehicletypes
values('Full-sized', 'Gas', '240',
'1680', '10', '40', '5', '5');
insert into vehicletypes
values('Truck', 'Gas', '240',
'1680', '10', '40', '5', '5');
insert into vehicletypes
values('SUV', 'Hybrid', '24',
'168', '1', '14', '2', '1');


create table vehicles
	 (vid       integer not null,
	  vlicense  varchar2(20),
	  make      varchar2(20),
	  year      integer,
	  color     varchar2(20),
	  odometer  float,
	  vstatus   varchar2(20),
	  vtname    varchar2(20),
	  location  varchar2(20),
	  city      varchar2(20),
	  foreign key  (vtname) references vehicletypes
		ON DELETE CASCADE,
		primary key (vid)
  );

insert into vehicles
values ('55550', 'KLM2-30', 'Toyota', '2019', 'Blue', '2000',
'For rent', 'Truck', 'Cambie', 'Vancouver');
insert into vehicles
values ('55551', 'KLM2-31', 'Toyota', '2019', 'Blue', '2000',
'For rent', 'Truck', 'Cambie', 'Vancouver');
insert into vehicles
values ('55552', 'KLM2-32', 'Ford', '2019', 'Blue', '2000',
'For rent', 'SUV', 'Cambie', 'Vancouver');
insert into vehicles
values ('55553', 'KLM2-33', 'Ford', '2019', 'Blue', '2000',
'For rent', 'SUV', 'Cambie', 'Vancouver');
insert into vehicles
values ('55554', 'KLM2-33', 'Honda', '2019', 'Blue', '2000',
'For rent', 'Economy', 'Cambie', 'Vancouver');
insert into vehicles
values ('55555', 'KLM2-35', 'Honda', '2019', 'Blue', '2000',
'For rent', 'Economy', 'Cambie', 'Vancouver');

insert into vehicles
values ('33330', 'LLM2-30', 'Toyota', '2019', 'Blue', '2000',
'For rent', 'Truck', '1st Ave', 'Calgary');
insert into vehicles
values ('33331', 'LLM2-31', 'Toyota', '2019', 'Silver', '2000',
'For rent', 'Truck', '1st Ave', 'Calgary');
insert into vehicles
values ('33332', 'LLM2-32', 'Ford', '2019', 'Silver', '2000',
'For rent', 'SUV', '1st Ave', 'Calgary');
insert into vehicles
values ('33333', 'LLM2-33', 'Ford', '2019', 'Green', '2000',
'For rent', 'SUV', '1st Ave', 'Calgary');
insert into vehicles
values ('33334', 'LLM2-33', 'Honda', '2019', 'Green', '2000',
'For rent', 'Economy', '1st Ave', 'Calgary');
insert into vehicles
values ('33335', 'LLM2-35', 'Honda', '2019', 'Green', '2000',
'For rent', 'Economy', '1st Ave', 'Calgary');

insert into vehicles
values ('44440', 'HG45-30', 'Ford', '2018', 'Red', '3000',
'For rent', 'Standard', 'Main Street', 'Vancouver');
insert into vehicles
values ('44441', 'HG45-31', 'Ford', '2018', 'Red', '3000',
'For rent', 'Standard', 'Main Street', 'Vancouver');
insert into vehicles
values ('44442', 'HG45-32', 'Honda', '2018', 'Blue', '3000',
'For rent', 'Compact', 'Main Street', 'Vancouver');
insert into vehicles
values ('44443', 'HG45-33', 'Honda', '2018', 'Blue', '3000',
'For rent', 'Compact', 'Main Street', 'Vancouver');
insert into vehicles
values ('44444', 'HG45-34', 'Honda', '2018', 'Blue', '3000',
'For rent', 'Compact', 'Main Street', 'Vancouver');
insert into vehicles
values ('44445', 'HG45-35', 'Tesla', '2018', 'Blue', '3000',
'For rent', 'Mid-sized', 'Main Street', 'Vancouver');
insert into vehicles
values ('44446', 'HG45-36', 'Ford', '2018', 'Green', '3000',
'For rent', 'Mid-sized', 'Main Street', 'Vancouver');
insert into vehicles
values ('44447', 'HG45-37', 'Ford', '2018', 'Green', '3000',
'For rent', 'Full-sized', 'Main Street', 'Vancouver');
insert into vehicles
values ('44448', 'HG45-38', 'Ford', '2018', 'Black', '3000',
'For rent', 'Full-sized', 'Main Street', 'Vancouver');



create table customers
	(dlicense varchar2(20) not null,
	  cellphone integer not null,
	  cname varchar2(20) not null,
	  addr varchar2(50),
		primary key (dlicense)
  );

insert into customers
	values ('123232', '8889999', 'Jenn', '123 3rd Street');
insert into customers
	values ('747473', '1112222', 'John','333 5th Ave');
insert into customers 
	values  ('098765', '1290909', 'Debbie', '214 Help St');	
insert into customers 
	values  ('098766', '1290910', 'Mark', '214 Please St');
insert into customers 
	values  ('098743', '1290911', 'Stove', '310 Moose St');
insert into customers 
	values  ('098750', '1290911', 'Quasimoto', '311 Also Moose St');	

	create table reservations (
		confNo integer not null,
		vid integer not null,
		vtname varchar2(20),
		dlicense varchar2(20),
		fromDate date,
		toDate date,
		foreign key (vid) references vehicles
		ON DELETE CASCADE,
		foreign key (vtname) references vehicletypes
		ON DELETE CASCADE,
		foreign key (dlicense) references customers
		ON DELETE CASCADE,
		primary key (confNo)
	);

	insert into reservations
		values ('54321','55550','Truck', '123232', to_date('2019/11/01', 'YYYY/MM/DD'),
		to_date('2019/11/10', 'YYYY/MM/DD'));
	
		insert into reservations
		values ('11111','55550','Truck', '747473', to_date('2019/11/22', 'YYYY/MM/DD'),
		to_date('2019/11/26', 'YYYY/MM/DD'));

		insert into reservations
		values ('11112','55551','Truck', '098765', to_date('2019/11/21', 'YYYY/MM/DD'),
		to_date('2019/11/28', 'YYYY/MM/DD HH24 MI'));

		insert into reservations
		values ('11113','44444','Compact', '098766', to_date('2019/11/27', 'YYYY/MM/DD'),
		to_date('2019/11/28', 'YYYY/MM/DD HH24 MI'));

		insert into reservations
		values ('11114','44446','Mid-sized', '123232', to_date('2019/04/11', 'YYYY/MM/DD'),
		to_date('2019/04/21', 'YYYY/MM/DD HH24 MI'));

		insert into reservations
		values ('11115','44448','Full-sized', '747473', to_date('2019/11/24', 'YYYY/MM/DD'),
		to_date('2019/11/28', 'YYYY/MM/DD HH24 MI'));

		insert into reservations
		values ('11116', '33335','Economy', '123232', to_date('2019/11/27', 'YYYY/MM/DD'),
		to_date('2019/11/28', 'YYYY/MM/DD HH24 MI'));

		insert into reservations
		values ('11117','55553','SUV', '098750', to_date('2019/11/01', 'YYYY/MM/DD'),
		to_date('2019/11/28', 'YYYY/MM/DD HH24 MI'));

		insert into reservations
		values ('11118','44448','Full-sized', '747473', to_date('2019/11/20', 'YYYY/MM/DD'),
		to_date('2019/11/23', 'YYYY/MM/DD HH24 MI'));

		insert into reservations
		values ('11119','44448','Full-sized', '747473', to_date('2019/11/01', 'YYYY/MM/DD'),
		to_date('2019/11/10', 'YYYY/MM/DD HH24 MI'));

		insert into reservations
		values ('20000','44448','Full-sized', '747473', to_date('2019/12/01', 'YYYY/MM/DD'),
		to_date('2019/12/10', 'YYYY/MM/DD HH24 MI'));

		create table rentals (
			rid integer not null,
			vid integer not null,
			dlicense varchar2(20),
			fromDate date,
			toDate date,
			odometer float,
			cardName varchar2(20),
			cardNo integer,
			ExpDate date,
			confNo integer unique,
			foreign key (dlicense) references customers
			ON DELETE CASCADE,
			foreign key (confNo) references reservations
			ON DELETE CASCADE,
			foreign key (vid) references vehicles
			ON DELETE CASCADE,
			primary key (rid)
		);

		insert into rentals
		values ('100','55550', '123232', to_date('2019/11/24', 'YYYY/MM/DD'),
		to_date('2019/11/28', 'YYYY/MM/DD'), '200', 'Mastercard', '100009', to_date('2019/12/31', 'YYYY/MM/DD'),
		'54321');
	
		insert into rentals
		values ('101', '55550', '747473', to_date('2019/11/22', 'YYYY/MM/DD'),
		to_date('2019/11/27', 'YYYY/MM/DD'), '200', 'Mastercard', '100009', to_date('2019/12/31', 'YYYY/MM/DD'),
		'11111');

		insert into rentals
		values ('102','55551', '098765', to_date('2019/11/21', 'YYYY/MM/DD'),
		to_date('2019/11/28', 'YYYY/MM/DD HH24 MI'), '200', 'Mastercard', '100009', to_date('2019/12/31', 'YYYY/MM/DD'),
		'11112');

		insert into rentals
		values ('103','44444', '098766', to_date('2019/11/27', 'YYYY/MM/DD'),
		to_date('2019/11/28', 'YYYY/MM/DD HH24 MI'), '200', 'Mastercard', '100009', to_date('2019/12/31', 'YYYY/MM/DD'),
		'11113');

		insert into rentals
		values ('104','44446', '123232', to_date('2019/04/11', 'YYYY/MM/DD'),
		to_date('2019/04/21', 'YYYY/MM/DD HH24 MI'), '200', 'Mastercard', '100009', to_date('2019/12/31', 'YYYY/MM/DD'),
		'11114');

		insert into rentals
		values ('105','44448', '747473', to_date('2019/11/24', 'YYYY/MM/DD'),
		to_date('2019/11/28', 'YYYY/MM/DD HH24 MI'), '200', 'Mastercard', '100009', to_date('2019/12/31', 'YYYY/MM/DD'),
		'11115');

		insert into rentals
		values ('106', '33335', '123232', to_date('2019/11/27', 'YYYY/MM/DD'),
		to_date('2019/11/28', 'YYYY/MM/DD HH24 MI'), '200', 'Mastercard', '100009', to_date('2019/12/31', 'YYYY/MM/DD'),
		'11116');

		insert into rentals
		values ('107','55553', '098750', to_date('2019/11/01', 'YYYY/MM/DD'),
		to_date('2019/11/28', 'YYYY/MM/DD HH24 MI'), '200', 'Mastercard', '100009', to_date('2019/12/31', 'YYYY/MM/DD'),
		'11117');

		insert into rentals
		values ('108','44448', '747473', to_date('2019/11/20', 'YYYY/MM/DD'),
		to_date('2019/11/23', 'YYYY/MM/DD HH24 MI'), '200', 'Mastercard', '100009', to_date('2019/12/31', 'YYYY/MM/DD'),
		'11118');

		insert into rentals
		values ('109','44448', '747473', to_date('2019/11/01', 'YYYY/MM/DD'),
		to_date('2019/11/10', 'YYYY/MM/DD HH24 MI'), '200', 'Mastercard', '100009', to_date('2019/12/31', 'YYYY/MM/DD'),
		'11119');




		create table returns
			(rid integer not null,
			   return_date date,
			   return_odometer float,
			   return_fulltank varchar2(4),
			   return_value float,
			   foreign key (rid) references rentals
				 ON DELETE CASCADE,
				 primary key (rid)
		);
		insert into returns 
		values('109', to_date('2019/11/10', 'YYYY/MM/DD'), '50001', 'full', '20000.98');
		insert into returns 
		values('108', to_date('2019/11/23', 'YYYY/MM/DD'), '50001', 'full', '898.20');
		insert into returns 
		values('104', to_date('2019/04/21', 'YYYY/MM/DD'), '50001', 'full', '9898.30');
		insert into returns 
		values('101', to_date('2019/11/27', 'YYYY/MM/DD'), '50001', 'full', '9898.30');