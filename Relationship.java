


// ONE TO MANY
Entity(name = "Post")
@Table(name = "post")
public class Post {
 
    @Id
    @GeneratedValue
    private Long id;
 
    private String title;
 
    @OneToMany(cascade = CascadeType.ALL,)
    private List<Comment> comments = new ArrayList<>();
 
    //Constructors, getters and setters removed for brevity
}
 
@Entity
@Table(name = "comments")
public class Comment {
 
    @Id
    @GeneratedValue
    private Long id;
 
    private String review;
 
    //Constructors, getters and setters removed for brevity
}