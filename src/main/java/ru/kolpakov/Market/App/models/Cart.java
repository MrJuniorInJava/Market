package ru.kolpakov.Market.App.models;

import ru.kolpakov.Market.SecurityForApp.models.Person;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "Cart")
public class Cart {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(mappedBy = "cart",fetch = FetchType.EAGER)
    private List<Product> products;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "person_id",referencedColumnName = "id")
    private Person owner;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }


    public  void addProductToCart(Product product){
        if(this.products==null){
            products=new ArrayList<>();
        }
        this.products.add(product);
        product.setCart(this);
    }
}
