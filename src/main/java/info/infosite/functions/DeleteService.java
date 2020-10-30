package info.infosite.functions;

import info.infosite.entities.gentable.Col;
import info.infosite.entities.gentable.Menu;
import info.infosite.entities.gentable.SubMenu;
import info.infosite.entities.gentable.Tab;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class DeleteService {
    @PersistenceContext
    private EntityManager entityManager;

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public void DeleteSubMenu(SubMenu subMenu) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.delete(subMenu);
        tx.commit();
    }

    public void DeleteColumn(Col column) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.delete(column);
        tx.commit();
    }

    public void DeleteTable(Tab table) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.delete(table);
        tx.commit();
    }

    public void DeleteMenu(Menu menu) {
        Session session = getSession();
        Transaction tx = session.beginTransaction();
        session.delete(menu);
        tx.commit();
    }
}
