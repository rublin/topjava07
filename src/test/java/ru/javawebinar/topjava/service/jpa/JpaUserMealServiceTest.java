package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserMealServiceTest;

import static ru.javawebinar.topjava.Profiles.JPA;
import static ru.javawebinar.topjava.Profiles.POSTGRES;

@ActiveProfiles(JPA)
public class JpaUserMealServiceTest extends AbstractUserMealServiceTest {
}