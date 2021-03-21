/*insert into Water_Order(ID, FARM_ID, START_DATE_TIME, FLOW_DURATION, ORDER_STATUS)
values(1002, 112, parsedatetime('2021-03-19 09:30:00', 'yyyy-MM-dd hh:mm:ss')  , 10800000000000 , 0);

insert into Water_Order(ID, FARM_ID, START_DATE_TIME, FLOW_DURATION, ORDER_STATUS)
values(1003, 112, parsedatetime('2021-03-19 15:30:00.00', 'yyyy-MM-dd hh:mm:ss.SS')  , 16200000000000 , 0);

insert into Water_Order(ID, FARM_ID, START_DATE_TIME, FLOW_DURATION, ORDER_STATUS)
values(1004, 112, parsedatetime('2021-03-19 22:30:00.00', 'yyyy-MM-dd hh:mm:ss.SS')  , 12600000000000 , 0);*/

insert into Water_Order(ID, FARM_ID, START_DATE_TIME, FLOW_DURATION, ORDER_STATUS)
values(1003, 112, now() , 2000000000 , 0);
insert into Water_Order(ID, FARM_ID, START_DATE_TIME, FLOW_DURATION, ORDER_STATUS)
values(1004, 112,  DATEADD('SECOND',5, NOW())  , 5000000000 , 0);
insert into Water_Order(ID, FARM_ID, START_DATE_TIME, FLOW_DURATION, ORDER_STATUS)
values(1005, 112,  DATEADD('SECOND',10, NOW())  , 5000000000 , 0);