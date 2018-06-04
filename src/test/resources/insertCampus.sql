insert into campussen(naam,straat,huisNr,postCode,gemeente)
	values('testNaam','testStraat','testHuisNr','testPostCode','testGemeente');
insert into campussentelefoonnrs(campusid,nummer,fax,opmerking)
	values((select id from campussen where naam='testNaam'),'1',false,'testOpmerking');