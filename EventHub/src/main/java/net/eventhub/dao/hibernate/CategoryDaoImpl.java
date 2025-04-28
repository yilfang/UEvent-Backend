package net.eventhub.dao.hibernate;

import org.springframework.stereotype.Repository;

import net.eventhub.dao.CategoryDao;
import net.eventhub.dao.EventDao;
import net.eventhub.domain.Category;
import net.eventhub.domain.Event;

@Repository("categoryDao")
public class CategoryDaoImpl extends AbstractGenericDao<Category> implements CategoryDao{

}
