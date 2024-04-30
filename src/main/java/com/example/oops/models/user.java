package com.example.oops.models;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name= "u_table")
public class user{
	
	@Column(name = "name")
	private String name;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userID;

	@OneToMany(mappedBy = "us", cascade = CascadeType.ALL)
	private List<post> posts = new ArrayList<>();

	@OneToMany(mappedBy = "use", cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();


	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;

	public user()
	{

	}
	public user(String name, String password, String email) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
	}
	public long getuserID() {
		return userID;
	}
	public void setuserID(long id) {
		this.userID = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public List<post> getPosts()
	{
		return posts;
	}
	public void setPosts(List<post> posts)
	{
		this.posts=posts;	
	}

	public List<Comment> getComments()
	{
		return comments;
	}
	public void setComments(List<Comment> comments)
	{
		this.comments=comments;	
	}
}
