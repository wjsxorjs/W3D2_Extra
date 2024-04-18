package homework;

public class EmpVO {
	String empno, ename, job, hiredate, dname, city;
	
	public EmpVO() {}

	public EmpVO(String empno, String ename, String job, String hiredate, String dname, String city) {
		super();
		this.empno = empno;
		this.ename = ename;
		this.job = job;
		this.hiredate = hiredate;
		this.dname = dname;
		this.city = city;
	}

	public void setEmpno(String empno) {
		this.empno = empno;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public void setHiredate(String hiredate) {
		this.hiredate = hiredate;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public void setCity(String city) {
		this.city = city;
	}

	
	
	public String getEmpno() {
		return empno;
	}

	public String getEname() {
		return ename;
	}

	public String getJob() {
		return job;
	}

	public String getHiredate() {
		return hiredate;
	}

	public String getDname() {
		return dname;
	}

	public String getCity() {
		return city;
	};
	
	
	
	
	

}
