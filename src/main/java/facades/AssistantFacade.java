package facades;

import dtos.Assistant.AssistantsDTO;
import entities.Assistant;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class AssistantFacade {
    private static EntityManagerFactory emf;
    private static AssistantFacade instance;

    private AssistantFacade() {
    }

    public static AssistantFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AssistantFacade();
        }
        return instance;
    }

    public AssistantsDTO getAllAssistants() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Assistant> query = em.createQuery("SELECT a FROM Assistant a", Assistant.class);
            List<Assistant> result = query.getResultList();
            return new AssistantsDTO(result);
        } finally {
            em.close();
        }
    }
}
