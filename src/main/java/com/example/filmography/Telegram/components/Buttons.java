package com.example.filmography.Telegram.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Help");
    private static final InlineKeyboardButton FILM_BUTTON = new InlineKeyboardButton("Film");
    private static final InlineKeyboardButton DIRECTOR_BUTTON = new InlineKeyboardButton("Director");

    public static InlineKeyboardMarkup inlineMarkup() {
        START_BUTTON.setCallbackData("/start");
        HELP_BUTTON.setCallbackData("/help");
        FILM_BUTTON.setCallbackData("/what_to_see");
        DIRECTOR_BUTTON.setCallbackData("/find_all_films_director");


        List<InlineKeyboardButton> rowInline = List.of(START_BUTTON, HELP_BUTTON, FILM_BUTTON, DIRECTOR_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}
