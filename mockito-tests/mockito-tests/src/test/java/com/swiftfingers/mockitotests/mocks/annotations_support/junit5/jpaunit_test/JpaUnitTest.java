package com.swiftfingers.mockitotests.mocks.annotations_support.junit5.jpaunit_test;

import com.swiftfingers.mockitotests.entity.Tutorial;
import com.swiftfingers.mockitotests.repository.BookRepository;
import com.swiftfingers.mockitotests.repository.TutorialRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;


/*By default, tests annotated with @DataJpaTest are transactional and roll back at the end of each test.
If you don’t want it, you can disable transaction management for a test or for the whole class
using @Transactional annotation: such as @Transactional(propagation = Propagation.NOT_SUPPORTED)*/

@DataJpaTest //is the annotation that Spring supports for a JPA test that focuses only on JPA components.
public class JpaUnitTest {

    // Alternative for EntityManager
    // Optional in this case, we can use repository to do the same stuff
    @Autowired
    private TestEntityManager entityManager; //TestEntityManager allows us to use EntityManager in tests.

    @Autowired
    private TutorialRepository repository;

    @Test
    public void should_find_NO_tutorials_if_repository_is_empty() {
        Iterable<Tutorial> tutorials = repository.findAll();

        assertThat(tutorials).isEmpty();
    }

    @Test
    public void should_store_a_tutorial() {
        Tutorial tutorial = repository.save(Tutorial.builder().title("JPA 101").description("Tut desc").published(true).build());

        assertThat(tutorial).hasFieldOrPropertyWithValue("title", "JPA 101");
        assertThat(tutorial).hasFieldOrPropertyWithValue("description", "Tut desc");
        assertThat(tutorial).hasFieldOrPropertyWithValue("published", true);
    }

    @Test
    public void should_find_all_tutorials() {
        Tutorial tut1 = Tutorial.builder().title("JPA 101").description("Tut desc 1").published(true).build();
        entityManager.persist(tut1);

        Tutorial tut2 = Tutorial.builder().title("JPA 102").description("Tut desc 2").published(true).build();
        entityManager.persist(tut2);

        Tutorial tut3 = Tutorial.builder().title("JPA 103").description("Tut desc 3").published(true).build();
        entityManager.persist(tut3);

        Iterable tutorials = repository.findAll();

        assertThat(tutorials).hasSize(3).contains(tut1, tut2, tut3);
    }

    @Test
    public void should_find_tutorial_by_id() {
        Tutorial tut1 = Tutorial.builder().title("JPA 101").description("Tut desc 1").published(true).build();
        entityManager.persist(tut1);

        Tutorial tut2 = Tutorial.builder().title("JPA 102").description("Tut desc 2").published(true).build();
        entityManager.persist(tut2);

        Tutorial foundTutorial = repository.findById(tut2.getId()).get();

        assertThat(foundTutorial).isEqualTo(tut2);
    }

    @Test
    public void should_find_published_tutorials() {
        Tutorial tut1 = Tutorial.builder().title("JPA 101").description("Tut desc 1").published(true).build();
        entityManager.persist(tut1);

        Tutorial tut2 = Tutorial.builder().title("JPA 102").description("Tut desc 2").published(false).build();
        entityManager.persist(tut2);

        Tutorial tut3 = Tutorial.builder().title("JPA 103").description("Tut desc 3").published(true).build();
        entityManager.persist(tut3);

        Iterable<Tutorial> tutorials = repository.findByPublished(true);

        assertThat(tutorials).hasSize(2).contains(tut1, tut3);
    }

    @Test
    public void should_find_tutorials_by_title_containing_string() {
        Tutorial tut1 = Tutorial.builder().title("Spring Boot 101").description("Tut desc 1").published(true).build();
        entityManager.persist(tut1);

        Tutorial tut2 = Tutorial.builder().title("Spring Boot 102").description("Tut desc 2").published(false).build();
        entityManager.persist(tut2);

        Tutorial tut3 = Tutorial.builder().title("JPA 103").description("Tut desc 3").published(true).build();
        entityManager.persist(tut3);

        Iterable<Tutorial> tutorials = repository.findByTitleContaining("ring");

        assertThat(tutorials).hasSize(2).contains(tut1, tut2);
    }

    @Test
    public void should_update_tutorial_by_id() {
        Tutorial tut1 = Tutorial.builder().title("Spring Boot 101").description("Tut desc 1").published(true).build();
        entityManager.persist(tut1);

        Tutorial tut2 = Tutorial.builder().title("Spring Boot 102").description("Tut desc 2").published(false).build();
        entityManager.persist(tut2);

        Tutorial updatedTut = Tutorial.builder().title("Updated spring boot title").description("Tut desc 2").published(false).build();

        Tutorial tut = repository.findById(tut2.getId()).get();
        tut.setTitle(updatedTut.getTitle());
        tut.setDescription(updatedTut.getDescription());
        tut.setPublished(updatedTut.isPublished());
        repository.save(tut);

        Tutorial checkTut = repository.findById(tut2.getId()).get();

        assertThat(checkTut.getId()).isEqualTo(tut2.getId());
        assertThat(checkTut.getTitle()).isEqualTo(updatedTut.getTitle());
        assertThat(checkTut.getDescription()).isEqualTo(updatedTut.getDescription());
        assertThat(checkTut.isPublished()).isEqualTo(updatedTut.isPublished());
    }

    @Test
    public void should_delete_tutorial_by_id() {
        Tutorial tut1 = Tutorial.builder().title("Spring Boot 101").description("Tut desc 1").published(true).build();
        entityManager.persist(tut1);

        Tutorial tut2 = Tutorial.builder().title("Spring Boot 102").description("Tut desc 2").published(false).build();
        entityManager.persist(tut2);

        Tutorial tut3 = Tutorial.builder().title("JPA 103").description("Tut desc 3").published(true).build();
        entityManager.persist(tut3);

        repository.deleteById(tut2.getId());

        Iterable<Tutorial> tutorials = repository.findAll();

        assertThat(tutorials).hasSize(2).contains(tut1, tut3);
    }

    @Test
    public void should_delete_all_tutorials() {
        entityManager.persist(Tutorial.builder().title("Spring Boot 101").description("Tut desc 1").published(false).build());
        entityManager.persist(Tutorial.builder().title("Spring Boot 102").description("Tut desc 2").published(true).build());

        repository.deleteAll();

        assertThat(repository.findAll()).isEmpty();
    }
}
