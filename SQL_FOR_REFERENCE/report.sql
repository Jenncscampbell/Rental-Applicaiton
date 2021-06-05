SELECT location, city
FROM vehicles v
WHERE v.location LIKE 'Cambie' AND v.city LIKE 'Vancouver' 
GROUP BY location, city;

SELECT v.location, v.city, v.vid, v.vtname, v.vstatus
FROM rentals r, vehicles v 
WHERE r.vid = v.vid AND r.fromDate <= to_date('2019/11/27', 'YYYY/MM/DD') AND to_date('2019/11/27', 'YYYY/MM/DD') <= r.toDate

SELECT v.location, v.city, 
SUM(case when vtname = 'SUV' then 1 else 0 end) AS suv_count,
SUM(case when vtname = 'Truck' then 1 else 0 end) AS truck_count,
SUM(case when vtname = 'Mid-sized' then 1 else 0 end) AS midsize_count,
SUM(case when vtname = 'Full-sized' then 1 else 0 end) AS fullsize_count,
SUM(case when vtname = 'Compact' then 1 else 0 end) AS compact_count,
SUM(case when vtname = 'Economy' then 1 else 0 end) AS economy_count,
COUNT(*) AS total_count
FROM rentals r, vehicles v 
WHERE r.vid = v.vid AND r.fromDate <= to_date('2019/11/27', 'YYYY/MM/DD') AND to_date('2019/11/27', 'YYYY/MM/DD') <= r.toDate
GROUP BY location, city;

SELECT v.location, v.city, 
SUM(case when vtname = 'SUV' then 1 else 0 end) AS suv_count,
SUM(case when vtname = 'Truck' then 1 else 0 end) AS truck_count,
SUM(case when vtname = 'Mid-sized' then 1 else 0 end) AS midsize_count,
SUM(case when vtname = 'Full-sized' then 1 else 0 end) AS fullsize_count,
SUM(case when vtname = 'Compact' then 1 else 0 end) AS compact_count,
SUM(case when vtname = 'Economy' then 1 else 0 end) AS economy_count,
COUNT(*) AS total_count
FROM rentals r, vehicles v 
WHERE r.vid = v.vid AND v.location LIKE 'Cambie' AND v.city LIKE 'Vancouver' AND r.fromDate <= to_date('2019/11/27', 'YYYY/MM/DD') AND to_date('2019/11/27', 'YYYY/MM/DD') <= r.toDate
GROUP BY location, city;

SELECT COUNT(*) AS total_count
FROM rentals r, vehicles v
WHERE r.vid = v.vid AND r.fromDate <= to_date('2019/11/27', 'YYYY/MM/DD') AND to_date('2019/11/27', 'YYYY/MM/DD') <= r.toDate
GROUP BY location, city;


SELECT COUNT(*) AS vehicle_count, SUM(ret.return_value) AS revenue_total 
FROM returns ret, rentals ren, vehicles v
WHERE ret.rid = ren.rid AND v.vid = ren.vid AND ren.return_date = to_date('2019/11/27', 'YYYY/MM/DD')

SELECT v.location, v.city, v.vid, v.vtname, v.vstatus 
FROM rentals ren, vehicles v , returns ret
WHERE ren.vid = v.vid AND ret.rid = ren.rid AND ret.return_date =  to_date('2019/11/27', 'YYYY/MM/DD');

SELECT v.location, v.city,
SUM(case when vtname = 'SUV' then 1 else 0 end) AS suv_count,
SUM(case when vtname = 'SUV' then ret.return_value else 0 end) AS suv_subtotal,
SUM(case when vtname = 'Truck' then 1 else 0 end) AS truck_count,
SUM(case when vtname = 'Truck' then ret.return_value else 0 end) AS truck_subtotal,
SUM(case when vtname = 'Mid-sized' then 1 else 0 end) AS midsize_count,
SUM(case when vtname = 'Mid-sized' then ret.return_value else 0 end) AS midsize_subtotal,
SUM(case when vtname = 'Full-sized' then 1 else 0 end) AS fullsize_count,
SUM(case when vtname = 'Full-sized' then ret.return_value else 0 end) AS fullsize_subtotal,
SUM(case when vtname = 'Compact' then 1 else 0 end) AS compact_count,
SUM(case when vtname = 'Compact' then ret.return_value else 0 end) AS compact_subtotal,
SUM(case when vtname = 'Economy' then 1 else 0 end) AS economy_count,
SUM(case when vtname = 'Economy' then ret.return_value else 0 end) AS economy_subtotal,
COUNT(*) AS branch_vehicle_count,
SUM(ret.return_value) AS branch_subtotal
FROM returns ret, rentals ren, vehicles v 
WHERE ret.rid = ren.rid AND v.vid = ren.vid AND ret.return_date = to_date('2019/11/10', 'YYYY/MM/DD')
GROUP BY location, city;

SELECT v.location, v.city,
SUM(case when vtname = 'SUV' then 1 else 0 end) AS suv_count,
SUM(case when vtname = 'SUV' then ret.return_value else 0 end) AS suv_subtotal,
SUM(case when vtname = 'Truck' then 1 else 0 end) AS truck_count,
SUM(case when vtname = 'Truck' then ret.return_value else 0 end) AS truck_subtotal,
SUM(case when vtname = 'Mid-sized' then 1 else 0 end) AS midsize_count,
SUM(case when vtname = 'Mid-sized' then ret.return_value else 0 end) AS midsize_subtotal,
SUM(case when vtname = 'Full-sized' then 1 else 0 end) AS fullsize_count,
SUM(case when vtname = 'Full-sized' then ret.return_value else 0 end) AS fullsize_subtotal,
SUM(case when vtname = 'Compact' then 1 else 0 end) AS compact_count,
SUM(case when vtname = 'Compact' then ret.return_value else 0 end) AS compact_subtotal,
SUM(case when vtname = 'Economy' then 1 else 0 end) AS economy_count,
SUM(case when vtname = 'Economy' then ret.return_value else 0 end) AS economy_subtotal,
COUNT(*) AS branch_vehicle_count,
SUM(ret.return_value) AS branch_subtotal
FROM returns ret, rentals ren, vehicles v 
WHERE ret.rid = ren.rid AND v.vid = ren.vid AND v.location LIKE 'Cambie' AND v.city LIKE 'Vancouver' AND ret.return_date = to_date('2019/11/27', 'YYYY/MM/DD')
GROUP BY location, city;








