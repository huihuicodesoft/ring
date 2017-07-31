package cn.com.wh.ring.database;

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

        new DaoGenerator().generateAll(schema, "E:\\project\\developer\\android\\ring\\app\\src\\main\\java");
    }

    private static void addPostPublish(Schema schema) {
        Entity note = schema.addEntity("PostPublish");
        note.addIdProperty();
        note.addStringProperty("userId").notNull();
        note.addStringProperty("content");
        note.addStringProperty("mediaContent");
        note.addIntProperty("mediaType");
        note.addIntProperty("type").notNull();
        note.addLongProperty("time").notNull();
        note.addBooleanProperty("anonymous");
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
