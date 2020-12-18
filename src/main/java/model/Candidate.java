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


    public Candidate(int id, String name, int photoid, int cityId) {
        Photo photo = new Photo(0, "");
        if (photoid > 0) {
            photo = PsqlStore.instOf().findPhotoById(photoid);
        }
        City city = new City(0, "");
        if (cityId > 0) {
            city = PsqlStore.instOf().findCityById(cityId);
        }
        initCandidate(id, name, photo, city);
    }


    public Candidate(int id, String name, Photo photo, City city) {
        initCandidate(id, name, photo, city);
    }

    private void initCandidate(int id, String name, Photo photo, City city) {
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