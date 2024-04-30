package com.example.oops.Controllers;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.oops.models.Comment;
import com.example.oops.models.post;
import com.example.oops.models.user;
import com.example.oops.Repository.CommentRepository;
import com.example.oops.Repository.PostRepository;
import com.example.oops.Repository.UserRepository;

@RestController
@RequestMapping("")
public class controllers {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/user")
    public ResponseEntity<Object> getUser(@RequestParam("userID") Long id) {
        Optional<user> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            user use = userOptional.get();
            UserDetailsResponse userDetailsResponse = new UserDetailsResponse(
                use.getName(),
                use.getuserID(),
                use.getEmail()
            );
            return ResponseEntity.ok(userDetailsResponse);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "User does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    // Custom response object for user details
    static class UserDetailsResponse 
    {
        private String name;
        private Long userID;
        private String email;

        public UserDetailsResponse(String name, Long userID, String email) {
            this.name = name;
            this.userID = userID;
            this.email = email;
        }

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getuserID() {
            return userID;
        }

        public void setuserID(Long userID) {
            this.userID = userID;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////
	@PostMapping("/login")////////////////////////////////////////////////////////////
    public ResponseEntity<?> login(@RequestBody user Body) {
        Optional<user> userOptional = userRepository.findByEmailAndPassword(Body.getEmail(), Body.getPassword());

        if (userOptional.isPresent()) {
            return ResponseEntity.ok("Login Successful");
        } 
        else if(userRepository.findByEmail(Body.getEmail()).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "UserName/Password Incorrect");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        else
        {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "User does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
	@PostMapping("/signup")////////////////////////////////////////////////////////////
    public ResponseEntity<?> signup(@RequestBody user Body) 
    { 
        if (userRepository.existsByEmail(Body.getEmail())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Forbidden,Account already exists");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } else {
           user userOptionall=new user(Body.getName(),Body.getPassword(),Body.getEmail());
            userRepository.save(userOptionall);
            return ResponseEntity.ok("Account Creation Successful");
        }
    }
		

    // Define RequestBody class according to your requirements
   
	@GetMapping("/")
	public ResponseEntity<?> getUsersBy() {
        
        
		List<post> sortedPosts = postRepository.findAll()
                                               .stream()
                                               .sorted(Comparator.comparingLong(post::getpostID).reversed())
                                               .collect(Collectors.toList());
        List<HashMap<String,Object>> results=new ArrayList<>();
        for(post ps : sortedPosts)
        {
            
        LinkedHashMap<String,Object> mp1=new LinkedHashMap<>();
        mp1.put("postID",ps.getpostID());
        mp1.put("postBody",ps.getPostBody());
        mp1.put("date",ps.getDate());
        List<LinkedHashMap<String,Object>> result=new ArrayList<>();

		for(Comment com : ps.getCommentss())
        {
            LinkedHashMap<String,Object> mp3=new LinkedHashMap<>(); 
            mp3.put("commentID",com.getcommentID());
            mp3.put("commentBody",com.getcommentBody());

            LinkedHashMap<String,Object> mp4=new LinkedHashMap<>();
            mp4.put("userID",com.getUse().getuserID());
             mp4.put("name",com.getUse().getName()); 
             mp3.put("commentCreator",mp4);

             result.add(mp3);
        }
        mp1.put("comments",result);
        results.add(mp1);
	}
    return ResponseEntity.ok(results);
} 	

    @PostMapping("/post")
	public ResponseEntity<?> createPost(@RequestBody postRequest ps ) {

            if(userRepository.existsById(ps.getuserID()))
		      {
                Optional<user> us=userRepository.findById(ps.getuserID());
                post ps_new=new post(ps.getPostBody(),us.get());
        
                List<post> pos=us.get().getPosts();
                pos.add(ps_new);
                us.get().setPosts(pos);
                postRepository.save(ps_new);
              return ResponseEntity.ok("Post created successfully");
              }
              else
              {
                Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "User does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
              }
	}
    @PatchMapping("/post")
    public ResponseEntity<?> updatePost(@RequestBody postRequest ps)
    {
        if(postRepository.existsById(ps.getpostID()))
        {
          Optional<post> ps_original=postRepository.findById(ps.getpostID());
          ps_original.get().setPostBody(ps.getPostBody());
          postRepository.save(ps_original.get());

        return ResponseEntity.ok("Post edited successfully");
        }
        else
        {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } 
    }
   
    static class postRequest
    {
        private long postID;
	    private String postBody;
	    private long userID;

        public postRequest() {
        }
        
        public postRequest(String postBody,long postID,long userID) {
            super();
            this.userID=userID;
            this.postBody=postBody;
            this.postID=postID;
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
        public long getuserID()
        {
            return userID;
        }
        public void setuserID(long userID)
        {
         this.userID=userID;
        }
    }
    
    @GetMapping("/post")
	public ResponseEntity<?> getPostById(@RequestParam("postID") Long id) {

        if(!postRepository.existsById(id))
       { Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("Error", "Post does not exist");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
       }
		Optional<post> ps = postRepository.findById(id);

        LinkedHashMap<String,Object> mp1=new LinkedHashMap<>();
        mp1.put("postID",ps.get().getpostID());
        mp1.put("postBody",ps.get().getPostBody());
        mp1.put("date",ps.get().getDate());
        List<LinkedHashMap<String,Object>> result=new ArrayList<>();
    
    
		for(Comment com : ps.get().getCommentss())
        {
            LinkedHashMap<String,Object> mp3=new LinkedHashMap<>(); 
            mp3.put("commentID",com.getcommentID());
            mp3.put("commentBody",com.getcommentBody());

            LinkedHashMap<String,Object> mp4=new LinkedHashMap<>();
            mp4.put("userID",com.getUse().getuserID());
             mp4.put("name",com.getUse().getName()); 

             mp3.put("commentCreator",mp4);

             result.add(mp3);
        }
        mp1.put("comments",result);
        return ResponseEntity.ok(mp1);
	} 

	@DeleteMapping("/post")
	public ResponseEntity<?> deletePostById(@RequestParam("postID") Long id)	
	{
	
            if (postRepository.existsById(id)) {
                postRepository.deleteById(id);
                return ResponseEntity.ok("Post deleted successfully");
            } 
            else {
                Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
    }
    
    @PostMapping("/comment")
	public ResponseEntity<?> createComment(@RequestBody commentRequest cs ) {
        if(userRepository.existsById(cs.getuserID()))
        {
            if(postRepository.existsById(cs.getpostID()))
            {
                Optional<user> us=userRepository.findById(cs.getuserID()); 
                Optional<post> ps=postRepository.findById(cs.getpostID());
                List<Comment> com=ps.get().getCommentss();
                List<Comment> com2=us.get().getComments();
                Comment cs_new= new Comment(cs.getCommentBody(),ps.get(),us.get());
                com.add(cs_new);
                com2.add(cs_new);
                ps.get().setCommentss(com);
                us.get().setComments(com2);
                commentRepository.save(cs_new);
                return ResponseEntity.ok("Comment created Successfully");
            }
            else
            {
                Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        }
        else
        {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "User does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
	}
    @GetMapping("/comment")
	public ResponseEntity<?> getCommentById(@RequestParam("commentID") Long id) {
        if (!commentRepository.existsById(id)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        Optional<Comment> us = commentRepository.findById(id);
        LinkedHashMap<String,Object> mp1 = new LinkedHashMap<>();
        mp1.put("commentID",us.get().getcommentID());
        mp1.put("commentBody",us.get().getcommentBody());

        LinkedHashMap<String,Object> mp2 = new LinkedHashMap<>();
        mp2.put("userID",us.get().getUse().getuserID());
        mp2.put("name",us.get().getUse().getName());

        mp1.put("commentCreater",mp2);

        return ResponseEntity.ok(mp1);
	} 

	@DeleteMapping("/comment")
	public ResponseEntity<?> deleteCommentById(@RequestParam("commentID") Long id)	
	{
	
            // Check if the post exists
            if (commentRepository.existsById(id)) {
                commentRepository.deleteById(id);
                return ResponseEntity.ok("Comment deleted");
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Error", "Comment does not exist");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        
    }

    @PatchMapping("/comment")
    public ResponseEntity<?> updateComment(@RequestBody commentRequest cs)
    {
        if(commentRepository.existsById(cs.getcommentID()))
        {
          Optional<Comment> cs_original=commentRepository.findById(cs.getcommentID());
          cs_original.get().setcommentBody(cs.getCommentBody());
          commentRepository.save(cs_original.get());
        return ResponseEntity.ok("Comment edited Successfully");
        }
        else
        {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } 
    }
    static class commentRequest
    {
        private long commentID;
        private long postID;
	    private String commentBody;
	    private long userID;

        public commentRequest() {
        }
        
        public commentRequest(String commentBody,long postID,long userID) {
            super();
            this.userID=userID;
            this.commentBody=commentBody;
            this.postID=postID;
        }
        public long getpostID() {
            return postID;
        }
        public void setPostsId(long postID)
        {
            this.postID=postID;
        }
        
        public String getCommentBody() {
            return commentBody;
        }
        public void setComment(String commentBody) {
            this.commentBody = commentBody;
        }
        public long getuserID()
        {
            return userID;
        }
        public void setuserID(long userID)
        {
         this.userID=userID;
        }
        public long getcommentID()
        {
            return commentID;
        }
        public void setcommentID(long commentID)
        {
            this.commentID=commentID;
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> allUsers()
    {
        List<User> users=new ArrayList<>();
        for(user  us: userRepository.findAll())
        {
            User us2=new User(us.getName(),us.getuserID(),us.getEmail());

            users.add(us2);
        }
        return ResponseEntity.ok(users);
    }
    static class User
    {

	private String name;

	private long userID;
	
	private String email;

    public User(String name,long userID,String email)
    {
        this.name=name;
        this.userID=userID;
        this.email=email;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getuserID() {
		return userID;
	}
	public void setuserID(long userID) {
		this.userID = userID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
}
