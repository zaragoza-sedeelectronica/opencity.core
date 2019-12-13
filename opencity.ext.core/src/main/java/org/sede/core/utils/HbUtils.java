package org.sede.core.utils;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

public class HbUtils {
	private HbUtils() {
		super();
	}
	@SuppressWarnings("unchecked")
	public static <T>  T deproxy (T obj) {
        if (obj == null)
            return obj;
        if (obj instanceof HibernateProxy) {
            //      -- loading, if necessary.
            HibernateProxy proxy = (HibernateProxy) obj;
            LazyInitializer li = proxy.getHibernateLazyInitializer();
            return (T)  li.getImplementation();
        } 
        return obj;
    }


    public static boolean isProxy (Object obj) {
        return (obj instanceof HibernateProxy);
    }

    // ----------------------------------------------------------------------------------


    public static boolean isEqual (Object o1, Object o2) {
        if (o1 == o2)
            return true;
        if (o1 == null || o2 == null) 
            return false;
        Object d1 = deproxy(o1);
        Object d2 = deproxy(o2);
        return (d1 == d2 || d1.equals(d2));
    }

    public static boolean notEqual (Object o1, Object o2) {
        return ! isEqual( o1, o2);
    }

    // ----------------------------------------------------------------------------------

    public static boolean isSame (Object o1, Object o2) {
        if (o1 == o2)
            return true;
        if (o1 == null || o2 == null) 
            return false;
        Object d1 = deproxy(o1);
        Object d2 = deproxy(o2);
        return (d1 == d2);
    }

    public static boolean notSame (Object o1, Object o2) {
        return ! isSame( o1, o2);
    }



    // ----------------------------------------------------------------------------------

    public static Class<?> getClassWithoutInitializingProxy (Object obj) {
        if (obj instanceof HibernateProxy) {
            HibernateProxy proxy = (HibernateProxy) obj;
            LazyInitializer li = proxy.getHibernateLazyInitializer();
            return li.getPersistentClass();
        } 
        // Not a Proxy.
        return obj.getClass();
    }

}
