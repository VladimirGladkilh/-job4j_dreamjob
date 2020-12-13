package store;



import model.Candidate;
import model.Photo;
import model.Post;
import model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {

    private static final AtomicInteger POST_ID = new AtomicInteger(4);
    private static final AtomicInteger CANDIDAT_ID = new AtomicInteger(4);
    private static final AtomicInteger PHOTO_ID = new AtomicInteger(4);
    private static final AtomicInteger USER_ID = new AtomicInteger(4);

    private static final MemStore INST = new MemStore();

    private Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private Map<Integer, Photo> photos = new ConcurrentHashMap<>();
    private Map<Integer, User> users = new ConcurrentHashMap<>();

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job"));
        posts.put(2, new Post(2, "Middle Java Job"));
        posts.put(3, new Post(3, "Senior Java Job"));
        photos.put(1, new Photo(1, "1.png"));
        photos.put(2, new Photo(2, "2.png"));
        photos.put(3, new Photo(3, "2.png"));
        candidates.put(1, new Candidate(1, "Junior Java", 1));
        candidates.put(2, new Candidate(2, "Middle Java", 2));
        candidates.put(3, new Candidate(3, "Senior Java", 3));
        users.put(1, new User(1, "mail1", "First", "123"));
        users.put(2, new User(2, "mail2", "Second", "123"));
        users.put(3, new User(3, "mail3", "Third", "123"));
    }

    public static MemStore instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }


    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    @Override
    public void delete(Post post) {
        posts.remove(post);
    }

    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDAT_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    public Candidate findCandidateById(int id) {
        return candidates.get(id);
    }

    @Override
    public void delete(Candidate candidate) {
        candidates.remove(candidate);
    }

    @Override
    public Collection<Photo> findAllPhotos() {
        return photos.values();
    }

    @Override
    public Photo save(Photo photo) {
        if (photo.getId() == 0) {
            photo.setId(PHOTO_ID.incrementAndGet());
        }
        photos.put(photo.getId(), photo);
        return photo;
    }

    @Override
    public Photo findPhotoById(int id) {
        return photos.get(id);
    }

    @Override
    public void delete(Photo photo) {
        photos.remove(photo);
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User save(User user) {
        if (user.getId() == 0) {
            user.setId(USER_ID.incrementAndGet());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findUserById(int id) {
        return users.get(id);
    }

    @Override
    public User findUserByEmail(String name) {
        return users.values().stream()
                .filter(user -> user.getEmail().toLowerCase().equals(name.toLowerCase()))
                .findFirst().orElse(null);
    }

    @Override
    public void delete(User user) {
        users.remove(user);
    }


}