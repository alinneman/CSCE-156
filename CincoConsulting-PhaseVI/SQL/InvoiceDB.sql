start transaction;

drop table if exists Purchase;
drop table if exists Invoice;
drop table if exists Product;
drop table if exists Customer;
drop table if exists Email;
drop table if exists Person;
drop table if exists Address;



create table Address(
addressId int not null primary key auto_increment,
street varchar(255),
city varchar(255),
state varchar(255),
zip varchar(255),
country varchar(255)
);


create table Person(
personId int not null primary key auto_increment,
personCode varchar(255),
lastName varchar(255),
firstName varchar(255),
addressId int not null,
foreign key (addressId) references Address(addressId)
);

create table Email(
emailId int not null primary key auto_increment,
personId int not null,
email varchar(255),
foreign key (personId) references Person(personId)
);


create table Customer(
customerId int not null primary key auto_increment,
customerCode varchar(255),
customerType varchar(255),
contactPersonId int,
customerName varchar(255),
addressId int not null,
foreign key (contactPersonId) references Person(personId),
foreign key (addressId) references Address(addressId)
);


create table Product(
productId int not null primary key auto_increment,
productCode varChar(255),
productType varchar(255),
productName varchar(255),
consultantPersonId int,
hourlyFee double,
pricePerUnit double,
serviceFee double,
annualLicenseFee double,
foreign key (consultantPersonId) references Person(personId)
);


create table Invoice(
invoiceId int not null primary key auto_increment,
invoiceCode varchar(255),
customerId int not null,
salespersonId int,
foreign key (customerId) references Customer(customerId),
foreign key (salespersonId) references Person(personId)
);

create table Purchase(
purchaseId int not null primary key auto_increment,
invoiceId int,
productId int,
units double,
hours double,
dateStart varchar(255),
dateEnd varchar(255),
foreign key (invoiceId) references Invoice(invoiceId),
foreign key (productId) references Product(productId)
);


insert into Address (addressId, street, city, state, zip, country) values (1, "5455 Clemons Plaza", "Indianapolis", "Indiana", "46221", "United States");
insert into Address (addressId, street, city, state, zip, country) values (2, "48 Badeau Drive", "Flushing", "New York", "11388", "United States");
insert into Address (addressId, street, city, state, zip, country) values (3, "98077 3rd Road", "Saint Louis", "Missouri", "63131", "United States");
insert into Address (addressId, street, city, state, zip, country) values (4, "4320 Green Ridge Court", "Canton", "Ohio", "44720", "United States");
insert into Address (addressId, street, city, state, zip, country) values (5, "792 Dayton Avenue,Houston", "Houston", "Texas", "77201", "United States");
insert into Address (addressId, street, city, state, zip, country) values (6, "198 Rockefeller Center", "Indianapolis", "Indiana", "46221", "United States");
insert into Address (addressId, street, city, state, zip, country) values (7, "2 Claremont Road", "Fort Pierce", "Florida", "34981", "United States");
insert into Address (addressId, street, city, state, zip, country) values (8, "885 Debs Way", "Appleton", "Wisconsin", "54915", "United States");
insert into Address (addressId, street, city, state, zip, country) values (9, "40 Lerdahl Avenue", "Asheville", "North Carolina", "28815", "United States");
insert into Address (addressId, street, city, state, zip, country) values (10, "23 Haas Parkway", "Chicago", "Illinois", "60652", "United States");
insert into Address (addressId, street, city, state, zip, country) values (11, "302 Eggendart Place", "Amarillo", "Texas", "79171", "United States");
insert into Address (addressId, street, city, state, zip, country) values (12, "81 Harper Drive", "Lansing", "Michigan", "48912", "United States");
insert into Address (addressId, street, city, state, zip, country) values (13, "7 Muir Trail", "Flushing", "New York", "11388", "United States");
insert into Address (addressId, street, city, state, zip, country) values (14, "4588 Cherokee Park", "Washington", "District of Columbia", "20525", "United States");
insert into Address (addressId, street, city, state, zip, country) values (15, "958 Homewood Lane", "Washington", "District of Columbia", "20525", "United States");
insert into Address (addressId, street, city, state, zip, country) values (16, "7105 Grim Pass", "Amarillo", "Texas", "79171", "United States");
insert into Address (addressId, street, city, state, zip, country) values (17, "524 Barby Center", "Lansing", "Michigan", "48912", "United States");
insert into Address (addressId, street, city, state, zip, country) values (18, "49420 Center Circle", "Flushing", "New York", "11388", "United States");
insert into Address (addressId, street, city, state, zip, country) values (19, "000 Arrowood Trail", "Saint Louis", "Missouri", "63131", "United States");
insert into Address (addressId, street, city, state, zip, country) values (20, "6684 Paget Crossing", "Canton", "Ohio", "44720", "United States");
insert into Address (addressId, street, city, state, zip, country) values (21, "4 Beilfuss Park", "Houston", "Texas", "77201", "United States");
insert into Address (addressId, street, city, state, zip, country) values (22, "105 Annamark Road", "Indianapolis", "Indiana", "46221", "United States");
insert into Address (addressId, street, city, state, zip, country) values (23, "60 Logan Crossing", "Fort Pierce", "Florida", "34981", "United States");
insert into Address (addressId, street, city, state, zip, country) values (24, "230 Melody Street", "Appleton", "Wisconsin", "54915", "United States");
insert into Address (addressId, street, city, state, zip, country) values (25, "59 Golf View Court", "Asheville", "North Carolina", "28815", "United States");
insert into Address (addressId, street, city, state, zip, country) values (26, "007 Browning Alley", "Chicago", "Illinois", "60652", "United States");
insert into Address (addressId, street, city, state, zip, country) values (27, "546 Pearson Place", "Washington", "District of Columbia", "20525", "United States");





