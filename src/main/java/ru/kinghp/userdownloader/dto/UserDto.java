package ru.kinghp.userdownloader.dto;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {

    @CsvBindByPosition(position = 0)
    private String firstName;
    @CsvBindByPosition(position = 1)
    private String LastName;

}
