package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Courier {
    @NonNull private String login;
    @NonNull private String password;
    private String firstName;
}