insert into Person (personId, personCode, lastName, firstName, addressId) values (1, "aqwqj", "Tettersell", "Margaretta",1);
insert into Person (personId, personCode, lastName, firstName, addressId) values (2, "DyW2", "Halls", "Homerus", 2);
insert into Person (personId, personCode, lastName, firstName, addressId) values (3, "h7a9p6", "Ridpath", "Arlyne", 3);
insert into Person (personId, personCode, lastName, firstName, addressId) values (4, "mengk", "Highnam", "Ansell", 4);
insert into Person (personId, personCode, lastName, firstName, addressId) values (5, "obnd", "Geffe", "Jaynell", 5);
insert into Person (personId, personCode, lastName, firstName, addressId) values (6, "31nn", "Schimpke", "Charmine", 6);
insert into Person (personId, personCode, lastName, firstName, addressId) values (7, "CkP8", "Swalough", "Maxi", 7);
insert into Person (personId, personCode, lastName, firstName, addressId) values (8, "d1w2i", "Mordin", "Rosita", 8);
insert into Person (personId, personCode, lastName, firstName, addressId) values (9, "ar2r", "Devon", "Andrej", 9);
insert into Person (personId, personCode, lastName, firstName, addressId) values (10, "A6Fr", "McLaren", "Marcellus", 10);
insert into Person (personId, personCode, lastName, firstName, addressId) values (11, "1mGf", "Fried", "Clifford", 11);
insert into Person (personId, personCode, lastName, firstName, addressId) values (12, "R27y", "Walliker", "Brok", 12);
insert into Person (personId, personCode, lastName, firstName, addressId) values (13, "gnc7tm", "Notman", "Cindie", 13);
insert into Person (personId, personCode, lastName, firstName, addressId) values (14, "I57j", "Luxmoore", "Leda", 14);
insert into Person (personId, personCode, lastName, firstName, addressId) values (15, "HrFm", "Grant", "Jock", 15);
insert into Person (personId, personCode, lastName, firstName, addressId) values (16, "fh1R", "Seignior", "Dennis", 16);
insert into Person (personId, personCode, lastName, firstName, addressId) values (17, "vu29h", "Baroche", "Dex", 17);



