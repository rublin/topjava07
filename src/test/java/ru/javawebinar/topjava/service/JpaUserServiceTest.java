package ru.javawebinar.topjava.service;

import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.javawebinar.topjava.Profiles;

/**
 * Created by Sheremet on 02.07.2016.
 */
@ActiveProfiles({Profiles.ACTIVE_DB, Profiles.JPA})
public class JpaUserServiceTest extends UserServiceTest {}
