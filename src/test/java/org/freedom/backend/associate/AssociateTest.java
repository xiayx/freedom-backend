package org.freedom.backend.associate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xiayx
 */
@RunWith(SpringRunner.class)
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = WebMvcAutoConfiguration.class)
@ComponentScan
@PropertySource("classpath:org/freedom/backend/associate/application.properties")
public class AssociateTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private AssociateRepositoryImpl associateRepository;

    public static void initDatabase(EntityManager entityManager) {
        Set<Associate> associates = Stream.generate(Associate::new).limit(5).collect(Collectors.toSet());
        associates.forEach(entityManager::persist);

        associates.stream()
                .map(associate -> new Main(associate.getId(), Collections.singletonList(associate.getId())))
                .forEach(entityManager::persist);
    }

    @Test
    @Transactional
    @SuppressWarnings("unchecked")
    public void test() throws Exception {
        initDatabase(entityManager);
        List<Main> mains = entityManager.createQuery("select e from Main e").getResultList();
        AssociateUtils.setAssociate(mains, "associate", associateRepository, "associateId", "id");
        mains.forEach(main -> Assert.assertEquals(main.getAssociate().getId(), main.getAssociateId()));
        AssociateUtils.setAssociateCollection(mains, "associates", associateRepository, "associateIds", "id");
        mains.forEach(main -> Assert.assertEquals(main.getAssociates().stream().map(Associate::getId).collect(Collectors.toList()), main.getAssociateIds()));
    }


    @Service
    @SuppressWarnings("unchecked")
    @Transactional
    public static class AssociateRepositoryImpl implements
            AssociateSource<Long, Associate>,
            CollectionAssociateSource<Long, Associate> {

        @PersistenceContext
        private EntityManager entityManager;

        @Override
        public Associate getById(Long id) {
            return (Associate) entityManager.createQuery("select e from Associate e where e.id = ?1").setParameter(1, id).getSingleResult();
        }

        @Override
        public Collection<Associate> getCollectionById(Collection<Long> id) {
            return entityManager.createQuery("select e from Associate e where e.id in ?1").setParameter(1, id).getResultList();
        }

    }
}
