package com.example.filmography.Telegram.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start bot"),
            new BotCommand("/what_to_see", "Что посмотреть?"),
            new BotCommand("/find_all_films_director", "В каких фильмах участвовал?"),
            new BotCommand("/help", "Информация")
    );

    String HELP_TEXT = "This bot will help to choise the film. " +
            "The following commands are available to you:\n\n" +
            "/start - start the bot\n" +
            "/what_to_see - Что посмотреть?\n" +
            "/find_all_films_director - В каких фильмах участвовал?\n" +
            "/help - help menu";
}
