import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;
import org.greenrobot.greendao.generator.ToMany;

/**
 * Created by Hui on 2017/7/31.
 */

public class ExampleDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "cn.com.wh.ring.database");

        addPostPublish(schema);
        addUserInfo(schema);

        new DaoGenerator().generateAll(schema, "E:\\project\\developer\\android\\ring\\app\\src\\test");
    }

    private static void addPostPublish(Schema schema) {
        Entity note = schema.addEntity("PostPublish");
        note.addIdProperty().primaryKey().autoincrement();
        note.addStringProperty("token").notNull();
        note.addStringProperty("content");
        note.addStringProperty("mediaContent");
        note.addStringProperty("type").notNull();
        note.addBooleanProperty("anonymous");
        note.addIntProperty("state");
        note.addStringProperty("addressCode");
        note.addLongProperty("time").notNull();
    }

    private static void addUserInfo(Schema schema) {
        Entity note = schema.addEntity("UserInfo");
        note.addIdProperty().primaryKey().autoincrement();
        note.addLongProperty("userId").unique();
        note.addLongProperty("infoId");
        note.addStringProperty("nickname");
        note.addLongProperty("birthday");
        note.addIntProperty("sex");
        note.addStringProperty("avatar");
        note.addStringProperty("signature");
        note.addStringProperty("addressCode");
        note.addLongProperty("lastModifiedTime");
    }

    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);

        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }
}
