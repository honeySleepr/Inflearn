package jpql;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class JpaMainProjection {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Member member = new Member();
			member.setUsername("member1");
			member.setAge(10);
			em.persist(member);

			em.flush();
			em.clear();

			// 여러 값을 DTO에 담아서 가져오는 방법
			List<MemberDto> resultList = em.createQuery(
					"select new jpql.MemberDto(m.username, m.age) from Member m", MemberDto.class)
				.getResultList();

			TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
			Query query1 = em.createQuery("select m.age, m.username from Member m");

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();


	}
}
