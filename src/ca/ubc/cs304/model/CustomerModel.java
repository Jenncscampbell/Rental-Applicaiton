package ca.ubc.cs304.model;

public class CustomerModel {

    private final String dlicense;
    private final int cellPhone;
    private final String cname;
    private final String addr;

    public CustomerModel(String dlicense, int cellPhone, String cname, String addr) {
        this.dlicense = dlicense;
        this.cellPhone = cellPhone;
        this.cname = cname;
        this.addr = addr;
    }

    public String getDlicense() { return dlicense; }

    public int getCellPhone() { return cellPhone; }

    public String getCname() { return cname; }

    public String getAddr() { return addr; }

    @Override
    public String toString() {
        String str = "";
        str += "CUSTOMER: " + cname + "\n";
        str += "LICENSE NUM: " + dlicense + "\n";
        str += "CELL: " + cellPhone + "\n";
        str += "ADDR: " + addr + "\n";
        return str;
    }
}
