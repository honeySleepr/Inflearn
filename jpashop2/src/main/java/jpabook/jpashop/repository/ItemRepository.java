package jpabook.jpashop.repository;

import java.util.List;
import javax.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

	private final EntityManager em;

	public void save(Item item) {
		// id == null 이라는건 아직 db에 저장 안됐다는거니까!
		if (item.getId() == null) {
			em.persist(item);
		} else {
			em.merge(item); // update 같은 것
		}
	}

	public Item findOne(Long id) {
		return em.find(Item.class, id);
	}

	public List<Item> findAll() {
		return em.createQuery("select i from Item i", Item.class)
			.getResultList();
	}
}
