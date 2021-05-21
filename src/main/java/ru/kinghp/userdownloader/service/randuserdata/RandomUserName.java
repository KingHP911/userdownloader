package ru.kinghp.userdownloader.service.randuserdata;

import lombok.Data;

import java.io.Serializable;

@Data
public class RandomUserName implements Serializable {

    private String title;
    private String first;
    private String last;
}
