package ca.ubc.cs304.database;

import java.sql.*;
import java.util.ArrayList;
import ca.ubc.cs304.model.*;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	// private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	
	private Connection connection = null;
	
	public DatabaseConnectionHandler() {
		try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}


	public boolean login(String username, String password) {
		try {
			if (connection != null) {
				connection.close();
			}

			connection = DriverManager.getConnection(ORACLE_URL, username, password);
			connection.setAutoCommit(false);

			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}
	
	public void close() throws Exception {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
            throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public CompanyRentalReportModel generateDailyRentalAllBranches(String date) {
		ArrayList<BranchRentalReportModel> result = new ArrayList<>();
		Date sqlDate = Date.valueOf(date);
		ArrayList<ReportVehicleModel> vehicles = new ArrayList<>();
		try {
			PreparedStatement stmt = connection.prepareStatement(" SELECT v.location, v.city, v.vid, v.vtname, v.vstatus " +
					"FROM rentals r, vehicles v " +
					"WHERE r.vid = v.vid AND r.fromDate <= ? AND ? <= r.toDate ");
			stmt.setDate(1, sqlDate);
			stmt.setDate(2,sqlDate);
			stmt.executeUpdate();
			ResultSet rs = stmt.executeQuery();
			// System.out.println("made it 2");

			while(rs.next()) {
				ReportVehicleModel model = new ReportVehicleModel
						(rs.getString("location"),
								rs.getString("city"),
								rs.getInt("vid"),
								rs.getString("vtname"),
								rs.getString("vstatus"));
				vehicles.add(model);
			}
			if(vehicles.size() == 0) {
				rs.close();
				stmt.close();
				return new CompanyRentalReportModel(1, "No Rentals At Any Branches", null, 0);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return new CompanyRentalReportModel(1, e.getMessage(),null,0);
		}

		try {
			PreparedStatement stmt = connection.prepareStatement(" SELECT v.location, v.city, " +
					"SUM(case when vtname = 'SUV' then 1 else 0 end) AS suv_count, " +
					"SUM(case when vtname = 'Truck' then 1 else 0 end) AS truck_count, " +
					"SUM(case when vtname = 'Mid-sized' then 1 else 0 end) AS midsize_count, " +
					"SUM(case when vtname = 'Full-sized' then 1 else 0 end) AS fullsize_count, " +
					"SUM(case when vtname = 'Compact' then 1 else 0 end) AS compact_count, " +
					"SUM(case when vtname = 'Economy' then 1 else 0 end) AS economy_count, " +
					"SUM(case when vtname = 'Economy' then 1 else 0 end) AS standard_count, " +
					"COUNT(*) AS total_count " +
					"FROM rentals r, vehicles v " +
					"WHERE r.vid = v.vid AND r.fromDate <= ? AND ? <= r.toDate " +
					"GROUP BY location, city");
			stmt.setDate(1, sqlDate);
			stmt.setDate(2,sqlDate);
			stmt.executeUpdate();
			ResultSet rs = stmt.executeQuery();
			// System.out.println("made it 2");

			while(rs.next()) {
				BranchRentalReportModel model = new BranchRentalReportModel
						(vehicles, 0, rs.getString("location"),
						rs.getString("city"),
						rs.getInt("suv_count"),
						rs.getInt("truck_count"),
						rs.getInt("economy_count"),
						rs.getInt("midsize_count"),
						rs.getInt("compact_count"),
						rs.getInt("standard_count"),
						rs.getInt("fullsize_count"),
						rs.getInt("total_count"));
				result.add(model);
			}
			if(result.size() == 0) {
				rs.close();
				stmt.close();
				return new CompanyRentalReportModel(1, "No Rentals At Any Branches", null, 0);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return new CompanyRentalReportModel(1, e.getMessage(),null,0);
		}
		try {
			// System.out.println("made it");
			PreparedStatement stmt = connection.prepareStatement( "SELECT COUNT(*) AS total_count " +
					"FROM rentals r, vehicles v " +
					"WHERE r.vid = v.vid AND r.fromDate <= ?" +
					"GROUP BY location, city");
			stmt.setDate(1, sqlDate);
			stmt.executeUpdate();
			ResultSet rs = stmt.executeQuery();
			rs.next();
			CompanyRentalReportModel crrm = new CompanyRentalReportModel(0, "", result, rs.getInt("total_count"));
			rs.close();
			stmt.close();
			return crrm;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return new CompanyRentalReportModel(1, e.getMessage(), null,0);
		}
	}


	public BranchRentalReportModel generateDailyRentalSingleBranch(String location, String city, String date) {
		Date sqlDate = Date.valueOf(date);
		ArrayList<ReportVehicleModel> vehicles = new ArrayList<>();
		try {
			PreparedStatement stmt = connection.prepareStatement(" SELECT location, city " +
					"FROM vehicles v " +
					"WHERE v.location LIKE ? AND v.city LIKE ? " +
					"GROUP BY location, city ");
			stmt.setString(1, location);
			stmt.setString(2, city);
			stmt.executeUpdate();
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()) {
				rs.close();
				stmt.close();
				return new BranchRentalReportModel(1, "A branch does not exist at the specified location and city");
			}

		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return new BranchRentalReportModel(1, e.getMessage());
		}
		try {
			PreparedStatement stmt = connection.prepareStatement(" SELECT v.location, v.city, v.vid, v.vtname, v.vstatus " +
					"FROM rentals r, vehicles v " +
					"WHERE r.vid = v.vid AND r.fromDate <= ? AND ? <= r.toDate ");
			stmt.setDate(1, sqlDate);
			stmt.setDate(2,sqlDate);
			stmt.executeUpdate();
			ResultSet rs = stmt.executeQuery();
			// System.out.println("made it 2");

			while(rs.next()) {
				ReportVehicleModel model = new ReportVehicleModel
						(rs.getString("location"),
								rs.getString("city"),
								rs.getInt("vid"),
								rs.getString("vtname"),
								rs.getString("vstatus"));
				vehicles.add(model);
			}
			if(vehicles.size() == 0) {
				rs.close();
				stmt.close();
				return new BranchRentalReportModel(1, "No Rentals At Any Branches");
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return new BranchRentalReportModel(1, "No Rentals At Any Branches");
		}
		try {
			PreparedStatement stmt = connection.prepareStatement(" SELECT v.location, v.city, " +
					"SUM(case when vtname = 'SUV' then 1 else 0 end) AS suv_count, " +
					"SUM(case when vtname = 'Truck' then 1 else 0 end) AS truck_count, " +
					"SUM(case when vtname = 'Mid-sized' then 1 else 0 end) AS midsize_count, " +
					"SUM(case when vtname = 'Full-sized' then 1 else 0 end) AS fullsize_count, " +
					"SUM(case when vtname = 'Compact' then 1 else 0 end) AS compact_count, " +
					"SUM(case when vtname = 'Standard' then 1 else 0 end) AS standard_count, " +
					"SUM(case when vtname = 'Economy' then 1 else 0 end) AS economy_count, " +
					"COUNT(*) AS total_count " +
					"FROM rentals r, vehicles v " +
					"WHERE r.vid = v.vid AND r.fromDate <= ? AND ? <= r.toDate AND v.location LIKE ? AND v.city LIKE ? " +
					"GROUP BY location, city ");
			stmt.setDate(1, sqlDate);
			stmt.setDate(2,sqlDate);
			stmt.setString(3, location);
			stmt.setString(4, city);
			stmt.executeUpdate();
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				BranchRentalReportModel model = new BranchRentalReportModel
						(vehicles, 0, rs.getString("location"),
								rs.getString("city"),
								rs.getInt("suv_count"),
								rs.getInt("truck_count"),
								rs.getInt("economy_count"),
								rs.getInt("compact_count"),
								rs.getInt("midsize_count"),
								rs.getInt("standard_count"),
								rs.getInt("fullsize_count"),
								rs.getInt("total_count"));
				rs.close();
				stmt.close();
				return model;
			} else {
				rs.close();
				stmt.close();
				return new BranchRentalReportModel(1, "No Results Found");

			}

		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return new BranchRentalReportModel(1, e.getMessage());
		}
	}


	public CompanyReturnReportModel generateDailyReturnAllBranches(String date) {
		ArrayList<BranchReturnReportModel> result = new ArrayList<BranchReturnReportModel>();
		Date sqlDate = Date.valueOf(date);
		ArrayList<ReportVehicleModel> vehicles = new ArrayList<>();
		try {
			PreparedStatement stmt = connection.prepareStatement(" SELECT v.location, v.city, v.vid, v.vtname, v.vstatus " +
					"FROM rentals ren, vehicles v , returns ret " +
					"WHERE ren.vid = v.vid AND ret.rid = ren.rid AND ret.return_date = ? ");
			stmt.setDate(1, sqlDate);
			stmt.executeUpdate();
			ResultSet rs = stmt.executeQuery();
			// System.out.println("made it 2");

			while(rs.next()) {
				ReportVehicleModel model = new ReportVehicleModel
						(rs.getString("location"),
								rs.getString("city"),
								rs.getInt("vid"),
								rs.getString("vtname"),
								rs.getString("vstatus"));
				vehicles.add(model);
			}
			if(vehicles.size() == 0) {
				rs.close();
				stmt.close();
				return new CompanyReturnReportModel(1, "No Returns At Any Branches", null, 0, 0);
			}
			for(int i = 0; i < vehicles.size(); i++) {
				System.out.println(vehicles.get(i));
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return new CompanyReturnReportModel(1, "No Returns At Any Branches", null, 0, 0);
		}
		try {
			PreparedStatement stmt = connection.prepareStatement(" SELECT v.location, v.city, " +
				"SUM(case when vtname = 'SUV' then 1 else 0 end) AS suv_count, " +
				"SUM(case when vtname = 'SUV' then rt.return_value else 0 end) AS suv_subtotal, " +
				"SUM(case when vtname = 'Truck' then 1 else 0 end) AS truck_count, " +
				"SUM(case when vtname = 'Truck' then rt.return_value else 0 end) AS truck_subtotal, " +
				"SUM(case when vtname = 'Mid-sized' then 1 else 0 end) AS midsize_count, " +
				"SUM(case when vtname = 'Mid-sized' then rt.return_value else 0 end) AS midsize_subtotal, " +
				"SUM(case when vtname = 'Full-sized' then 1 else 0 end) AS fullsize_count, " +
				"SUM(case when vtname = 'Full-sized' then rt.return_value else 0 end) AS fullsize_subtotal, " +
				"SUM(case when vtname = 'Compact' then 1 else 0 end) AS compact_count, " +
				"SUM(case when vtname = 'Compact' then rt.return_value else 0 end) AS compact_subtotal, " +
				"SUM(case when vtname = 'Economy' then 1 else 0 end) AS economy_count, " +
				"SUM(case when vtname = 'Economy' then rt.return_value else 0 end) AS economy_subtotal, " +
				"COUNT(*) AS branch_vehicle_count, " +
				"SUM(rt.return_value) AS branch_subtotal " +
				"FROM returns rt, rentals rn, vehicles v " +
				"WHERE rt.rid = rn.rid AND v.vid = rn.vid AND rn.return_date = ? "+
				"GROUP BY location, city ");
			stmt.setDate(1, sqlDate);
			System.out.println(stmt.executeUpdate());
			ResultSet rs = stmt.executeQuery();


			while(rs.next()) {
				System.out.println("made it");
				BranchReturnReportModel model = new BranchReturnReportModel(vehicles, 0, rs.getString("location"),
						rs.getString("city"),
						rs.getInt("suv_count"),
						rs.getFloat("suv_subtotal"),
						rs.getInt("truck_count"),
						rs.getFloat("truck_subtotal"),
						rs.getInt("economy_count"),
						rs.getFloat("economy_subtotal"),
						rs.getInt("compact_count"),
						rs.getFloat("compact_subtotal"),
						rs.getInt("midsize_count"),
						rs.getFloat("midsize_subtotal"),
						rs.getInt("standard_count"),
						rs.getFloat("standard_subtotal"),
						rs.getInt("fullsize_count"),
						rs.getFloat("fullsize_subtotal"),
						rs.getInt("branch_vehicle_count"),
						rs.getFloat("branch_subtotal"));
				System.out.println(model);
				result.add(model);
			}
			if(result.size() == 0) {
				rs.close();
				stmt.close();
				return new CompanyReturnReportModel(1, "No Returns At Any Branches", null, 0, 0);
			}
			System.out.println(result);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return new CompanyReturnReportModel(1, e.getMessage(),null,0, 0);
		}

		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) AS vehicle_count, SUM(ret.return_value) AS revenue_total " +
					"FROM returns ret, rentals ren, vehicles v " +
					"WHERE ret.rid = ren.rid AND v.vid = ren.vid AND ren.return_date = ?");
			stmt.setDate(1, sqlDate);
			stmt.executeUpdate();
			ResultSet rs = stmt.executeQuery();
			rs.next();
			System.out.println(result);
			CompanyReturnReportModel crrm = new CompanyReturnReportModel(0, "", result,
					rs.getInt("vehicle_count"), rs.getFloat("revenue_total"));
			System.out.println(crrm.getBranchReturnVehicleCount());
			System.out.println(crrm.getBranchRevenueTotal());
			rs.close();
			stmt.close();
			return crrm;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return new CompanyReturnReportModel(1,  e.getMessage(), null,0, 0);
		}
	}

	public BranchReturnReportModel generateDailyReturnSingleBranch(String location, String city, String date) {
		Date sqlDate = Date.valueOf(date);
		ArrayList<ReportVehicleModel> vehicles = new ArrayList<>();
		try {
			PreparedStatement stmt = connection.prepareStatement(" SELECT location, city " +
					"FROM vehicles v " +
					"WHERE v.location LIKE ? AND v.city LIKE ? " +
					"GROUP BY location, city ");
			stmt.setString(1, location);
			stmt.setString(2, city);
			stmt.executeUpdate();
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()) {
				rs.close();
				stmt.close();
				return new BranchReturnReportModel(1, "A branch does not exist at the specified location and city");
			}

		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return new BranchReturnReportModel(1, e.getMessage());
		}
		try {
			PreparedStatement stmt = connection.prepareStatement(" SELECT v.location, v.city, v.vid, v.vtname, v.vstatus " +
					"FROM rentals ren, vehicles v , returns ret " +
					"WHERE ren.vid = v.vid AND ret.rid = ren.rid AND ret.return_date = ? ");
			stmt.setDate(1, sqlDate);
			stmt.executeUpdate();
			ResultSet rs = stmt.executeQuery();
			// System.out.println("made it 2");

			while(rs.next()) {
				ReportVehicleModel model = new ReportVehicleModel
						(rs.getString("location"),
								rs.getString("city"),
								rs.getInt("vid"),
								rs.getString("vtname"),
								rs.getString("vstatus"));
				vehicles.add(model);
			}
			if(vehicles.size() == 0) {
				rs.close();
				stmt.close();
				return new BranchReturnReportModel(1, "No Returns At Any Branches");
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return new BranchReturnReportModel(1, "No Returns At Any Branches");
		}
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT v.location, v.city, " +
					"SUM(case when vtname = 'SUV' then 1 else 0 end) AS suv_count, " +
					"SUM(case when vtname = 'SUV' then ret.return_value else 0 end) AS suv_subtotal, " +
					"SUM(case when vtname = 'Truck' then 1 else 0 end) AS truck_count, " +
					"SUM(case when vtname = 'Truck' then ret.return_value else 0 end) AS truck_subtotal, " +
					"SUM(case when vtname = 'Mid-sized' then 1 else 0 end) AS midsize_count, " +
					"SUM(case when vtname = 'Mid-sized' then ret.return_value else 0 end) AS midsize_subtotal, " +
					"SUM(case when vtname = 'Full-sized' then 1 else 0 end) AS fullsize_count, " +
					"SUM(case when vtname = 'Full-sized' then ret.return_value else 0 end) AS fullsize_subtotal, " +
					"SUM(case when vtname = 'Compact' then 1 else 0 end) AS compact_count, " +
					"SUM(case when vtname = 'Compact' then ret.return_value else 0 end) AS compact_subtotal, " +
					"SUM(case when vtname = 'Economy' then 1 else 0 end) AS economy_count, " +
					"SUM(case when vtname = 'Economy' then ret.return_value else 0 end) AS economy_subtotal, " +
					"COUNT(*) AS branch_vehicle_count, " +
					"SUM(ret.return_value) AS branch_subtotal " +
					"FROM returns ret JOIN rentals ren USING (rid) " +
					"JOIN vehicles v USING (vid) " +
					"WHERE ret.return_date = ? AND v.location LIKE ? AND v.city LIKE ? "+
					"GROUP BY location, city");
			stmt.setDate(1, sqlDate);
			stmt.setString(2, location);
			stmt.setString(3, city);
			stmt.executeUpdate();
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				BranchReturnReportModel model = new BranchReturnReportModel(vehicles, 0, rs.getString("location"),
						rs.getString("city"),
						rs.getInt("suv_count"),
						rs.getFloat("suv_subtotal"),
						rs.getInt("truck_count"),
						rs.getFloat("truck_subtotal"),
						rs.getInt("economy_count"),
						rs.getFloat("economy_subtotal"),
						rs.getInt("compact_count"),
						rs.getFloat("compact_subtotal"),
						rs.getInt("midsize_count"),
						rs.getFloat("midsize_subtotal"),
						rs.getInt("standard_count"),
						rs.getFloat("standard_subtotal"),
						rs.getInt("fullsize_count"),
						rs.getFloat("fullsize_subtotal"),
						rs.getInt("branch_vehicle_count"),
						rs.getFloat("branch_subtotal"));
				rs.close();
				stmt.close();
				return model;
			} else {
				rs.close();
				stmt.close();
				return new BranchReturnReportModel(1, "No Results Found");
			}

		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return new BranchReturnReportModel(1, e.getMessage());
		}
	}


	private void rollbackConnection() throws Exception {
		try  {
			connection.rollback();	
		} catch (SQLException e) {
            throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public void insertRental(RentalModel model) throws Exception {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO rentals VALUES (?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, model.getRid());
            ps.setInt(2, model.getVid());
            ps.setString(3, model.getDlicense());
            ps.setDate(4, Date.valueOf(model.getFromDate()));
            ps.setDate(5, Date.valueOf(model.getToDate()));
            ps.setFloat(6, model.getOdometer());
            ps.setString(7, model.getCardName());
            ps.setInt(8, model.getCardNo());
            ps.setDate(9, Date.valueOf(model.getExpDate()));
            ps.setInt(10, model.getConfNo());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            connection.rollback();
            throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
        }
    }


    public RentalModel getRentalInfo(int rid) throws Exception {
        RentalModel result = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM rentals WHERE rid = ?");
            stmt.setInt(1, rid);
            ResultSet rs = stmt.executeQuery();
			rs.next();
            RentalModel model = new RentalModel(rs.getInt("rid"),
                    rs.getInt("vid"),
                    rs.getString("dlicense"),
                    rs.getDate("fromDate").toLocalDate(),
                    rs.getDate("toDate").toLocalDate(),
                    rs.getFloat("odometer"),
                    rs.getString("cardName"),
                    rs.getInt("cardNo"),
                    rs.getDate("ExpDate").toLocalDate(),
                    rs.getInt("confNo"));
            result = model;

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            // System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public void insertReturn(ReturnModel model) throws Exception {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO returns VALUES (?,?,?,?,?)");
            ps.setInt(1, model.getRentID());
            ps.setDate(2, Date.valueOf(model.getDate()));
            ps.setFloat(3, model.getOdometer());
            ps.setBoolean(4, model.getFullTank());
            ps.setFloat(5, model.getValue());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            connection.rollback();
            throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public ReturnModel getReturnInfo(int rentID) throws Exception {
        ReturnModel result = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM returns WHERE rid = ?");
            stmt.setInt(1, rentID);
            ResultSet rs = stmt.executeQuery();
			rs.next();
            ReturnModel model = new ReturnModel(rs.getInt("rid"),
                    rs.getDate("return_date").toLocalDate(),
                    rs.getFloat("return_odometer"),
                    rs.getBoolean("return_fulltank"),
                    rs.getFloat("return_value"));
            result = model;

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            // System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }


    public ReservationModel getReservation(int confNo) throws Exception {
        ReservationModel result = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM reservations WHERE confNo = ?");
            stmt.setInt(1, confNo);
            ResultSet rs = stmt.executeQuery();
			rs.next();
            ReservationModel model = new ReservationModel(rs.getInt("confNo"),
                   rs.getInt("vid"),
                   rs.getString("vtname"),
                    rs.getString("dlicense"),
                    rs.getDate("fromDate").toLocalDate(),
                    rs.getDate("toDate").toLocalDate());
            result = model;

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            // System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }

    public VehicleModel getVehicle(int vid) throws Exception {
        VehicleModel result = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM vehicles WHERE vid = ?");
            stmt.setInt(1, vid);
            ResultSet rs = stmt.executeQuery();
			rs.next();
            VehicleModel model = new VehicleModel(rs.getInt("vid"),
                    rs.getString("vlicense"), rs.getString("make"),
                    rs.getInt("year"), rs.getString("color"),
                    rs.getFloat("odometer"), rs.getString("vstatus"),
                    rs.getString("vtname"),
                    rs.getString("location"), rs.getString("city"));
            result = model;

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            // System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }
    // public void updateReturnValue(int rid, float value);
    public void updateReturnValue(int rid, float value) throws Exception {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE returns SET return_value = ? WHERE rid = ?");
            ps.setFloat(1, value);
            ps.setInt(2, rid);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Return " + rid + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            connection.rollback();
            throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public VehicleTypeModel getVehicleType(String vtname) throws Exception {
        VehicleTypeModel result = null;

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM vehicletypes WHERE vtname = ?");
            stmt.setString(1, vtname);
            ResultSet rs = stmt.executeQuery();
			rs.next();
            VehicleTypeModel model = new VehicleTypeModel(rs.getString("vtname"), rs.getString("features"),
                    rs.getFloat("drate"), rs.getFloat("wrate"), rs.getFloat("hrate"), rs.getFloat("wirate"),
                    rs.getFloat("dirate"), rs.getFloat("krate"));
            result = model;

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            // System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result;
    }


//	//TODO: once this works we need it for the other combinations, also change return to count
//	public VehicleModel veiwAmtVehiclesAvailable(String location, String city, Date fromDate, Date toDate, Enum type) {
//		VehicleModel result = null;
//		try {
//			PreparedStatement stmt = connection.prepareStatement("Select v.vtname" +
//					"from vehicles v" +
//					"where v.city = ? AND v.location = ? AND v.vtname = ?" +
//					"AND v.vid NOT IN ( " +
//					" SELECT vid\n  from rentals r  " +
//					"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
//					"BETWEEN r.fromDate AND r.toDate  " +
//					"UNION  " +
//					"SELECT vid  from rentals r" +
//					"  where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
//					"BETWEEN r.fromDate AND r.toDate  " +
//					"UNION" +
//					"  Select vid  " +
//					"from rentals r  " +
//					"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate and " +
//					"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate ");
//			stmt.setString(1, city);
//			stmt.setString(2, location);
//			stmt.setObject(3, type);
//			stmt.setDate(4, fromDate);
//			stmt.setDate(5, toDate);
//			stmt.setDate(6, fromDate);
//			stmt.setDate(7, toDate);
//			ResultSet rs = stmt.executeQuery();
//// TODO get updated version of this
//
////			VehicleModel model = new VehicleModel(rs.getString("vid"),
////					rs.getString("vlicense"),
////					rs.getString("make")
////					);
//
//			// int year, String color,
//			// float odometer, String vstatus, VehicleTypeEnum vtname, String location, String city)
////			result = model;
//
//			rs.close();
//			stmt.close();
//		} catch (SQLException e) {
//			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//		}
//
//		return result;
//	}

	public void insertCustomer(CustomerModel model) throws Exception {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO customers VALUES (?,?,?,?)");
			ps.setString(1, model.getDlicense());
			ps.setInt(2, model.getCellPhone());
			ps.setString(3, model.getCname());
			ps.setString(4, model.getAddr());

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			rollbackConnection();
			throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public CustomerModel getCustomer(String dlicense) throws Exception {
		CustomerModel result = null;

		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT * FROM customers WHERE dlicense = ?");
			stmt.setString(1, dlicense);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			CustomerModel model = new CustomerModel(rs.getString("dlicense"), rs.getInt("cellphone"),
					rs.getString("cname"), rs.getString("addr"));
			result = model;

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
		}

		return result;
	}

	public CustomerModel[] getAllCustomers() throws Exception {
		ArrayList<CustomerModel> result = new ArrayList<CustomerModel>();

		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM customers");

			while(rs.next()) {
				CustomerModel model = new CustomerModel(rs.getString("dlicense"),
						rs.getInt("cellphone"), rs.getString("cname"), rs.getString("addr"));
				result.add(model);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
		}

		return result.toArray(new CustomerModel[result.size()]);
	}

	public void updateCustomerAddress(String dlicense, String address) throws Exception {
		try {
			PreparedStatement ps = connection.prepareStatement("UPDATE customers SET addr = ? WHERE dlicense = ?");
			ps.setString(1, address);
			ps.setString(2, dlicense);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Customer " + dlicense + " does not exist!");
			}

			connection.commit();

			ps.close();
		} catch (SQLException e) {
			rollbackConnection();
			throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
		}
	}


	public void deleteCustomer(String dlicense) throws Exception {
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM customers WHERE dlicense = ?");
			ps.setString(1, dlicense);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Customer " + dlicense + " does not exist!");
			}

			connection.commit();

			ps.close();
		} catch (SQLException e) {
			rollbackConnection();
			throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	//TODO check date selection
	public String veiwAmtVehiclesAvailable(String location, String city, Date fromDate, Date toDate, String type) {
		//ArrayList<SearchModel> result = new ArrayList<SearchModel>();
		if ((fromDate == null || toDate == null) & city == null & location == null & type == null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) from vehicles ");
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}

		} else if ((fromDate != null && toDate != null) & city == null & location == null & type == null) {
			System.out.println("Input into view amt: " + fromDate + " " + toDate);
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.vid NOT IN ( " +
						"SELECT vid  from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION  " +
						"SELECT vid  from rentals r" +
						"  where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"Select vid  " +
						"from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate and " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate )");
				stmt.setDate(1, fromDate);
				stmt.setDate(2, toDate);
				stmt.setDate(3, fromDate);
				stmt.setDate(4, toDate);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate == null || toDate == null) & city == null & location == null & type != null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.vtname = ? ");
				stmt.setString(1, type);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate == null || toDate == null) & city != null & location == null & type == null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.city = ? ");
				stmt.setString(1, city);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate == null || toDate == null) & city == null & location != null & type == null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.location = ? ");
//				stmt.setString(1, city);
				stmt.setString(1, location);
//				stmt.setObject(3, type);
//				stmt.setDate(4, fromDate);
//				stmt.setDate(5, toDate);
//				stmt.setDate(6, fromDate);
//				stmt.setDate(7, toDate);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate == null || toDate == null) & city == null & location != null & type != null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.location = ? AND v.vtname = ? ");
				//stmt.setString(1, city);
				stmt.setString(1, location);
				stmt.setObject(2, type);
//				stmt.setDate(4, fromDate);
//				stmt.setDate(5, toDate);
//				stmt.setDate(6, fromDate);
//				stmt.setDate(7, toDate);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate == null || toDate == null) & city != null & location == null & type != null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.city = ? AND v.vtname = ? ");
				stmt.setString(1, city);
				//stmt.setString(2, location);
				stmt.setObject(2, type);
//				stmt.setDate(4, fromDate);
//				stmt.setDate(5, toDate);
//				stmt.setDate(6, fromDate);
//				stmt.setDate(7, toDate);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate != null && toDate != null) & city == null & location != null & type == null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.location = ? " +
						"AND v.vid NOT IN ( " +
						"SELECT vid  from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION  " +
						"SELECT vid  from rentals r" +
						"  where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"Select vid  " +
						"from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate and " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate )");
				//stmt.setString(1, city);
				stmt.setString(1, location);
				//stmt.setObject(3, type);
				stmt.setDate(2, fromDate);
				stmt.setDate(3, toDate);
				stmt.setDate(4, fromDate);
				stmt.setDate(5, toDate);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate != null && toDate != null) & city == null & location == null & type != null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.vtname = ? " +
						"AND v.vid NOT IN ( " +
						"SELECT vid  from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION  " +
						"SELECT vid  from rentals r" +
						"  where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"Select vid  " +
						"from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate and " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate )");
				//stmt.setString(1, city);
				//stmt.setString(2, location);
				stmt.setObject(1, type);
				stmt.setDate(2, fromDate);
				stmt.setDate(3, toDate);
				stmt.setDate(4, fromDate);
				stmt.setDate(5, toDate);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate != null && toDate != null) & city != null & location == null & type == null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.city = ? " +
						"AND v.vid NOT IN ( " +
						"SELECT vid  from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION  " +
						"SELECT vid  from rentals r" +
						"  where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"Select vid  " +
						"from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate and " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate )");
				stmt.setString(1, city);
				//stmt.setString(2, location);
				//stmt.setObject(3, type);
				stmt.setDate(2, fromDate);
				stmt.setDate(3, toDate);
				stmt.setDate(4, fromDate);
				stmt.setDate(5, toDate);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate == null || toDate == null) & city != null & location != null & type == null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.city = ? AND v.location = ?");
				stmt.setString(1, city);
				stmt.setString(2, location);
				//stmt.setObject(3, type);
