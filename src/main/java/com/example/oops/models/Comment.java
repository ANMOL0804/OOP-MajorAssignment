package com.example.oops.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="comment")
public class Comment {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long commentID;

    @ManyToOne(fetch = FetchType.EAGER) // Many posts belong to one user
	@JoinColumn(name = "userId")
    private user use;
    
    @ManyToOne(fetch = FetchType.EAGER) // Many posts belong to one user
	@JoinColumn(name = "posId")
    private post pos;

    private String commentBody;
   public Comment()
    {

    }
  public  Comment(String commentBody,post pos,user use)
    {

        this.commentBody=commentBody;
        this.pos=pos;
        this.use=use;
    }

    public String getcommentBody()
    {
        return commentBody;
    }
    public void setcommentBody(String commentBody)
    {
        this.commentBody=commentBody;
    }
    public user getUse()
    {
        return use;
    }
    public void setUse(user use)
    {
        this.use=use;
    }
    public post getPos()
    {
        return pos;
    }
    public void setPos(post pos)
    {
        this.pos=pos;
    }
    public void setcommentID(long commentID)
    {
        this.commentID=commentID;
    }
    public long getcommentID()
    {
        return commentID;
    }
}