insert into Email (emailId, personId, email) values (1, 2, "hhalls1@google.com.br");
insert into Email (emailId, personId, email) values (2, 3, "aridpath2@digg.com");
insert into Email (emailId, personId, email) values (3, 4, "ahighnam3@opera.com");
insert into Email (emailId, personId, email) values (4, 5, "jgeffe4@washingtonpost.com");
insert into Email (emailId, personId, email) values (5, 7, "mswalough6@1688.com");
insert into Email (emailId, personId, email) values (6, 8, "rmordin7@acquirethisname.com");
insert into Email (emailId, personId, email) values (7, 9, "adevon8@naver.com");
insert into Email (emailId, personId, email) values (8, 10, "mmclaren9@blogs.com");
insert into Email (emailId, personId, email) values (9, 12, "bwallikerb@senate.gov");
insert into Email (emailId, personId, email) values (10, 13, "cnotmanc@posterous.com");
insert into Email (emailId, personId, email) values (11, 11, "lluxmoored@gov.uk");
insert into Email (emailId, personId, email) values (12, 17,"dbarocheg@vk.com");
insert into Email (emailId, personId, email) values (13, 2, "deckthehalls1@google.com.br");





insert into Customer (customerId, customerCode, customerType, contactPersonId, customerName, addressId) values (1, "C001", "C", 4, "Generic Company", 18);
insert into Customer (customerId, customerCode, customerType, contactPersonId, customerName, addressId) values (2, "C002", "G", 16, "Humphrey", 19);
insert into Customer (customerId, customerCode, customerType, contactPersonId, customerName, addressId) values (3, "C003", "G", 6, "Halimedas", 20);
insert into Customer (customerId, customerCode, customerType, contactPersonId, customerName, addressId) values (4, "C004", "G", 8, "Odos", 21);
insert into Customer (customerId, customerCode, customerType, contactPersonId, customerName, addressId) values (5, "C005", "C", 17, "Vittorio Incorporated", 22);
insert into Customer (customerId, customerCode, customerType, contactPersonId, customerName, addressId) values (6, "C006", "G", 9, "Ebony", 23);
insert into Customer (customerId, customerCode, customerType, contactPersonId, customerName, addressId) values (7, "C007", "C", 1, "Alvis and Sons", 24);
insert into Customer (customerId, customerCode, customerType, contactPersonId, customerName, addressId) values (8, "C008", "C", 5, "Millson Firm", 25);
insert into Customer (customerId, customerCode, customerType, contactPersonId, customerName, addressId) values (9, "C009", "C", 13, "Haggerty", 26);
insert into Customer (customerId, customerCode, customerType, contactPersonId, customerName, addressId) values (10, "C010", "C", 3, "Diamant Corp.", 27);



