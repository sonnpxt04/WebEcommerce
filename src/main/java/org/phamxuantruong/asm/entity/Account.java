package org.phamxuantruong.asm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "Accounts")
public class Account implements Serializable {
    @Id
    String username;
    String password;
    @Column(name = "fullname", nullable = false)
    String fullname;
    String email;
    @Column(name = "photo", nullable = false)
    private String photo = ""; // Gán giá trị mặc định
    @JsonIgnore
    @OneToMany(mappedBy = "account")
    List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    List<Authority> authorities;
}


