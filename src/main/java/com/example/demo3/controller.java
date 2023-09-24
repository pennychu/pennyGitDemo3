package com.example.demo3;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/users")
public class controller {
    // new ArrayList 才能.ADD .REMOVE
    private List<User> users = new ArrayList<>(Arrays.asList(
            new User("Jason", 20),
            new User("Alan", 22),
            new User("David", 21),
            new User("Monika", 20),
            new User("Angela", 22)
    ));

    @GetMapping
    @ResponseBody
    public List<User> getUsers(@RequestParam(required = false, defaultValue = "0") int age) {
        if (age == 0) {
            return users;
        } else {
            List<User> filteredUsers = new ArrayList<>();
            for (User user : users) {
                if (user.getAge() == age) {
                    filteredUsers.add(user);
                }
            }
            return filteredUsers;
        }
    }

    @GetMapping("/{name}")
    @ResponseBody
    public User getUserBy(@PathVariable String name) {
        User user = users.stream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + name + " not found");
        }
        return user;
    }

    //實作POST
    @PostMapping("/Create")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        User existingUser = users.stream()
                .filter(u -> u.getName().equals(user.getName()))
                .findFirst()
                .orElse(null);

        if (existingUser == null) {
            users.add(user);
            return ResponseEntity.ok("User created successfully" + user.getName());
        } else {
            // 如果用户已存在，你可以返回一个适当的响应，例如冲突状态码
            //throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists: " + user.getName());
            return ResponseEntity.ok("User already exists:"+user.getName());
        }
    }


    //實作PUT
    @PutMapping("/UpDate/{name}")
    public User modifyUser(@PathVariable String name, @RequestBody User user) {
        User found = users.stream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .orElse(null);
                //.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + name + " not found"));
         if (found != null)
         {
             found.setAge(user.getAge());
         }

        return found;

    }

    //實作Delete
    @DeleteMapping("/Delete/{name}")
    public ResponseEntity<String> removeUser(@PathVariable String name) {
        User found = users.stream()
                .filter(user -> user.getName().equals(name))
                .findFirst()
                .orElse(null);
                //.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + name + " not found"));
        if (found != null)
        {
            users.remove(found);
            return ResponseEntity.ok("User already Delete" + found.getName());
        }
        return ResponseEntity.ok("User not found");
    }
    //實作PATCH
    @PatchMapping("/Patch/{name}")
    public ResponseEntity<User> patchUser(@PathVariable String name, @RequestBody User updatedUser) {
        // 根据id查找要更新的用户
        //User existingUser = userService.findById(id);
        User foundUSER = users.stream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (foundUSER == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + updatedUser.getName() + " not found");
        }

        // 执行部分更新，例如只更新用户的某些属性
//        if (updatedUser.getName() != null) {
//            foundUSER.setName(updatedUser.getName());
//        }
        Integer age = updatedUser.getAge();
        if (age != null) {
            foundUSER.setAge(age);
        }

        // 保存更新后的用户
        //userService.save(existingUser);

        return new ResponseEntity<>(foundUSER, HttpStatus.OK);
    }
}

class User {
    private int Id;
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
