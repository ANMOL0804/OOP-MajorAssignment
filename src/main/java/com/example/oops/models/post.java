package com.example.oops.models;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


import jakarta.persistence.*;


@Entity
@Table(name = "p_table")
@Getter
@Setter
@AllArgsConstructor
public class post
{
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long postID;


	@Column(name = "postBody")
	private String postBody;

	@Column(name = "date")
	private LocalDate date;
	

	@ManyToOne(fetch = FetchType.EAGER) // Many posts belong to one user
	@JoinColumn(name = "user_id",nullable  =false)
	private user us;

	@OneToMany(mappedBy = "pos", cascade = CascadeType.ALL)
	private List<Comment> commentss = new ArrayList<>();


	public post() {
	}
	
	public post(String postBody,user us) {
		super();
		this.us=us;
		this.postBody=postBody;
		this.date=LocalDate.now();
	}
	public long getpostID() {
		return postID;
	}
	public void setPostsId(long postID)
	{
		this.postID=postID;
	}
	
	public String getPostBody() {
		return postBody;
	}
	public void setPostBody(String postBody) {
		this.postBody = postBody;
	}
	public user getUs()
	{
		return us;
	}
	public void setUs(user us)
	{
	 this.us=us;
	}
	public LocalDate geDate()
	{
		return date;
	}
	public void setDate()
	{
		this.date=LocalDate.now();
	}
	public List<Comment> getCommentss()
	{
		return commentss;
	}
	public void setCommentss(List<Comment> comments)
	{
		this.commentss=comments;
	}
}