insert into Product (productId, productCode, productType, productName, pricePerUnit) values (1, "D7Ly", "E", "Meedoo", 827.68);
insert into Product (productId, productCode, productType, productName, pricePerUnit) values (2, "Re23", "E", "Feedmix", 680.27);
insert into Product (productId, productCode, productType, productName, pricePerUnit) values (3, "SyJ0", "E", "Ooba", 28.86);
insert into Product (productId, productCode, productType, productName, pricePerUnit) values (4, "Zz97", "E", "Skimia", 534.66);
insert into Product (productId, productCode, productType, productName, serviceFee, annualLicenseFee) values (5, "KyHm", "L", "Swaniawski", 524.04, 9848.79);
insert into Product (productId, productCode, productType, productName, serviceFee, annualLicenseFee) values (6, "4gQu", "L", "Dibbert", 171.44, 7664.82);
insert into Product (productId, productCode, productType, productName, serviceFee, annualLicenseFee) values (7, "AbE3", "L", "Rempel-Aufderhar", 92.34, 5205.29);
insert into Product (productId, productCode, productType, productName, serviceFee, annualLicenseFee) values (8, "5dEj", "L", "Kunze", 991.43, 1095.66);
insert into Product (productId, productCode, productType, productName, serviceFee, annualLicenseFee) values (9, "7cRc", "L", "Parker and Sons", 508.56, 4698.97);
insert into Product (productId, productCode, productType, productName, serviceFee, annualLicenseFee) values (10, "OrOp", "L", "Tillman", 968.18, 103.94);
insert into Product (productId, productCode, productType, productName, serviceFee, annualLicenseFee) values (11, "7sMo", "L", "Bergnaum", 56.57, 9821.61);
insert into Product (productId, productCode, productType, productName, consultantPersonId, hourlyFee) values (12, "YgMl", "C", "Daugherty Group", 10, 9790.86);
insert into Product (productId, productCode, productType, productName, consultantPersonId, hourlyFee) values (13, "Am70", "C", "Feil", 3, 3316.17);
insert into Product (productId, productCode, productType, productName, consultantPersonId, hourlyFee) values (14, "F7Iu", "C", "Hessel", 6, 2956.13);



insert into Invoice (invoiceId, invoiceCode, customerId, salespersonId) values (1, "INV007", 6, 2);
insert into Invoice (invoiceId, invoiceCode, customerId, salespersonId) values (2, "INV008", 3, 7); 
insert into Invoice (invoiceId, invoiceCode, customerId, salespersonId) values (3, "INV009", 4, 10);
insert into Invoice (invoiceId, invoiceCode, customerId, salespersonId) values (4, "INV010", 1, 11);
insert into Invoice (invoiceId, invoiceCode, customerId, salespersonId) values (5, "INV011", 4, 12);
insert into Invoice (invoiceId, invoiceCode, customerId, salespersonId) values (6, "INV012", 1, 14);
insert into Invoice (invoiceId, invoiceCode, customerId, salespersonId) values (7, "INV013", 3, 15);



insert into Purchase (purchaseId, invoiceId, productId, units) values (1, 1, 1, 21);
insert into Purchase (purchaseId, invoiceId, productId, dateStart, dateEnd) values (2, 1, 7, "2012-06-07", "2013-06-07");
insert into Purchase (purchaseId, invoiceId, productId, units) values (3, 2, 4, 13);
insert into Purchase (purchaseId, invoiceId, productId, hours) values (4, 2, 14, 10);
insert into Purchase (purchaseId, invoiceId, productId, units) values (5, 2, 3, 12);
insert into Purchase (purchaseId, invoiceId, productId, units) values (6, 3, 3, 8);
insert into Purchase (purchaseId, invoiceId, productId, dateStart, dateEnd) values (7, 3, 9, "2009-01-15", "2018-12-31");
insert into Purchase (purchaseId, invoiceId, productId, units) values (8, 4, 2, 117);
insert into Purchase (purchaseId, invoiceId, productId, dateStart, dateEnd) values (9, 4, 5, "2012-07-04", "2018-11-23");
insert into Purchase (purchaseId, invoiceId, productId, dateStart, dateEnd) values (10, 4, 9, "2015-04-11", "2017-12-31");
insert into Purchase (purchaseId, invoiceId, productId, dateStart, dateEnd) values (11, 5, 11, "2013-01-15", "2014-12-31");
insert into Purchase (purchaseId, invoiceId, productId, hours) values (12, 6, 13, 24);
insert into Purchase (purchaseId, invoiceId, productId, dateStart, dateEnd) values (13, 6, 8, "2017-01-01", "2019-02-20");
insert into Purchase (purchaseId, invoiceId, productId, hours) values (14, 7, 12, 34);
insert into Purchase (purchaseId, invoiceId, productId, hours) values (15, 7, 14, 17);
insert into Purchase (purchaseId, invoiceId, productId, units) values (16, 7, 1, 19);


commit;