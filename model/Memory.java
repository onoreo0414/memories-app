@Entity
public class Memory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String title;
    private String message;
    private String author;
    private String photoUrl; // static配下の写真 or アップロード先URL
}
