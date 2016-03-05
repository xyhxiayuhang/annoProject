package annoProject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Test {

	public static void main(String[] args) {
		Filter f1 = new Filter();
		f1.setId(10);

		Filter f2 = new Filter();
		f2.setUserName("lucy");

		Filter f3 = new Filter();
		f3.setEmail("liu@sina.com,zh@163.com,77777@qq.com");

		String sql1 = query(f1);
		String sql2 = query(f2);
		String sql3 = query(f3);

		System.out.println(sql1);
		System.out.println(sql2);
		System.out.println(sql3);
	}

	private static String query(Filter f) {
		StringBuilder sb = new StringBuilder();
		// 1.��ȡ��class
		Class c = f.getClass();
		// 2.��ȡ��table������
		boolean exists = c.isAnnotationPresent(Table.class);
		if (!exists) {
			return null;
		}
		Table t = (Table) c.getAnnotation(Table.class);
		String tableName = t.value();
		sb.append("select * from ").append(tableName).append("where 1=1");
		// 3.�������е��ֶ�
		Field[] fArray = c.getDeclaredFields();
		for (Field field : fArray) {
			// 4.����ÿ���ֶζ�Ӧ��sql
			// 4.1�õ��ֶ���
			boolean fExists = field.isAnnotationPresent(Column.class);
			if (!exists) {
				continue;
			}
			Column column = field.getAnnotation(Column.class);
			String columnName = column.value();
			// 4.2�õ��ֶε�ֵ
			String fieldName = field.getName();
			String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Object fieldValue = null;
			try {
				Method getMethod = c.getMethod(getMethodName);
				fieldValue = getMethod.invoke(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 4.3ƴװsql
			sb.append(" and ").append(fieldName).append("=").append(fieldValue);
		}
		return sb.toString();

	}
}
