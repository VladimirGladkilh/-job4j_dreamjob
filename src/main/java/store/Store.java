package store;

import model.*;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();
    void save(Post post);
    Post findById(int id);
    void delete(Post post);

    Collection<Candidate> findAllCandidates();
    void save(Candidate candidate);
    Candidate findCandidateById(int id);
    void delete(Candidate candidate);

    Collection<Photo> findAllPhotos();
    Photo save(Photo photo);
    Photo findPhotoById(int id);
    void delete(Photo photo);

    Collection<User> findAllUsers();
    User save(User user);
    User findUserById(int id);
    User findUserByEmail(String name);
    void delete(User user);

    Collection<City> findAllCities();
    City save(City city);
    City findCityById(int id);
    void delete(City city);
}
