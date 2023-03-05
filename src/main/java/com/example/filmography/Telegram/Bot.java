package com.example.filmography.Telegram;

import com.example.filmography.Telegram.components.Buttons;
import com.example.filmography.model.Director;
import com.example.filmography.model.Film;
import com.example.filmography.repository.DirectorRepository;
import com.example.filmography.repository.FilmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Set;

import static com.example.filmography.Telegram.components.BotCommands.HELP_TEXT;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    @Autowired
    FilmRepository filmRepository;
    @Autowired
    DirectorRepository directorRepository;

    @Value("${bot.username}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    //точка входа, куда будут поступать сообщения от пользователей. Отсюда будет идти вся новая логика
    @Override
    public void onUpdateReceived(Update update) {

        String message = "";
        String chatId = "";
        String memberName = "";

        if (update.hasMessage() && update.getMessage().hasText()) {
            message = update.getMessage().getText().trim();
            chatId = update.getMessage().getChatId().toString();
            memberName = update.getMessage().getFrom().getFirstName();

            if (message.contains("/find_all_films_director")) {
                findAllFilmsFromDirector(chatId, message);
            } else {
                botAnswerUtils(message, chatId, memberName);
            }
        } else if (update.hasCallbackQuery()) {
            message = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            memberName = update.getCallbackQuery().getFrom().getFirstName();

            botAnswerUtils(message, chatId, memberName);
        }
    }

    private void botAnswerUtils(String receivedMessage, String chatId, String userName) {
        switch (receivedMessage) {
            case "/start":
                startBot(chatId, userName);
                break;
            case "/help":
                sendHelpText(chatId, HELP_TEXT);
                break;
            case "/what_to_see":
                sendRandomFilmToUser(chatId);
                break;
            case "/find_all_films_director":
                findAllFilmsFromDirector(chatId);
                break;
            default:
                sendHelpText(chatId, HELP_TEXT);
                break;
        }
    }

    private void startBot(String chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hi, " + userName + "! I'm a Film-bot.'");
        message.setReplyMarkup(Buttons.inlineMarkup());

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendHelpText(String chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendRandomFilmToUser(String chatId) {

        Film film = filmRepository.getRandomFilm();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Посмотрите: " + film.getTitle() + " - жанр:" + film.getGenre().getGenreText() + ", страна: " + film.getCountry());

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void findAllFilmsFromDirector(String chatId) {

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Используйте команду /find_all_films_director <Фамилия участника>");

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void findAllFilmsFromDirector(String chatId, String receivedMessage) {

        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        String directorFio = receivedMessage.replace("/find_all_films_director", "").trim();

        if (directorFio == "") {
            message.setText("Не ввели фамилию!");
        } else {

            List<Director> directors = directorRepository.findDirectorsByDirectorFIOLike(directorFio);

            if (directors == null) {
                message.setText("Такого участника нет в базе");
            } else {
                String messageFilms = "";
                for (Director director : directors) {
                   List<Film> films = filmRepository.findFilmsByDirectorsId(director.getId());
                    for (Film film:films) {
                        messageFilms += film.getTitle()+ "; ";
                    }
                }
                message.setText((messageFilms == "") ? "Нет фильмов с этим учатсником" : messageFilms);
            }
        }

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
