package ru.kinghp.userdownloader.service.randuserdata;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AllRandomUsers implements Serializable {
    private List<RandomUser> Results;
}
