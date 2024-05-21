package ru.yarm.banksample.Configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

// Добавление возможность выполнение задач по расписанию
@Configuration
@EnableScheduling
public class SpringScheduleConfig {
}
