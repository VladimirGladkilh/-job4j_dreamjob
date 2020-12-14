package model;

import store.PsqlStore;

import java.util.Objects;

public class Candidate {
    private int id;
    private String name;
    private Photo photo;
    private City city;

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Candidate(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Candidate(int id, String name, int photoid) {
        this.id = id;
        this.name = name;
        if (photoid > 0) {
            this.photo = PsqlStore.instOf().findPhotoById(photoid);
        } else {
            this.photo = new Photo(0, "");
        }
    }
    public Candidate(int id, String name, Photo photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public Candidate(int id, String name, Photo photo, City city) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return id == candidate.id &&
                Objects.equals(name, candidate.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}