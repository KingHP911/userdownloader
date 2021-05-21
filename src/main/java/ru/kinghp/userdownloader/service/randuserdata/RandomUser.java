package ru.kinghp.userdownloader.service.randuserdata;

import lombok.Data;

import java.io.Serializable;

@Data
public class RandomUser implements Serializable {
    private RandomUserName name;
}