//				stmt.setDate(4, fromDate);
//				stmt.setDate(5, toDate);
//				stmt.setDate(6, fromDate);
//				stmt.setDate(7, toDate);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate != null && toDate != null) & city != null & location != null & type == null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.city = ? AND v.location = ? " +
						"AND v.vid NOT IN ( " +
						"SELECT vid  from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION  " +
						"SELECT vid  from rentals r" +
						"  where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"Select vid  " +
						"from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate and " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate )");
				stmt.setString(1, city);
				stmt.setString(2, location);
				//stmt.setObject(3, type);
				stmt.setDate(3, fromDate);
				stmt.setDate(4, toDate);
				stmt.setDate(5, fromDate);
				stmt.setDate(6, toDate);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate == null || toDate == null) & city != null & location != null & type != null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.city = ? AND v.location = ? AND v.vtname = ? ");
				stmt.setString(1, city);
				stmt.setString(2, location);
				stmt.setObject(3, type);
//				stmt.setDate(4, fromDate);
//				stmt.setDate(5, toDate);
//				stmt.setDate(6, fromDate);
//				stmt.setDate(7, toDate);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate != null && toDate != null) & city == null & location != null & type != null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.location = ? AND v.vtname = ? " +
						"AND v.vid NOT IN ( " +
						"SELECT vid  from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION  " +
						"SELECT vid  from rentals r" +
						"  where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"Select vid  " +
						"from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate and " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate )");
				//stmt.setString(1, city);
				stmt.setString(1, location);
				stmt.setObject(2, type);
				stmt.setDate(3, fromDate);
				stmt.setDate(4, toDate);
				stmt.setDate(5, fromDate);
				stmt.setDate(6, toDate);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate != null && toDate != null) & city != null & location == null & type != null) {
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.city = ? AND v.vtname = ? " +
						"AND v.vid NOT IN ( " +
						"SELECT vid  from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION  " +
						"SELECT vid  from rentals r" +
						"  where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"Select vid  " +
						"from rentals r  " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate and " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate )");
				stmt.setString(1, city);
				//stmt.setString(2, location);
				stmt.setObject(2, type);
				stmt.setDate(3, fromDate);
				stmt.setDate(4, toDate);
				stmt.setDate(5, fromDate);
				stmt.setDate(6, toDate);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else { // all fields have input
			System.out.println("DATES prior to submission: " + fromDate + " " + toDate);
			try {
				PreparedStatement stmt = connection.prepareStatement("Select count(*) " +
						"from vehicles v " +
						"where v.city = ? AND v.location = ? AND v.vtname = ? " +
						"AND v.vid NOT IN ( " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate " +
						"UNION " +
						"Select vid " +
						"from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate AND " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate )");
				stmt.setString(1, city);
				stmt.setString(2, location);
				stmt.setObject(3, type);
				stmt.setDate(4, fromDate);
				stmt.setDate(5, toDate);
				stmt.setDate(6, fromDate);
				stmt.setDate(7, toDate);
				return getAmtFromResults(stmt);
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}

		}
		return "0";
	}


	private String getAmtFromResults(PreparedStatement stmt) throws SQLException {
		ResultSet rs = stmt.executeQuery();
		String returnResult = "0";
		while(rs.next()) {
			SearchModel model = new SearchModel(123,
					"makeString",
					"nameString",
					1999,
					"colorString",
					//rs.getString(0));
					rs.getString(1));
			//System.out.println("test1_amt: " + model.getAmt()); // gets 2
			returnResult = model.getAmt();
			if (returnResult == "0") {
				//TODO change to pop up
				System.out.println("There are no vehicles available with that selection criteria.");
			}
			// add this to return
		}
		rs.close();
		stmt.close();
		return returnResult;
	}


	// once a valid reservation has been found
	public void insertReservation(ReservationModel model) throws Exception {
		try {
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO reservations VALUES (?,?,?,?,?,?)");
			ps.setInt(1, model.getConfNo());
			ps.setInt(2, model.getVID());
			ps.setString(3, model.getVtname());
			ps.setString(4, model.getDlicense());
			ps.setDate(5, Date.valueOf(model.getFromDate()));
			ps.setDate(6,Date.valueOf(model.getToDate()));


			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			connection.rollback();
			throw new Exception(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public ArrayList<SearchModel> veiwVehiclesAvailable(String location, String city, Date fromDate, Date toDate, String type) {
		ArrayList<SearchModel> result = new ArrayList<SearchModel>();
		if ((fromDate == null || toDate == null) & city == null & location == null & type == null) {
			//1
			try {
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"ORDER BY v.make, v.vtname, v.year, v.color");

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate != null && toDate != null) & city == null & location == null & type == null) {
			//2
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.vid NOT IN ( " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate " +
						"UNION " +
						"Select vid " +
						"from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate AND " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate) " +
						"ORDER BY v.make, v.vtname, v.year, v.color");

				stmt.setDate(1, fromDate);
				stmt.setDate(2, toDate);
				stmt.setDate(3, fromDate);
				stmt.setDate(4, toDate);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate == null || toDate == null) & city == null & location == null & type != null) {
			//3
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.vtname = ? " +
						"ORDER BY v.make, v.vtname, v.year, v.color");

				stmt.setObject(1, type);


				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate == null || toDate == null) & city != null & location == null & type == null) {
			//4
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.city = ?" +
						"ORDER BY v.make, v.vtname, v.year, v.color");
				stmt.setString(1, city);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else if ((fromDate == null || toDate == null) & city == null & location != null & type == null) {
			//5
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.location = ? " +
						"ORDER BY v.make, v.vtname, v.year, v.color");
				stmt.setString(1, location);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			} //6
		} else if ((fromDate == null || toDate == null) & city == null & location != null & type != null) {
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.location = ? AND v.vtname = ? " +
						"ORDER BY v.make, v.vtname, v.year, v.color");

				stmt.setString(1, location);
				stmt.setObject(2, type);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}//7
		} else if ((fromDate == null || toDate == null) & city != null & location == null & type != null) {
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.city = ? AND v.vtname = ? " +
						"ORDER BY v.make, v.vtname, v.year, v.color");
				stmt.setString(1, city);
				stmt.setObject(2, type);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			} //8
		} else if ((fromDate != null && toDate != null) & city == null & location != null & type == null) {
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.location = ?" +
						"AND v.vid NOT IN ( " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate " +
						"UNION " +
						"Select vid " +
						"from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate AND " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate) " +
						"ORDER BY v.make, v.vtname, v.year, v.color");
				stmt.setString(1, location);
				stmt.setDate(2, fromDate);
				stmt.setDate(3, toDate);
				stmt.setDate(4, fromDate);
				stmt.setDate(5, toDate);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			} //9
		} else if ((fromDate != null && toDate != null) & city == null & location == null & type != null) {
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.vtname = ? " +
						"AND v.vid NOT IN ( " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate " +
						"UNION " +
						"Select vid " +
						"from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate AND " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate) " +
						"ORDER BY v.make, v.vtname, v.year, v.color");
				stmt.setObject(1, type);
				stmt.setDate(2, fromDate);
				stmt.setDate(3, toDate);
				stmt.setDate(4, fromDate);
				stmt.setDate(5, toDate);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}//10
		} else if ((fromDate != null && toDate != null) & city != null & location == null & type == null) {
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.city = ?  " +
						"AND v.vid NOT IN ( " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate " +
						"UNION " +
						"Select vid " +
						"from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate AND " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate) " +
						"ORDER BY v.make, v.vtname, v.year, v.color");
				stmt.setString(1, city);
				stmt.setDate(2, fromDate);
				stmt.setDate(3, toDate);
				stmt.setDate(4, fromDate);
				stmt.setDate(5, toDate);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}//11
		} else if ((fromDate == null || toDate == null) & city != null & location != null & type == null) {
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.city = ? AND v.location = ?" +
						"ORDER BY v.make, v.vtname, v.year, v.color");
				stmt.setString(1, city);
				stmt.setString(2, location);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}//12
		} else if ((fromDate != null && toDate != null) & city != null & location != null & type == null) {
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.city = ? AND v.location = ? " +
						"AND v.vid NOT IN ( " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate " +
						"UNION " +
						"Select vid " +
						"from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate AND " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate) " +
						"ORDER BY v.make, v.vtname, v.year, v.color");
				stmt.setString(1, city);
				stmt.setString(2, location);
				stmt.setDate(3, fromDate);
				stmt.setDate(4, toDate);
				stmt.setDate(5, fromDate);
				stmt.setDate(6, toDate);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}//13
		} else if ((fromDate == null || toDate == null) & city != null & location != null & type != null) {
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.city = ? AND v.location = ? AND v.vtname = ? " +
						"ORDER BY v.make, v.vtname, v.year, v.color");
				stmt.setString(1, city);
				stmt.setString(2, location);
				stmt.setObject(3, type);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}//14
		} else if ((fromDate != null && toDate != null) & city == null & location != null & type != null)
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.location = ? AND v.vtname = ? " +
						"AND v.vid NOT IN ( " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate " +
						"UNION " +
						"Select vid " +
						"from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate AND " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate) " +
						"ORDER BY v.make, v.vtname, v.year, v.color");
				stmt.setString(1, location);
				stmt.setObject(2, type);
				stmt.setDate(3, fromDate);
				stmt.setDate(4, toDate);
				stmt.setDate(5, fromDate);
				stmt.setDate(6, toDate);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		else if ((fromDate != null && toDate != null) & city != null & location == null & type != null) {
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.city = ? AND v.vtname = ? " +
						"AND v.vid NOT IN ( " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate " +
						"UNION " +
						"Select vid " +
						"from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate AND " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate) " +
						"ORDER BY v.make, v.vtname, v.year, v.color");
				stmt.setString(1, city);
				stmt.setObject(2, type);
				stmt.setDate(3, fromDate);
				stmt.setDate(4, toDate);
				stmt.setDate(5, fromDate);
				stmt.setDate(6, toDate);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		} else {
			try {
				//(int vid, String make, String name, int year, String color, String amt)
				PreparedStatement stmt = connection.prepareStatement("Select v.vid, v.make, v.vtname, v.year, v.color " +
						"from vehicles v " +
						"where v.city = ? AND v.vtname = ? AND v.location = ?" +
						"AND v.vid NOT IN ( " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate  " +
						"UNION " +
						"SELECT vid from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') " +
						"BETWEEN r.fromDate AND r.toDate " +
						"UNION " +
						"Select vid " +
						"from rentals r " +
						"where to_timestamp(?, 'YYYY/MM/DD HH24 MI') < r.fromDate AND " +
						"to_timestamp(?, 'YYYY/MM/DD HH24 MI') > r.toDate) " +
						"ORDER BY v.make, v.vtname, v.year, v.color");
				stmt.setString(1, city);
				stmt.setObject(2, type);
				stmt.setObject(3, location);
				stmt.setDate(4, fromDate);
				stmt.setDate(5, toDate);
				stmt.setDate(6, fromDate);
				stmt.setDate(7, toDate);

				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					//(int vid, String make, String name, int year, String color, String amt)
					SearchModel model = new SearchModel(rs.getInt(1), rs.getString(2),
							rs.getString(3), rs.getInt(4), rs.getString(5),
							"");
					result.add(model);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			}
		}
		return result;
	}
}

