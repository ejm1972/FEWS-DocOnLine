//package ar.com.boldt.monedero.ws.module;
//
//public class ClassToXMLMapperModule {
//
//	package com.journaldev.xml.jaxb;
//	 
//	 
//	import javax.xml.bind.annotation.XmlAttribute;
//	import javax.xml.bind.annotation.XmlElement;
//	import javax.xml.bind.annotation.XmlRootElement;
//	import javax.xml.bind.annotation.XmlTransient;
//	import javax.xml.bind.annotation.XmlType;
//	 
//	 
//	@XmlRootElement(name = "Emp")
//	@XmlType(propOrder = {"name", "age", "role", "gender"})
//	public class Employee {
//	 
//	    private int id;
//	 
//	    private String gender;
//	 
//	    private int age;
//	    private String name;
//	    private String role;
//	 
//	    private String password;
//	 
//	 
//	    @XmlTransient
//	    public String getPassword() {
//	        return password;
//	    }
//	 
//	 
//	    public void setPassword(String password) {
//	        this.password = password;
//	    }
//	 
//	 
//	    @XmlAttribute
//	    public int getId() {
//	        return id;
//	    }
//	 
//	 
//	    public void setId(int id) {
//	        this.id = id;
//	    }
//	 
//	 
//	    public int getAge() {
//	        return age;
//	    }
//	 
//	 
//	    public void setAge(int age) {
//	        this.age = age;
//	    }
//	 
//	 
//	    public String getName() {
//	        return name;
//	    }
//	 
//	 
//	    public void setName(String name) {
//	        this.name = name;
//	    }
//	 
//	 
//	    @XmlElement(name = "gen")
//	    public String getGender() {
//	        return gender;
//	    }
//	 
//	 
//	    public void setGender(String gender) {
//	        this.gender = gender;
//	    }
//	 
//	 
//	    public String getRole() {
//	        return role;
//	    }
//	 
//	 
//	    public void setRole(String role) {
//	        this.role = role;
//	    }
//	 
//	 
//	    @Override
//	    public String toString() {
//	        return "ID = " + id + " NAME=" + name + " AGE=" + age + " GENDER=" + gender + " ROLE=" +
//	                role + " PASSWORD=" + password;
//	    }
//	 
//	}
//	
//	package com.journaldev.xml.jaxb;
//	 
//	 
//	import java.io.File;
//	 
//	import javax.xml.bind.JAXBContext;
//	import javax.xml.bind.JAXBException;
//	import javax.xml.bind.Marshaller;
//	import javax.xml.bind.Unmarshaller;
//	 
//	 
//	public class JAXBExample {
//	 
//	    private static final String FILE_NAME = "jaxb-emp.xml";
//	 
//	    public static void main(String[] args) {
//	        Employee emp = new Employee();
//	        emp.setId(1);
//	        emp.setAge(25);
//	        emp.setName("Pankaj");
//	        emp.setGender("Male");
//	        emp.setRole("Developer");
//	        emp.setPassword("sensitive");
//	 
//	        jaxbObjectToXML(emp);
//	 
//	        Employee empFromFile = jaxbXMLToObject();
//	        logger.debug(empFromFile.toString());
//	    }
//	 
//	 
//	    private static Employee jaxbXMLToObject() {
//	        try {
//	            JAXBContext context = JAXBContext.newInstance(Employee.class);
//	            Unmarshaller un = context.createUnmarshaller();
//	            Employee emp = (Employee) un.unmarshal(new File(FILE_NAME));
//	            return emp;
//	        } catch (JAXBException e) {
//	            e.printStackTrace();
//	        }
//	        return null;
//	    }
//	 
//	 
//	    private static void jaxbObjectToXML(Employee emp) {
//	 
//	        try {
//	            JAXBContext context = JAXBContext.newInstance(Employee.class);
//	            Marshaller m = context.createMarshaller();
//	            //for pretty-print XML in JAXB
//	            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//	 
//	            // Write to System.out for debugging
//	            // m.marshal(emp, System.out);
//	 
//	            // Write to File
//	            m.marshal(emp, new File(FILE_NAME));
//	        } catch (JAXBException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	 
//	}
//	
//}
