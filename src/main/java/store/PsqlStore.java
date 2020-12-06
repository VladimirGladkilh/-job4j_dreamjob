package store;

import model.Photo;
import org.apache.commons.dbcp2.BasicDataSource;
import model.Candidate;
import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private final Logger LOG = LoggerFactory.getLogger(PsqlStore.class);
    private final BasicDataSource pool = new BasicDataSource();


    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name"), it.getInt("photoid")));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return candidates;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO post(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return post;
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("UPDATE post set name = ? where id = ?", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public Post findById(int id) {
        Post posts = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post where id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    posts = new Post(it.getInt("id"), it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return posts;
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }
    private Candidate create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO candidate(name, photoId) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getPhoto() != null ? candidate.getPhoto().getId() : 0);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return candidate;
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("UPDATE candidate set name = ?, photoId= ? where id = ?", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getPhoto() != null ? candidate.getPhoto().getId() : 0);
            ps.setInt(3, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
    @Override
    public Candidate findCandidateById(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate where id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    candidate = new Candidate(it.getInt("id"), it.getString("name"), it.getInt("photoid"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return candidate;
    }

    @Override
    public void delete(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("DELETE FROM candidate where id = ?")
        ) {
            ps.setInt(1, candidate.getId());
            try {
                ps.executeQuery();
            } catch (SQLException s) {
                LOG.error(s.getMessage());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public Collection<Photo> findAllPhotos() {
        List<Photo> photos = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM photo")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    photos.add(new Photo(it.getInt("id"), it.getString("path")));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return photos;
    }

    @Override
    public Photo save(Photo photo) {
        if (photo.getId() == 0) {
            return create(photo);
        } else {
            update(photo);
            return photo;
        }
    }

    private Photo create(Photo photo) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO photo(path ) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, photo.getPath());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    photo.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return photo;
    }

    private Photo update(Photo photo) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("UPDATE photo set path = ? where id = ?", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, photo.getPath());
            ps.setInt(2, photo.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return photo;
    }

    @Override
    public Photo findPhotoById(int id) {
        Photo photo = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM photo where id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    photo = new Photo(it.getInt("id"), it.getString("path"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return photo;
    }

    @Override
    public void delete(Photo photo) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("DELETE FROM photo where id = ?")
        ) {
            ps.setInt(1, photo.getId());
            try {
                ps.executeQuery();
            } catch (SQLException s) {
                LOG.error(s.getMessage());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}