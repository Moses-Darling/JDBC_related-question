package net.guides.jdbc_connection;
import java.sql.DriverManager; 
import java.util.*;
import java.sql.*;
class Item{
	private String name,type;
	private double ppq;
	void setName(String name) {this.name=name;}
	void setType(String type) {this.type=type;}
	void setPpq(double ppq) {this.ppq=ppq;}
	String getName() {return this.name;}
	String getType() {return this.type;}
	Double getPpq() {return this.ppq;}
	Item(String name,String type,double ppq){
		setName(name);
		setType(type);
		setPpq(ppq);
	}
}
class ItemDAO{
	public void addItem(Item item) throws SQLException{
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant","root","linux");
		PreparedStatement a=con.prepareStatement("insert into items values(?,?,?);");
		a.setString(1, item.getName());
		a.setString(2, item.getType());
		a.setDouble(3, item.getPpq());
		a.executeUpdate();
	}
	public void bulkCopy(List<Item>itemList) throws SQLException {
		for(Item i:itemList)
			addItem(i);
	}
	public List<Item> findItem(String type) throws SQLException{
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant","root","linux");
		PreparedStatement ps=con.prepareStatement("select * from items where item_type=(?);");
		ps.setString(1, type);
		ResultSet rs=ps.executeQuery();
		List<Item> li=new ArrayList<Item>();
		while(rs.next()) 
			li.add(new Item(rs.getString(1),rs.getString(2),rs.getDouble(3)));
		return li;
	}
	public List<Item> findItem(Double price) throws SQLException{
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant","root","linux");
		PreparedStatement ps=con.prepareStatement("select * from items where ppq=?;");
		ps.setDouble(1, price);
		ResultSet rs=ps.executeQuery();
		List<Item> li=new ArrayList<Item>();
		while(rs.next()) 
			li.add(new Item(rs.getString(1),rs.getString(2),rs.getDouble(3)));
		return li;
	}
}
public class ItemSearch {
	static List<Item> prefill(){
		List<Item> li=new ArrayList<Item>();
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter the number of items to input");
		int n=sc.nextInt();
		String iname,itype;
		double ippq;
		for(int i=0;i<n;i++) {
			System.out.println("Enter the name of Item "+(i+1));
			iname=sc.next();
			System.out.println("Enter the type of Item "+(i+1));
			itype=sc.next();
			System.out.println("Enter the Price per quantity of Item "+(i+1));
			ippq=sc.nextDouble();
			li.add(new Item(iname,itype,ippq));
		}
		return li;
	}
	public static void main(String[] args) throws ClassNotFoundException,SQLException {
		//Class.forName("com.mysql.cj.jdbc.Driver");
		try {
			   Class.forName("com.mysql.cj.jdbc.Driver");
			}
			catch(ClassNotFoundException ex) {
			   System.out.println("Error: unable to load driver class!");
			   System.exit(1);
			}
		Scanner sc=new Scanner(System.in);
		ItemDAO idao=new ItemDAO();
		List<Item> mainLi=prefill();
		idao.bulkCopy(mainLi);
		System.out.println("Enter a search type\n1.By Type\n2.By Price");
		int opt=sc.nextInt();
		switch(opt){
		case 1:System.out.println("Enter the type");
			String typeSearch=sc.next();
			List<Item> tysearch=idao.findItem(typeSearch);
			if(tysearch.size()<1)
				System.out.println("No such item is present");
			else {
				System.out.println("Name\tPrice\tType ");
				for(Item i:tysearch)
					System.out.println(i.getName()+"\t"+i.getPpq()+"\t"+i.getType());
			}
			break;
		case 2: System.out.println("Enter the price");
			double priceSearch=sc.nextDouble();
			List<Item> prisearch=idao.findItem(priceSearch);
			if(prisearch.size()<1)
				System.out.println("No such item is present");
			else {
				System.out.println("Name\\tPrice\\tType ");
				for(Item i:prisearch)
					System.out.println(i.getName()+"\t"+i.getPpq()+"\t"+i.getType());
			}
			break;
		default: System.out.println("Invalid Choice");

		}
	}
}