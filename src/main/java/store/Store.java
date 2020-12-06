package store;

import model.Candidate;
import model.Photo;
import model.Post;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();
    void save(Post post);
    Post findById(int id);

    Collection<Candidate> findAllCandidates();
    void save(Candidate candidate);
    Candidate findCandidateById(int id);
    void delete(Candidate candidate);

    Collection<Photo> findAllPhotos();
    Photo save(Photo photo);
    Photo findPhotoById(int id);
    void delete(Photo photo);

}
