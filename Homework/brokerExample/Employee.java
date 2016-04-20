package brokerExample;

/* Generated by Together */

public class Employee
{
   private double sal;
   private String job;
   private String name;
   private int id;

   /**
    * @clientCardinality 0..*
    * @supplierCardinality 1 
    */
   private Department department;

   public Employee()
   {
    	this(0,null,null,0.0, null);
   }
   public Employee(int id, String name, String job, double sal, Department department)
   {
    	this.id = id;
        this.name = name;
        this.job = job;
        this.sal = sal;
        this.department = department;
   }
   public void setId(int id)
   {
    	this.id = id;
   }
   public int getId()
   {
    	return id;
   }
   public void setName(String name)
   {
    	this.name = name;
   }
   public String getName()
   {
    	return name;
   }
   public void setJob(String job)
   {
    	this.job = job;
   }
   public String getJob()
   {
    	return job;
   }
   public void setSalary(double sal)
   {
    	this.sal = sal;
   }
   public double getSalary()
   {
    	return sal;
   }
   public Department getDepartment()
   {
    	return department;
   }
   public void setDepartment(Department department)
   {
       this.department = department;
   }
   public String toString()
   {
    	return "\nEmployee Id      : " + getId() +
			   "\nEmployee Name    : " + getName() +
               "\nJob              : " + getJob() +
               "\nSalary           : " + getSalary() +
               "\nDepartment       : " + department.getName();
   }
}
