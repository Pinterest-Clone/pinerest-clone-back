package com.sparta.pinterest_clone.pin.PinRepository;

import com.sparta.pinterest_clone.pin.entity.Pin;
import com.sparta.pinterest_clone.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PinRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Pin save(Pin pin) {
        entityManager.persist(pin);
        return pin;
    }

    public void delete(Pin pin) {
        entityManager.remove(pin);
    }

    public Optional<Pin> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Pin.class, id));
    }
    public List<Pin> findByUser(User user) {
        String jpql = "SELECT p FROM Pin p WHERE p.user = :user";
        return entityManager.createQuery(jpql, Pin.class)
                .setParameter("user", user)
                .getResultList();
    }

    public List<Pin> findAllByOrderByCreatedAtDesc() {
        String jpql = "SELECT p FROM Pin p " +
                "ORDER BY p.createdAt DESC";
        return entityManager.createQuery(jpql, Pin.class)
                .getResultList();
    }


    public List<Pin> searchPinsByKeywordWithPriority(String keyword) {
        String jpql = "SELECT p FROM Pin p " +
                "WHERE p.title LIKE :keyword OR p.content LIKE :keyword " +
                "ORDER BY CASE WHEN p.title LIKE :keyword AND p.content LIKE :keyword THEN 1 " +
                "              WHEN p.title LIKE :keyword THEN 2 " +
                "              WHEN p.content LIKE :keyword THEN 3" +
                "              ELSE 4 " +
                "         END, p.createdAt DESC";
        List<Pin> pinsWithKeyword = entityManager.createQuery(jpql, Pin.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();

        jpql = "SELECT p FROM Pin p " +
                "WHERE p.title not LIKE :keyword AND p.content not LIKE :keyword " +
                "ORDER BY p.createdAt DESC";
        List<Pin> pinsWithoutKeyword = entityManager.createQuery(jpql, Pin.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();

        List<Pin> result = new ArrayList<>();
        result.addAll(pinsWithKeyword);
        result.addAll(pinsWithoutKeyword);
        return result;
    }

}
