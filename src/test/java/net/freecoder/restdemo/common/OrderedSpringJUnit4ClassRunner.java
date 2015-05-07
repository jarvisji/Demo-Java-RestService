package net.freecoder.restdemo.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Add order support for SpringJUnit4ClassRunner. The test cases will be
 * executed according to (@code Order} annotation.
 * 
 * @author JiTing
 */
public class OrderedSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {

	/**
	 * Constructor.
	 * 
	 * @param clazz
	 *            Class.
	 * @throws InitializationError
	 *             InitializationError
	 */
	public OrderedSpringJUnit4ClassRunner(Class<?> clazz)
			throws InitializationError {
		super(clazz);
	}

	/**
	 * The test cases will be executed according to (@code Order} annotation.
	 * 
	 * @return List<FrameworkMethod> Test case methods.
	 */
	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		List<FrameworkMethod> list = super.computeTestMethods();
		Collections.sort(list, new Comparator<FrameworkMethod>() {
			public int compare(FrameworkMethod f1, FrameworkMethod f2) {
				Order o1 = f1.getAnnotation(Order.class);
				Order o2 = f2.getAnnotation(Order.class);
				if (o1 == null && o2 == null) {
					return 0;
				} else if (o1 == null) {
					return -1;
				} else if (o2 == null) {
					return 1;
				} else {
					return o1.value() - o2.value();
				}
			}
		});
		return list;
	}
}
